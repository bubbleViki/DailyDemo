package com.meritit.dataimport.queue;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.meritit.dataimport.service.IDataImportService;
import com.meritit.dataimport.service.IFileUploadService;
import com.meritit.dataimport.service.IIndexService;

/**
 * 接收消息队列的数据并存储到hbase
 * 
 * @author Lenovo
 * 
 */
@Service("queueReceiver")
public class QueueReceiver {

	@Autowired
	private IDataImportService dataImportService;
	
	@Resource(name="index.indexService")
	private IIndexService indexService;
	
	@Resource(name="file.fileUploadService")
	private IFileUploadService fileUploadService;;
	
	private Map ruleMap = new ConcurrentHashMap<>();
	
	@Value("${activemq.ip}")
	private String host;
	
	@Value("${activemq.port}")
	private String port;

	/**
	 *接收activemq中的数据
	 */
	public void acceptMsg() {
		try {
			
			System.out.println("tcp://"+host+":"+port+"");
			
			
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(
					"tcp://"+host+":"+port+"");

			//ExecutorService pool = Executors.newCachedThreadPool();
			
			ExecutorService pool = Executors.newSingleThreadExecutor();
			
			pool.execute(new MyT(cf, dataImportService,ruleMap, indexService));
			
//			for (int i = 0; i < 1; i++) {
//				new MyT(cf, dataImportService).start();
//			}

		} catch (Exception err) {
			err.printStackTrace();
		} finally {

		}
	}
	
	
	public void acceptFilesMsg(){
	  try {
			
			System.out.println("tcp://"+host+":"+port+"");
			
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(
					"tcp://"+host+":"+port+"");

			ExecutorService pool = Executors.newSingleThreadExecutor();
			
			pool.execute(new MyFileHandler(cf, fileUploadService));
			
		} catch (Exception err) {
			err.printStackTrace();
		} finally {

		}
	}
	
	
	public void closeResource(){
		dataImportService.closeTable();
	}

}

class MyT extends Thread {

	private ActiveMQConnectionFactory cf = null;
	
	private IDataImportService dataImportService = null;
	
	private IIndexService indexService;
	
	//key 为ruleid  value为数据规则map
	private Map ruleMap = null;
	//key 为ename  value为field
	private Map rule = null;
	

	public MyT(ActiveMQConnectionFactory cf,IDataImportService dataImportService,Map ruleMap, IIndexService indexService) {
		super();
		this.cf = cf;
		this.dataImportService = dataImportService;
		this.ruleMap = ruleMap;
		this.indexService = indexService;
	}

	public void run() {
		Connection connection = null;
		final Session session;
		try {
			connection = cf.createConnection();
			connection.start();

			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("data-queue");

			ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) session
					.createConsumer(destination);

			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					try {
						TextMessage txtMsg = (TextMessage) msg;
						System.out.println(txtMsg.getText());
						
						Map <String,String> jsonMap=JSON.parseObject(txtMsg.getText(),new TypeReference<Map<String,String>>(){});
						
						
						//规则ID 获取规则数据
						String ruleId = jsonMap.get("taskid")+"-"+jsonMap.get("ruleid");
						String url = jsonMap.get("url");
						String classify = jsonMap.get("classify");
						
						//判断重复
						boolean isRepeat = indexService.isExists(url, ruleId);
						if(!isRepeat){
							if(ruleMap.get(ruleId) == null){
								rule = dataImportService.getRuleByID(ruleId);
								ruleMap.put(ruleId, rule);
							}else{
								rule = (Map)ruleMap.get(ruleId);
							}
							
							String uuid = jsonMap.get("id");
							//封装数据put
							Put put = new Put(Bytes.toBytes(ruleId+"-"+ uuid));
							
							Set set = rule.keySet();
							Iterator it = set.iterator();
							while(it.hasNext()){
								String key =(String)it.next();
								String value =(String) rule.get(key);
								
								String value1 = jsonMap.get(key);
								if(value1==null){
									continue;
								}
								put.add(Bytes.toBytes("c1"), Bytes.toBytes(value), Bytes.toBytes(value1));
							}
							
							if(StringUtils.isNotEmpty(classify)){
								put.add(Bytes.toBytes("c1"), Bytes.toBytes("classify"), Bytes.toBytes(classify));
							}
							
							put.add(Bytes.toBytes("c1"), Bytes.toBytes("url"), Bytes.toBytes(url));
							put.add(Bytes.toBytes("c1"), Bytes.toBytes("inserttime"), Bytes.toBytes(jsonMap.get("inserttime")));
							
							//数据导入到hbase
							dataImportService.saveData(put);
							indexService.saveIndex(url, ruleId, uuid);
						}
						session.commit();

					} catch (Exception err) {
						err.printStackTrace();
					}
				}
			});

		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}

class MyFileHandler implements Runnable{

	private ActiveMQConnectionFactory cf = null;
	private IFileUploadService fileUploadService;
	
	public MyFileHandler(ActiveMQConnectionFactory cf, IFileUploadService fileUploadService){
		this.cf = cf;
		this.fileUploadService = fileUploadService;
	}
	
	@Override
	public void run() {
		Connection conn = null;
		final Session session;
		try {
			conn = cf.createConnection();
			conn.start();
			session = conn.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("file-queue");
			ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer)session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message msg) {
					TextMessage txtMsg = (TextMessage) msg;
					try {
						System.out.println(txtMsg.getText());
						Map<String, String> jsonMap = JSON.parseObject(txtMsg.getText(), new TypeReference<Map<String,String>>(){});
						String dataid = jsonMap.get("dataid");
						String attachment = jsonMap.get("attachment");
						Put put = new Put(Bytes.toBytes(dataid));
						put.add(Bytes.toBytes("c1"), Bytes.toBytes("attachment"), Bytes.toBytes(attachment));
						fileUploadService.save(put);
						session.commit();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}

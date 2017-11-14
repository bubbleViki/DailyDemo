package com.meritit.cidp.etl.job;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.meritit.dataimport.service.ISendRecordService;
import com.meritit.util.WebserviceUtil;

/**
 * 招投标网站数据抽取到数据管控平台的ETL类
 * @author Lenovo
 *
 */
public class ZtjJob extends com.meritit.mup.foundation.schedule.service.impl.BaseJob {

	private static final String CONFIG = "FieldMap.xml";
	
	private static Map<String, List<Map<String, Object>>> configMap = new HashMap<>();
	
	//hbase数据库操作类
	private ISendRecordService sendRecordService;
		
	//key 为ruleid  value为数据规则map
	private Map ruleMap =  new HashMap();
	//key 为ename  value为field
	private Map rule = null;
	
	private static Properties companyProperty = null;
	
	static{
		
		if(configMap == null || configMap.size() == 0){
			parseFieldMapXML();
		}
		
		if(companyProperty == null){
			companyProperty = new Properties();
		}
		InputStream is1 = ZtjJob.class.getClassLoader().getResourceAsStream("company-province.properties");
		try {
			companyProperty.load(is1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String executeJob(Map jobmap, Map trigmap) throws Exception {
		Map<String, String> zbqy = new HashMap<String, String>();
		for(Object o : companyProperty.keySet()){
			String key = (String)o;
			zbqy.put(key, companyProperty.getProperty(key));
		}

		/**
		 * 国网招投标网
		 * 第一步获取要传输到数据管控平台的数据
		 */
		
		sendRecordService = (ISendRecordService) jobmap.get("sendRecordService");
		
		for(String taskid : configMap.keySet()){
			List<Map> resultList = new ArrayList();
			String id = "001";
			//获取数据导入的最大时间防止数据重复导入
			Map maxtime = sendRecordService.querySendRecord(taskid);
			String recordtime;
			if(maxtime!=null&&maxtime.size()>0){
				recordtime = (String) maxtime.get(taskid);
			}else{
				recordtime = "0";
			}
			
			//获取001的数据规则
			if(ruleMap.get(taskid + "-" +"001") == null){
				rule = sendRecordService.getRuleByID(taskid, "001");
				ruleMap.put(taskid + "-" +"001", rule);
			}else{
				rule = (Map)ruleMap.get(taskid + "-" +"001");
			}
			
			Map<String,Map<String,String>> dataMap1 = sendRecordService.getPartsByTime(taskid, "001", recordtime,rule);
			rule.clear();

			//获取002的数据规则
			if(ruleMap.get(taskid + "-" +"002") == null){
				rule = sendRecordService.getRuleByID(taskid, "002");
				ruleMap.put(taskid + "-" +"002", rule);
			}else{
				rule = (Map)ruleMap.get(taskid + "-" +"002");
			}
			Map<String,Map<String,String>> dataMap2 = sendRecordService.getPartsByTime(taskid, "002", recordtime,rule);

			String maxInsertTime = null;
			

			//通过配置文件设置对应字段
			List<Map<String, Object>> rList = configMap.get(taskid);
			
			/*
			 * 第二步数据和主数据平台的字典进行转换，导入数据到数据管控平台
			 */
			Set<String> set = dataMap1.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()){
				Map<String, String> jsonMap = new HashMap<String, String>();
				String url = it.next();
				Map<String,String> t1 = dataMap1.get(url);
				Map<String,String> t2 = dataMap2.get(url);
				
				if(t1 == null || t2 == null){
					continue;
				}
				
				String inserttime = t1.get("inserttime");
				if(maxInsertTime == null){
					maxInsertTime = inserttime;
				}else{
					if(Long.valueOf(maxInsertTime) < Long.valueOf(inserttime)){
						maxInsertTime = inserttime;
					}
				}
				
				
				for(Map<String, Object> map : rList){
					String field = (String)map.get("field");
					String type = (String)map.get("type");
					if(map.get("mark") != null){
						
						List<String> _dList = (List<String>) map.get("data");
						
						if(_dList == null || _dList.size() == 0){
							throw new NullPointerException("[" + field + "]标签DATA属性不能为空！");
						}
						
						if(map.get("split") != null){
							String s = (String)map.get("split");
							jsonMap.put(field, obtainField(t2.get(type), _dList.get(0).split(s)));
						}else{
							jsonMap.put(field, obtainField(t2.get(type), _dList.get(0)));
						}
					}else{
						
						if(map.get("data") != null){
							List<String> _dList = (List<String>) map.get("data");
							
							if(_dList == null || _dList.size() == 0){
								throw new NullPointerException("[" + field + "]标签DATA属性不能为空！");
							}
							
							Map<String, String> _dMap = new HashMap<>();
							for(String d : _dList){
								String[] rows = d.split(";");
								if(rows.length < 2){
									throw new RuntimeException("[" + map.get("field") + "]元素DATA属性格式不正确！");
								}
								_dMap.put(rows[0].trim(), rows[1].trim());
							}
							
							if(StringUtils.isBlank(type)){
								jsonMap.put(field, "");
							}else{
								if(!setF(jsonMap, t1, field, type, _dMap)){
									if(!setF(jsonMap, t2, field, type, _dMap)){
										jsonMap.put(field, "");
									}
								}
							}
							
						}else{
							if(StringUtils.isBlank(type)){
								jsonMap.put(field, "");
							}else{
								if(!setF(jsonMap, t1, field, type, null)){
									if(!setF(jsonMap, t2, field, type, null)){
										jsonMap.put(field, "");
									}
								}
							}
						}
					}
				}
				
				resultList.add(jsonMap);
			}
			
			try{
				String json = JSON.toJSONString(resultList, SerializerFeature.DisableCircularReferenceDetect);
				json = "{'DATASPIDER01':"+json+"}";
				json = json.replaceAll("\"", "'");
				WebserviceUtil.importDataForPt(json);
				
				//记录数据导入的最大日期，防止数据重复导入
				sendRecordService.save(taskid, maxInsertTime);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return super.executeJob(jobmap, trigmap);
	}
	
	/**
	 * 根据字段内容从详情页面中提取字段
	 * @param content
	 * @param names
	 * @return
	 */
	private String obtainField(String content, String... names){
		Document doc = Jsoup.parse(content);
		Element ele = doc.select(".xx").get(0);
		String html = ele.html();
		String[] lines = html.split("<br>|<br/>|<br />");
		for(String line : lines){
			for(String name : names){
				if(line.contains(name)){
					String field = line.replace(name, "").replaceAll("[\\s:：]", "");
					return field;
				}
			}
		}
		return "";
	}
	
	/**
	 * 解析xml字段映射文件
	 */
	private static void parseFieldMapXML(){
		String root = ZtjJob.class.getClassLoader().getResource("").getPath();
		try {
			
			if(root.startsWith("/")){
				root = root.substring(1);
			}
			
			SAXReader reader = new SAXReader();
			
			org.dom4j.Document doc = reader.read(new File(root + CONFIG));
			org.dom4j.Element element = doc.getRootElement();
			List<org.dom4j.Element> eleList = element.elements();
			
			for(org.dom4j.Element ele : eleList){
				
				List<Map<String, Object>> rList = new ArrayList<>();
				
				String id = ele.attributeValue("ID");
				List<org.dom4j.Element> list = ele.elements();
				for(org.dom4j.Element e : list){
					Map<String, Object> map = new HashMap<>();
					map.put("field", e.getName());
					map.put("type", e.getText());
					//处理该字段时传入的参数
					if(e.attributeValue("MARK") != null){
						map.put("mark", e.attributeValue("MARK"));
					}
					
					//对应信息
					if(e.attributeValue("DATA") != null){
						String data = e.attributeValue("DATA");
						List<String> _dList = new ArrayList<>();
						String[] lines = data.split("\\$\\$");
						for(String line : lines){
							_dList.add(line.trim());
						}
						map.put("data", _dList);
					}
					
					//用户分割的属性
					if(e.attributeValue("SPLIT") != null){
						map.put("split", e.attributeValue("SPLIT"));
					}
					rList.add(map);
				}
				configMap.put(id, rList);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 通过对应关系设置字段值
	 * @param jsonMap
	 * @param t
	 * @param field
	 * @param type
	 * @param dMap
	 * @return
	 */
	private boolean setF(Map<String, String> jsonMap, Map<String, String> t, String field, String type, Map<String, String> dMap){
		
		if(t.get(type) == null){
			return false;
		}
		
		if(dMap != null){
			for(String key : dMap.keySet()){
				String value = dMap.get(key);
				String[] lines = value.split(",");
				for(String line : lines){
					if(line.equals(t.get(type))){
						jsonMap.put(field, key);
						return true;
					}
				}
			}
		}else{
			jsonMap.put(field, t.get(type));
		}
		
		return true;
	}
}

package com.meritit.dataimport.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.meritit.dataimport.service.ISendRecordService;
import com.meritit.db.HbaseDAO;
import com.meritit.taskmgr.dao.ITaskMgrDao;
import com.meritit.taskmgr.model.FieldTypeMap;

@Service("data.sendRecordService")
public class SendRecordServiceImpl implements ISendRecordService {

	@Qualifier("db.hbaseDAOImp")
	@Autowired
	private HbaseDAO hbaseDAO;

	@Resource(name="taskmgr.taskMgrDao")
	private ITaskMgrDao taskMgrDao;
	
	@Resource(name="phoenixDataSource")
	private DruidDataSource dataSource;
	
	private static final String SPIDER_SEND_RECORD = "spider_send_record";
	
	@Override
	public void save(String taskid, String recordtime) throws Exception {
		Put put = new Put(Bytes.toBytes(taskid));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("recordtime"), Bytes.toBytes(recordtime));
		hbaseDAO.save(put, SPIDER_SEND_RECORD);
	}

	@Override
	public Map<String, String> querySendRecord(String taskid) throws Exception{
		Result result = hbaseDAO.getResultByRowKey(SPIDER_SEND_RECORD, taskid);
		Map<String, String> map = new HashMap<>();
		String recordtime = Bytes.toString(result.getValue(Bytes.toBytes("c1"), Bytes.toBytes("recordtime")));
		if(recordtime!=null){
			map.put(taskid, recordtime);
		}
		
		return map;
	}
	
	
	@Override
	public Map<String, Map<String, String>> getPartsByTime(String taskid, String ruleid,
			String recordtime, Map<String, String> ruleMap) {
		Map<String, Map<String, String>> resultMap = new HashMap<>();
		Connection conn = null;
		Statement smt = null;
		try {
			conn = dataSource.getConnection();
			String sql = "select * from \"spider_data\" where \"inserttime\" > '" + recordtime + "' and \"ROW\" like '"
					+ taskid + "-" + ruleid + "%'";
			smt = conn.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			List<FieldTypeMap> fieldList = taskMgrDao.selectRuleByTaskid(taskid+"-"+ruleid, false);
			while(rs.next()){
				Map<String, String> map = new HashMap<String, String>();
				for(FieldTypeMap field : fieldList){
					String key = ruleMap.get(field.getField());
					String value = rs.getString(field.getField());
					map.put(key, value);
				}
				map.put("inserttime", rs.getString("inserttime"));
				resultMap.put(rs.getString("url"), map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(smt != null){
				try {
					smt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return resultMap;
	}
	
	@Override
	public Map getRuleByID(String taskid, String ruleid) {
		Map map = null;
		try {
			map = new HashMap();
			List<Result> results = hbaseDAO.getResultByFilter("spider_datarule",taskid+"-"+ruleid+"-*");
			for(Result result:results){
				String field = Bytes.toString(result.getValue(Bytes.toBytes("c1"), Bytes.toBytes("field")));
				String ename = Bytes.toString(result.getValue(Bytes.toBytes("c1"), Bytes.toBytes("Ename")));
				map.put(field, ename);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}

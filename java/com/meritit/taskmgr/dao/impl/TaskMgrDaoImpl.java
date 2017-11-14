package com.meritit.taskmgr.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;
import com.meritit.db.HbaseDAO;
import com.meritit.taskmgr.dao.ITaskMgrDao;
import com.meritit.taskmgr.model.FieldTypeMap;

@Repository("taskmgr.taskMgrDao")
public class TaskMgrDaoImpl implements ITaskMgrDao{

	@Resource(name="phoenixDataSource")
	private DruidDataSource dataSource;
	
	@Override
	public void deleteDataruleByTaskid(String taskid) {
		Connection conn = null; 
		Statement smt = null;
		try {
			conn = dataSource.getConnection();
			String sql = "delete from \"spider_datarule\" where \"ROW\" like '" + taskid + "%'";
			smt = conn.createStatement();
			smt.executeUpdate(sql);
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
		deleteRuleByTaskid(taskid);
		deleteRuleRelation(taskid);
	}
	
	@Override
	public void deleteRuleByTaskid(String taskid) {
		Connection conn = null; 
		Statement smt = null;
		try {
			conn = dataSource.getConnection();
			String sql = "delete from \"spider_rule\" where \"PK\" like '" + taskid + "%'";
			smt = conn.createStatement();
			smt.executeUpdate(sql);
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
	}
	
	public Map<String, String> selectRuleByTaskid(String ruleid){
		String sql = "select * from \"spider_datarule\" where \"ROW\" like '" + ruleid + "%'";
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			rs = smt.executeQuery(sql);
			while(rs.next()){
				String cname = rs.getString("Cname");
				String field = rs.getString("field");
				resultMap.put(field, cname);
			}
			resultMap.put("url", "链接");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
	public List<FieldTypeMap> selectRuleByTaskid(String ruleid, boolean withType) {
		String sql = "select * from \"spider_datarule\" where \"ROW\" like '" + ruleid + "%'";
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		List<FieldTypeMap> ruleList = new ArrayList<FieldTypeMap>();
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			rs = smt.executeQuery(sql);
			while(rs.next()){
				String cname = rs.getString("Cname");
				String field = rs.getString("field");
				String type = "";
				if(withType){
					type = rs.getString("Type");
				}
				FieldTypeMap ftm = new FieldTypeMap(field, cname, type);
				ruleList.add(ftm);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return ruleList;
	}
	
	
	public Object selectAssemRuleData(String ruleid, int num, Map<String, String> ruleMap){
		StringBuffer sb = new StringBuffer();
		Map<String, Object> resultMap = new HashMap<>();
		for(String key : ruleMap.keySet()){
			sb.append("\"").append(key).append("\"").append(",");
		}
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		resultList.add(ruleMap);
		String sql = "select " + sb.substring(0, sb.length() - 1) + " from \"spider_data\" where \"ROW\" like '" + ruleid +"%' " +
				"order by \"inserttime\" desc";
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			rs = smt.executeQuery(sql);
			int count = 1;
			while(rs.next()){
				if(count > num){
					break;
				}
				Map<String, String> map = new LinkedHashMap<String, String>();
				for(String key : ruleMap.keySet()){
					map.put(key, rs.getString(key));
				}
				resultList.add(map);
				count++;
			}
			if(num >= count){
				resultMap.put("state", "all");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		
		Map<String, String> fieldLenMap = queryFieldLenById(ruleid);
		resultMap.put("fieldLenMap", fieldLenMap);
		resultMap.put("result", resultList);
		return resultMap;
	}


	@Override
	public Map<String, String> queryFieldLenById(String ruleid) {
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "select * from \"spider_datarule\" where \"ROW\" like '" + ruleid + "%'";
			rs = smt.executeQuery(sql);
			while(rs.next()){
				map.put(rs.getString("field"), rs.getString("Len"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return map;
	}
	
	public Map<String, List<FieldTypeMap>> queryDetailByUrl(String url, String ruleid, List<FieldTypeMap> ruleList){
		Map<String, List<FieldTypeMap>> resultMap = new HashMap<>();
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "select * from \"spider_data\" where \"url\" = '"+ url +"' and \"ROW\" like '"+ ruleid + "%'";
			rs = smt.executeQuery(sql);
			List<FieldTypeMap> list = new ArrayList<>();
			while(rs.next()){
				for(FieldTypeMap ftm : ruleList){
					String field = ftm.getName();
					String name = rs.getString(ftm.getField()) == null ? "" : rs.getString(ftm.getField());
					String type = ftm.getType();
					if(type.equals("hyperlink")){
						name = rs.getString("ROW");
					}
					FieldTypeMap ft = new FieldTypeMap(field, name, type);
					list.add(ft);
				}
			}
			//将labelname存入map
			String labelname = queryRuleLable(ruleid);
			resultMap.put(labelname, list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
	
	public String queryRuleLable(String ruleid){
		Connection conn = null;
		String labelname = null;
		Statement smt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "select * from \"spider_rule\" where \"PK\" = '" + ruleid+ "'";
			rs = smt.executeQuery(sql);
			while(rs.next()){
				labelname = rs.getString("labelname");
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return labelname;
	}

	@Override
	public List<Map<String, String>> queryRuleLabelByTaskid(String taskid) {
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "select * from \"spider_rule\" where \"PK\" like '"+ taskid +"%'";
			rs = smt.executeQuery(sql);
			while(rs.next()){
				Map<String, String> map = new HashMap<String, String>();
				map.put("pk", rs.getString("PK"));
				map.put("labelname", rs.getString("labelname"));
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return list;
	}

	@Override
	public void deleteRuleRelation(String taskid) {
		Connection conn = null;
		Statement smt = null;
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "delete from \"spider_rule_relation\" where \"PK\" = '" + taskid + "'";
			smt.executeUpdate(sql);
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
	}
	
	public Map<String, String> queryRuleRelationByTaskid(String taskid){
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "select * from \"spider_rule_relation\" where \"PK\" = '" + taskid + "'";
			rs = smt.executeQuery(sql);
			while(rs.next()){
				map.put("first", rs.getString("first"));
				map.put("last", rs.getString("last"));
				map.put("relation", rs.getString("relation"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return map;
	}

	@Override
	public String selectRowkey(String ruleid, String url) {
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "select * from \"spider_data\" where \"url\" = '"+ url +"' and \"ROW\" like '"+ ruleid + "%'";
			rs = smt.executeQuery(sql);
			while(rs.next()){
				return rs.getString("ROW");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
		return null;
	}
}

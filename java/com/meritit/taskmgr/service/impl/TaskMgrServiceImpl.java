package com.meritit.taskmgr.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.meritit.db.HbaseDAO;
import com.meritit.taskmgr.dao.ITaskMgrDao;
import com.meritit.taskmgr.model.FieldTypeMap;
import com.meritit.taskmgr.model.PageResult;
import com.meritit.taskmgr.model.Rule;
import com.meritit.taskmgr.model.RuleSet;
import com.meritit.taskmgr.model.TaskInfo;
import com.meritit.taskmgr.service.ITaskMgrService;

/**
 * 创建phoenix映射
 * 
 * 任务定义表
 * create 'spider_task','c1'
 * create table "spider_task"("PK" varchar primary key,"c1"."taskname" varchar,"c1"."taskdesp" varchar,"c1"."crawlerlayer" varchar,"c1"."taskurl" varchar,"c1"."choiseway" varchar,"c1"."createdate" varchar,"c1"."keyword_filter" varchar)
 * 
 * 数据规则表
 * create 'spider_datarule','c1'
 * create table "spider_datarule"("ROW" varchar primary key,"c1"."dataitem" varchar,"c1"."field" varchar,"c1"."Ename" varchar,"c1"."Cname" varchar,"c1"."Len" varchar,"c1"."Type" varchar)
 * 
 * 规则表
 * create 'spider_rule','c1'
 * create table "spider_rule"("PK" varchar primary key,"c1"."labelname" varchar)
 * 
 * 规则关系表
 * create 'spider_rule_relation','c1'
 * create table "spider_rule_relation"("PK" varchar primary key,"c1"."first" varchar,"c1"."last" varchar,"c1"."relation" varchar)
 *  
 * 数据存储表
 * create 'spider_data','c1'
 * create table "spider_data"("ROW" varchar primary key,"c1"."field1" varchar,"c1"."field2" varchar,"c1"."field3" varchar,"c1"."field4" varchar,"c1"."field5" varchar,"c1"."field6" varchar,"c1"."field7" varchar,"c1"."field8" varchar,"c1"."field9" varchar,"c1"."field10" varchar,"c1"."field11" varchar,"c1"."url" varchar,"c1"."inserttime" varchar)
 *  
 * @author Administrator
 *
 */
@Service("taskmgr.taskMgrService")
public class TaskMgrServiceImpl implements ITaskMgrService {

	@Resource(name="phoenixDataSource")
	private DruidDataSource dataSource;
	
	@Resource(name="db.hbaseDAOImp")
	private HbaseDAO hbaseDao;
	
	@Resource(name="taskmgr.taskMgrDao")
	private ITaskMgrDao taskMgrDao;
	
	@Override
	public boolean insertTaskInfo(TaskInfo taskInfo, String tablename)throws Exception{
		Put put = new Put(Bytes.toBytes(taskInfo.getId()));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("taskname"), Bytes.toBytes(taskInfo.getTaskname()));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("taskdesp"), Bytes.toBytes(taskInfo.getTaskdesp()));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("keyword_filter"), Bytes.toBytes(taskInfo.getKeyword_filter()));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("crawlerlayer"), Bytes.toBytes(taskInfo.getCrawlerlayer()));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("choiseway"), Bytes.toBytes(taskInfo.getChoiseway()));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("createdate"), Bytes.toBytes(taskInfo.getCreatedate()));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("taskurl"), Bytes.toBytes(taskInfo.getTaskurl()));
		return hbaseDao.save(put, tablename);
	}

	@Override
	public List<String> insertTaskRules(String taskRules)throws Exception {
		List<RuleSet> rsList = JSON.parseArray(taskRules, RuleSet.class);
		
		if(rsList != null && rsList.size() > 0){
			String taskid = rsList.get(0).getRuleid().split("-")[0];
			//先删除taskid对应的规则数据
			taskMgrDao.deleteDataruleByTaskid(taskid);
		}
		
		List<Put> putList = new ArrayList<Put>();
		List<Put> labelPutList = new ArrayList<Put>();
		DecimalFormat sf = new DecimalFormat("00");
		
		List<String> resultList = new ArrayList<String>();
		
		for(RuleSet rs : rsList){
			List<Rule> list = rs.getRuleList();
			String ruleid = rs.getRuleid();
			if(ruleid!= null && !"".equals(ruleid) && list != null && list.size() > 0){
				Put p = new Put(Bytes.toBytes(ruleid));
				p.add(Bytes.toBytes("c1"), Bytes.toBytes("labelname"), Bytes.toBytes(list.get(0).getLabel_name()));
				labelPutList.add(p);
				resultList.add(ruleid);
			}
			for(int i = 0; i < list.size(); i++){
				Put put = new Put(Bytes.toBytes(ruleid + "-" + (sf.format(i+1))));
				put.add(Bytes.toBytes("c1"), Bytes.toBytes("field"), Bytes.toBytes("field" + (i+1)));
				put.add(Bytes.toBytes("c1"), Bytes.toBytes("Cname"), Bytes.toBytes(list.get(i).getZh_name()));
				put.add(Bytes.toBytes("c1"), Bytes.toBytes("Ename"), Bytes.toBytes(list.get(i).getEn_name()));
				put.add(Bytes.toBytes("c1"), Bytes.toBytes("Len"), Bytes.toBytes(list.get(i).getField_len()));
				put.add(Bytes.toBytes("c1"), Bytes.toBytes("Type"), Bytes.toBytes(list.get(i).getField_type()));
				putList.add(put);
			}
		}
		hbaseDao.save(labelPutList, "spider_rule");
		hbaseDao.save(putList, "spider_datarule");
		return resultList;
	}

	@Override
	public PageResult<TaskInfo> getAllResultAndSort(int pageSize, String threshold, String mark) {
//		Connection conn = PhoenixUtils.getConnection();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Statement smt = null;
		ResultSet rs = null;
		PageResult<TaskInfo> page = new PageResult<TaskInfo>();
		try {
			smt = conn.createStatement();
			String count_sql = "select count(*) from \"spider_task\"";
			int total = 0;
			rs = smt.executeQuery(count_sql);
			while(rs.next()){
				total = rs.getInt(1);
			}
			StringBuffer sb = new StringBuffer("select * from \"spider_task\"");
			if(null != threshold && !"".equals(threshold)){
				if("next".equals(mark) || "current".equals(mark)){
					sb.append(" where \"PK\" < '" + threshold + "'");
				}else if("previous".equals(mark)){
					sb.append(" where \"PK\" > '" + threshold + "'");
				}
			}
			sb.append(" order by \"PK\" desc");
			if(!"previous".equals(mark) && !"last".equals(mark)){
				sb.append(" limit " + pageSize);
			}
			
			rs = smt.executeQuery(sb.toString());
			List<TaskInfo> taskList = new ArrayList<TaskInfo>();
			if("previous".equals(mark)){
				taskList = lastResultSetData(rs, pageSize);
			}else if("last".equals(mark)){
				taskList = lastResultSetData(rs, total % pageSize);
			}else{
				taskList = parseResultSetData(rs);
			}
			page.setTotal(total);
			page.setDataList(taskList);
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
		return page;
	}
	
	@Override
	public void deleteTaskByPK(String pk) throws Exception {
		hbaseDao.deleteByRowKey("spider_task", pk);
		//删除对应规则数据
		taskMgrDao.deleteDataruleByTaskid(pk);
	}
	
	private List<TaskInfo> lastResultSetData(ResultSet rs, int size){
		List<TaskInfo> resultList = new ArrayList<TaskInfo>();
		try {
			List<TaskInfo> taskList = new ArrayList<TaskInfo>();
			while(rs.next()){
				TaskInfo task = new TaskInfo();
				task.setId(rs.getString("PK"));
				task.setTaskname(rs.getString("taskname"));
				task.setTaskdesp(rs.getString("taskdesp"));
				if("1".equals(rs.getString("crawlerlayer"))){
					task.setCrawlerlayer("一层");
				}else{
					task.setCrawlerlayer("两层");
				}
				if("bzy".equals(rs.getString("choiseway"))){
					task.setChoiseway("八爪鱼");
				}else{
					task.setChoiseway("自定义开发");
				}
				task.setKeyword_filter(rs.getString("keyword_filter"));
				task.setCreatedate(rs.getString("createdate"));
				task.setTaskurl(rs.getString("taskurl"));
				taskList.add(task);
			}
			if(taskList.size() != 0){
				resultList = taskList.subList(taskList.size() - (size), taskList.size());
				Collections.sort(resultList, Collections.reverseOrder());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	private List<TaskInfo> parseResultSetData(ResultSet rs){
		List<TaskInfo> taskList = new ArrayList<TaskInfo>();
		try {
			while(rs.next()){
				TaskInfo task = new TaskInfo();
				task.setId(rs.getString("PK"));
				task.setTaskname(rs.getString("taskname"));
				task.setTaskdesp(rs.getString("taskdesp"));
				if("1".equals(rs.getString("crawlerlayer"))){
					task.setCrawlerlayer("一层");
				}else{
					task.setCrawlerlayer("两层");
				}
				if("bzy".equals(rs.getString("choiseway"))){
					task.setChoiseway("八爪鱼");
				}else{
					task.setChoiseway("自定义开发");
				}
				task.setKeyword_filter(rs.getString("keyword_filter"));
				task.setCreatedate(rs.getString("createdate"));
				task.setTaskurl(rs.getString("taskurl"));
				taskList.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return taskList;
	}

	@Override
	public List<RuleSet> getAllTaskRule(String dataruleid) {
		List<RuleSet> resultList = new ArrayList<RuleSet>();
//		Connection conn = PhoenixUtils.getConnection();
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			smt = conn.createStatement();
//			String sql = "select * from \"spider_datarule\" where \"ROW\" like '" + dataruleid + "%'";
			String sql = "select a.*, b.\"labelname\" as \"labelname\" from \"spider_datarule\" a, \"spider_rule\" b where " +
					"substr(a.\"ROW\", 0, 17) = b.\"PK\" and \"ROW\" like '" + dataruleid + "%'";
			rs = smt.executeQuery(sql);
			Map<String, List<Rule>> ruleMap = new HashMap<String, List<Rule>>();
			while(rs.next()){
				String id = rs.getString("ROW");
				String ruleid = id.substring(0, id.lastIndexOf("-"));
				if(!ruleMap.containsKey(ruleid)){
					ruleMap.put(ruleid, new ArrayList<Rule>());
				}
				Rule rule = new Rule();
				String rowid = id.substring(id.lastIndexOf("-") + 1, id.length());
				String zh_name = rs.getString("Cname");
				String en_name = rs.getString("Ename");
				String field_len = rs.getString("Len");
				String field_type = rs.getString("Type");
				String field = rs.getString("field");
				String label_name = rs.getString("labelname");
				rule.setId(rowid);
				rule.setZh_name(zh_name);
				rule.setEn_name(en_name);
				rule.setField_len(field_len);
				rule.setField_type(field_type);
				rule.setField(field);
				rule.setLabel_name(label_name);
				ruleMap.get(ruleid).add(rule);
			}
			for(String ruleid : ruleMap.keySet()){
				RuleSet ruleSet = new RuleSet();
				ruleSet.setRuleid(ruleid);
				List<Rule> ruleList = ruleMap.get(ruleid);
				Collections.sort(ruleList);
				ruleSet.setRuleList(ruleList);
				resultList.add(ruleSet);
			}
			Collections.sort(resultList);
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
		return resultList;
	}
	
	public Object queryAllDataByTaskid(String ruleid, int num){
		Map<String, String> ruleMap = taskMgrDao.selectRuleByTaskid(ruleid);
		Object resultList = taskMgrDao.selectAssemRuleData(ruleid, num, ruleMap);
		return resultList;
	}

	@Override
	public Map<String, List<FieldTypeMap>> queryDetailByUrl(String url, String ruleid) {
		List<FieldTypeMap> ruleList = taskMgrDao.selectRuleByTaskid(ruleid, true);
		Map<String, List<FieldTypeMap>> resultMap = taskMgrDao.queryDetailByUrl(url, ruleid, ruleList);
		return resultMap;
	}

	@Override
	public List<Map<String, String>> queryRuleLabelByTaskid(String taskid) {
		List<Map<String, String>> ruleLabelList = taskMgrDao.queryRuleLabelByTaskid(taskid);
		return ruleLabelList;
	}

	@Override
	public void saveRuleRelation(String taskid, String first, String last, String relation) throws Exception {
		Put put = new Put(Bytes.toBytes(taskid));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("first"), Bytes.toBytes(first));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("last"), Bytes.toBytes(last));
		put.add(Bytes.toBytes("c1"), Bytes.toBytes("relation"), Bytes.toBytes(relation));
		hbaseDao.save(put, "spider_rule_relation");
	}

	@Override
	public Map<String, String> queryRuleRelationByTaskid(String taskid) {
		Map<String, String> map = taskMgrDao.queryRuleRelationByTaskid(taskid);
		return map;
	}

	@Override
	public String selectRowkey(String ruleid, String url) {
		return taskMgrDao.selectRowkey(ruleid, url);
	}
}

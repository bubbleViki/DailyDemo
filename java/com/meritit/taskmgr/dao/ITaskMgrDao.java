package com.meritit.taskmgr.dao;

import java.util.List;
import java.util.Map;

import com.meritit.taskmgr.model.FieldTypeMap;

public interface ITaskMgrDao {
	public void deleteDataruleByTaskid(String taskid);
	
	public void deleteRuleByTaskid(String taskid);
	
	public void deleteRuleRelation(String taskid);
	
	public Map<String, String> selectRuleByTaskid(String ruleid);
	
	public Object selectAssemRuleData(String ruleid, int num, Map<String, String> ruleMap);

	public Map<String, String> queryFieldLenById(String ruleid);
	
	public Map<String, List<FieldTypeMap>> queryDetailByUrl(String url, String ruleid, List<FieldTypeMap> ruleList);

	public List<Map<String, String>> queryRuleLabelByTaskid(String taskid);
	
	public Map<String, String> queryRuleRelationByTaskid(String taskid);

	public List<FieldTypeMap> selectRuleByTaskid(String ruleid, boolean withType);
	
	public String selectRowkey(String ruleid, String url);
}

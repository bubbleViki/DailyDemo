package com.meritit.taskmgr.service;

import java.util.List;
import java.util.Map;

import com.meritit.taskmgr.model.FieldTypeMap;
import com.meritit.taskmgr.model.PageResult;
import com.meritit.taskmgr.model.RuleSet;
import com.meritit.taskmgr.model.TaskInfo;

public interface ITaskMgrService {

	/**
	 * 将TaskInfo对象存入hbase对应表中
	 * @param taskInfo 对象
	 * @param tablename 表名
	 * @return
	 */
	public boolean insertTaskInfo(TaskInfo taskInfo, String tablename)throws Exception;
	
	/**
	 * 将规则插入hbase对应表中
	 * @param taskRules json格式数据
	 */
	public List<String> insertTaskRules(String taskRules)throws Exception;
	
	/**
	 * 根据条件查询数据
	 * @param pageSize 页面中的数据量
	 * @param threshold 阈值
	 * @return 
	 */
	public PageResult<TaskInfo> getAllResultAndSort(int pageSize, String threshold, String mark);
	
	/**
	 * 根据主键删除任务数据
	 * @param pk
	 * @throws Exception
	 */
	public void deleteTaskByPK(String pk)throws Exception;
	
	/**
	 * 根据任务id查询对应规则
	 * @return
	 */
	public List<RuleSet> getAllTaskRule(String dataruleid);
	
	
	/**
	 * 根据任务id查询对应数据基本信息
	 * @param taskid
	 * @return
	 */
	public Object queryAllDataByTaskid(String ruleid, int num);
	
	/**
	 * 根据url和ID查询该条url的详细信息，并以map返回
	 * @param url
	 * @param ruleid
	 * @param ruleMap
	 * @return
	 */
	public Map<String, List<FieldTypeMap>> queryDetailByUrl(String url, String ruleid);
	
	/**
	 * 根据任务id查询规则名称
	 * @param taskid
	 * @return
	 */
	public List<Map<String, String>> queryRuleLabelByTaskid(String taskid);

	/**
	 * 保存规则关系
	 * @param first
	 * @param last
	 * @param relation
	 */
	public void saveRuleRelation(String taskid, String first, String last, String relation) throws Exception;
	
	/**
	 * 根据taskid查询规则关系
	 * @param taskid
	 * @return
	 */
	public Map<String, String> queryRuleRelationByTaskid(String taskid);
	
	/**
	 * 根据id和url查询数据id
	 * @param ruleid
	 * @param url
	 * @return
	 */
	public String selectRowkey(String ruleid, String url);

}

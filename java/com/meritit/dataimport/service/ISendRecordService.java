package com.meritit.dataimport.service;

import java.util.Map;


public interface ISendRecordService {

	/**
	 * 将数据存数数据记录表
	 * @param taskid
	 * 				任务id
	 * @param ruleid
	 * 				规则id
	 * @param recordtime
	 * 				毫秒值
	 * @throws Exception
	 */
	public void save(String taskid, String recordtime) throws Exception;
	
	/**
	 * 查询数据记录表(spider_send_record)
	 * @param taskid
	 * 				任务id
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> querySendRecord(String taskid) throws Exception;
	
	
	/**
	 * 根据记录时间分段查询数据（spider_data）
	 * @param taskid
	 * 				任务id
	 * @param ruleid
	 * 				规则id
	 * @param recordtime
	 * 				毫秒值
	 * @return
	 */
	public Map<String, Map<String, String>> getPartsByTime(String taskid, String ruleid, String recordtime, Map<String, String> ruleMap);
	
	/**
	 * 根据任务id, 规则id查询相应规则映射
	 * @param taskid
	 * @param ruleid
	 * @return
	 */
	public Map getRuleByID(String taskid, String ruleid);
	
}

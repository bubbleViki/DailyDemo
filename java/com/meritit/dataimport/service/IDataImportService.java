package com.meritit.dataimport.service;

import java.util.Map;

import org.apache.hadoop.hbase.client.Put;

public interface IDataImportService {

	/**
	 * 通过ruleid获取数据规则
	 * @return
	 */
	public Map getRuleByID(String ruleId);
	
	/**
	 * 保存列表数据
	 * @param puts
	 */
	public void saveData(Put put);
	
	public void closeTable();
	
	
}

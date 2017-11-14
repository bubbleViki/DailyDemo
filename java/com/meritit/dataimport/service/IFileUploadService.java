package com.meritit.dataimport.service;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;

import com.meritit.taskmgr.model.FileBean;

public interface IFileUploadService {

	void save(Put put);
	
	Map<String, String> searchFileById(String rowKey);
	
	Map<String, List<FileBean>> findFilesById(String id);
}

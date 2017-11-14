package com.meritit.dataimport.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.meritit.dataimport.service.IFileUploadService;
import com.meritit.db.HbaseDAO;
import com.meritit.taskmgr.model.FileBean;

@Service("file.fileUploadService")
public class FileUploadServiceImpl implements IFileUploadService {

	private static final String FILE_UPLOAD = "spider_file_upload"; 
	
	@Qualifier("db.hbaseDAOImp")
	@Autowired
	private HbaseDAO hbaseDao;
	
	@Override
	public void save(Put put) {
		try {
			hbaseDao.save(put, FILE_UPLOAD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> searchFileById(String rowKey) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			Result result = hbaseDao.getResultByRowKey(FILE_UPLOAD, rowKey);
			String fileid = Bytes.toString(result.getValue(Bytes.toBytes("c1"), Bytes.toBytes("fileid")));
			String filename = Bytes.toString(result.getValue(Bytes.toBytes("c1"), Bytes.toBytes("filename")));
			map.put("fileid", fileid);
			map.put("filename", filename);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, List<FileBean>> findFilesById(String id) {
		Map<String, List<FileBean>> resultMap = new HashMap<>();
		try {
			Result r1 = hbaseDao.getResultByRowKey(FILE_UPLOAD, id + "-001");
			String am1 = Bytes.toString(r1.getValue(Bytes.toBytes("c1"), Bytes.toBytes("attachment")));
			List<FileBean> list1 = JSON.parseArray(am1, FileBean.class);
			if(list1 != null && list1.size() > 0){
				resultMap.put("001", list1);
			}
			
			Result r2 = hbaseDao.getResultByRowKey(FILE_UPLOAD, id + "-002");
			String am2 = Bytes.toString(r2.getValue(Bytes.toBytes("c1"), Bytes.toBytes("attachment")));
			List<FileBean> list2 = JSON.parseArray(am2, FileBean.class);
			if(list2 != null && list2.size() > 0){
				resultMap.put("002", list2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}

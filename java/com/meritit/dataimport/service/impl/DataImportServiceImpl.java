package com.meritit.dataimport.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.meritit.dataimport.service.IDataImportService;
import com.meritit.db.HbaseDAO;
import com.meritit.db.HbaseDAOImp;

@Service("data.dataImportServiceImpl")
public class DataImportServiceImpl implements IDataImportService {

	@Qualifier("db.hbaseDAOImp")
	@Autowired
	private HbaseDAO hbaseDAO;
	
	private HTableInterface table = null;
	private static final String SPIDER_DATA = "spider_data";
	
	
	public DataImportServiceImpl(){
		try {
			table = HbaseDAOImp.getConnection().getTable(SPIDER_DATA);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map getRuleByID(String ruleId) {
		Map map = null;
		try {
			map = new HashMap();
			List<Result> results = hbaseDAO.getResultByFilter("spider_datarule",ruleId+"-*");
			for(Result result:results){
				String ename = Bytes.toString(result.getValue(Bytes.toBytes("c1"), Bytes.toBytes("Ename")));
				String field = Bytes.toString(result.getValue(Bytes.toBytes("c1"), Bytes.toBytes("field")));
				map.put(ename, field);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	@Override
	public void saveData(Put put) {
		// TODO 自动生成的方法存根
		try {
			table.put(put);
			table.flushCommits();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	
	@Override
	public void closeTable() {

		if(table != null){
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}

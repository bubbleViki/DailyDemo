package com.meritit.dataimport.service.impl;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

import com.meritit.dataimport.service.IIndexService;
import com.meritit.db.HbaseDAO;
import com.meritit.db.HbaseDAOImp;

@Service("index.indexService")
public class IndexServiceImpl implements IIndexService{

	private HTableInterface table = null;
	private static final String SPIDER_INDEX = "spider_index";
	
	@Resource(name="db.hbaseDAOImp")
	private HbaseDAO hbaseDAO;
	
	public IndexServiceImpl(){
		try {
			table = HbaseDAOImp.getConnection().getTable(SPIDER_INDEX);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveIndex(String url, String ruleid, String uuid) {
		try {
			String encodeUrl = new BASE64Encoder().encode(url.getBytes("utf-8"));
			Put put = new Put(Bytes.toBytes(ruleid + "-" + encodeUrl));
			put.add(Bytes.toBytes("c1"), Bytes.toBytes("uuid"), Bytes.toBytes(uuid));
			table.put(put);
			table.flushCommits();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isExists(String url, String ruleid) {
		try {
			String encodeUrl = new BASE64Encoder().encode(url.getBytes("utf-8"));
			return hbaseDAO.isExists(SPIDER_INDEX, ruleid+"-"+encodeUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	

}

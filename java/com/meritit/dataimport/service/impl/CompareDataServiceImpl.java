package com.meritit.dataimport.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.meritit.dataimport.service.ICompareDataService;
import com.meritit.util.JdbcUtils;

@Service("data.compareDataService")
public class CompareDataServiceImpl implements ICompareDataService{

	@Resource(name="phoenixDataSource")
	private DruidDataSource dataSource;
	
	private final static String ruleId = "1486446072404-001";
	private final static String tenderLabel = "D_ZBDL";
	
	
	private final static String titleLabel = "field2";
	private final static String areaLabel = "field3";
	private final static String pubdateLabel = "field4";
	
	@Override
	public List<Map<String, String>> compare() {
		
		List<Map<String, String>> tenderList = getSpiderData();
		
		Connection conn = JdbcUtils.getConnection();
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<Map<String, String>> resultList = new ArrayList<>();
		try {
			psmt = conn.prepareStatement("select * from A0000000000001 where ltrim(substr(d_zbbt,instr(d_zbbt,']')+1)) like ?||'%'");
			for(Map<String, String> map : tenderList){
				Map<String, String> resultMap = new HashMap<>();
				psmt.setString(1, map.get("title"));
				rs = psmt.executeQuery();
				while(rs.next()){
					resultMap.put("ZBR", rs.getString(tenderLabel));
					resultMap.put("ZBQY", map.get("area"));
					resultMap.put("FBSJ", map.get("pubdate").replace("[", "").replace("]", ""));
					resultMap.put("ZHONGBR", "中标人");
					resultMap.put("ZHONGBGG", map.get("title"));
					resultList.add(resultMap);
				}
				return resultList;
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
			if(psmt != null){
				try {
					psmt.close();
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

	
	private List<Map<String, String>> getSpiderData(){
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		
		List<Map<String, String>> resultList = new ArrayList<>();
		
		try {
			conn = dataSource.getConnection();
			smt = conn.createStatement();
			String sql = "select * from \"spider_data\" where \"ROW\" like '" + ruleId + "%'";
			rs = smt.executeQuery(sql);
			while(rs.next()){
				Map<String, String> map = new HashMap<>();
				String title = rs.getString(titleLabel);
				title = title.replace("“", "").replace("”", "").replace("中标公告", "").replace("谈判结果", "").
						replace("的中标结果公示", "").replace("中标结果公示", "").replace("结果公示", "").replace("中标公示", "")
						.replace("谈判公告", "").replace("成交公示", "").replace("招标采购", "");
				map.put("title", title);
				map.put("area", rs.getString(areaLabel));
				map.put("pubdate", rs.getString(pubdateLabel));
				resultList.add(map);
			}
			return resultList;
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
	
	public static void main(String[] args) {
		new CompareDataServiceImpl().compare();
	}
}

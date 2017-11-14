package com.meritit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PhoenixUtils {

	static Properties prop = null;
	static Connection conn = null;
	
	static{
		prop = PropUtils.getProp("hbaseconfig.properties");
		String url = prop.getProperty("hbase.zookeeper.quorum");
		try {
			Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
			conn = DriverManager.getConnection("jdbc:phoenix:" + url);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		return conn;
	}
	
	public static void main(String[] args) {
		Connection conn = PhoenixUtils.getConnection();
		System.out.println(conn);
	}
}

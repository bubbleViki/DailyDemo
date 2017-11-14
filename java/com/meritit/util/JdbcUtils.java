package com.meritit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

	private static Properties prop = null;
	private static String dirver;
	private static String url;
	private static String username;
	private static String password;
	
	static{
		prop = PropUtils.getProp("jdbc.properties");
		dirver = prop.getProperty("cmp.orcl.driver");
		url = prop.getProperty("cmp.orcl.url");
		username = prop.getProperty("cmp.orcl.username");
		password = prop.getProperty("cmp.orcl.password");
	}
	
	
	public static Connection getConnection(){
		return JdbcHolder.getConn();
	}
	
	
	private static class JdbcHolder{
		private static Connection getConn(){
			try {
				Class.forName(dirver);
				return DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
}

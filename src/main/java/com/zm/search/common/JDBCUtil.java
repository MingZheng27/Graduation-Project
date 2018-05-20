package com.zm.search.common;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;

public class JDBCUtil {

	public static Connection conn = null;
	public static final String url = "jdbc:mysql://localhost:3306/spider";
	public static final String username = "root";
	public static final String password = "123456";


	private JDBCUtil() {

	}

	public static Connection getConn(){
		if (conn == null){
			synchronized (JDBCUtil.class) {
				if (conn == null){
					try {
						Class.forName("com.mysql.jdbc.Driver");
						conn = (Connection) DriverManager.getConnection(url, username, password);
						conn.setEncoding("UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return conn;
	}

}

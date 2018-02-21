package com.mckinsey.ckc.sf.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBCConnection {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//	static final String DB_URL = "jdbc:mysql://182.254.216.232:3306/xboost3";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/xboost2";
	
	static final String DB_URL_CLOUD = "jdbc:mysql://58f045257775e.sh.cdb.myqcloud.com:3635/dynamic_network";
	static Connection conn;

	// Database credentials mck-mck1234567
	static final String USER = "root";
	static final String PASS = "123456";

	public static Connection getConnection() {
		if (null != conn) {
			return conn;
		}
		// STEP 2: Register JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("New Connection to Database");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
		return conn;
	}


}

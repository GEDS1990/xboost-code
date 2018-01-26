package com.mckinsey.ckc.sf.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBCConnection {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/xboost2";
	static Connection conn;

	// Database credentials
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

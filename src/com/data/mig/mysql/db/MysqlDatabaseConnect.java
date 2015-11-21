package com.data.mig.mysql.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDatabaseConnect {
	
   public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	public Connection getMySqlDBConnection() {

		Connection conn = null;
		try {
			
			Class.forName(JDBC_DRIVER);
			//new WebappClassLoader(JDBC_DRIVER, true);
			conn = DriverManager.getConnection("jdbc:mysql://localhost/classicmodels?"
					+ "user=root&password=root");

			// Do something with the Connection

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	
	public Connection getMySqlDBConnection(String schemaName, String userId, String password) {

		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER); 
			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + schemaName + "?"
					+ "user=" + userId + "&password=" +password);

			// Do something with the Connection

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}

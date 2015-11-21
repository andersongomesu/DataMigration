package com.data.mig.mysql.db.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.mysql.db.MysqlDatabaseConnect;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MysqlDatabaseConnectTest {


	@Test
	public void getMysqlDBConnectionTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect ();
		
		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection();
		
		Assert.assertNotNull(conn);
		
		conn.close();
		
		
	}
	
	@Test
	public void getMysqlDBConnectionUsingParametersTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect ();
		
		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection("classicmodels", "root", "root");
		
		Assert.assertNotNull(conn);
		
		conn.close();
		
		
	}

}


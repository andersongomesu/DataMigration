package com.data.mig.oracle.db.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.oracle.db.OracleDatabaseConnect;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OracleDatabaseConnectTest {
	
	@Test
	public void getOracleDBConnectionTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect ();
		
		Connection conn = oracleDatabaseConnect.getOracleDBConnection();
		
		Assert.assertNotNull(conn);
		
		conn.close();
		
		
	}
	
	@Test
	public void getOracleDBConnectionUsingHRTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect ();
		
		Connection conn = oracleDatabaseConnect.getOracleDBConnection("HR", "HR");
		
		Assert.assertNotNull(conn);
		
		conn.close();
		
		
	}
	
	@Test
	public void getOracleDBConnectionUsingScottTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect ();
		
		Connection conn = oracleDatabaseConnect.getOracleDBConnection("SCOTT", "TIGER");
		
		Assert.assertNotNull(conn);
		
		conn.close();
		
		
	}	

}

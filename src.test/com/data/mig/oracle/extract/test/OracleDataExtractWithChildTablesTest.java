package com.data.mig.oracle.extract.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.extract.OracleDataExtractWithChildTables;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OracleDataExtractWithChildTablesTest {
		
	@Test
	public void getOracleDataExtractWithGivenChildTableTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(
				IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleDataExtractWithChildTables oracleDataExtractWithChildTables = new OracleDataExtractWithChildTables();

		/*boolean extractResult = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers","orders", 
				5L, "D:\\Habi\\MS\\Git\\MySQL\\orders_customers.json");
		
		Assert.assertTrue(extractResult);
		
		extractResult = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers","payments", 
				5L, "D:\\Habi\\MS\\Git\\MySQL\\payments_customers.json");
		
		Assert.assertTrue(extractResult);*/
		
		boolean extractResult = oracleDataExtractWithChildTables.extractOracleDataIntoJsonFile(IApplicationConstants.defaultOracleSchemaName, "EMPLOYEES",
		5L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\oracleextract.json");

		Assert.assertTrue(extractResult);
		
		/*boolean extractResult = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers","orders", 
				5L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\orders_customers.json");
		
		Assert.assertTrue(extractResult);
		
		extractResult = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers","payments", 
				5L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\payments_customers.json");
		
		Assert.assertTrue(extractResult);*/

		conn.close();

	}
	
	@Test
	public void getOracleDataExtractWithGivenChildTableTest1() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(
				IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleDataExtractWithChildTables oracleDataExtractWithChildTables = new OracleDataExtractWithChildTables();

		boolean extractResult = oracleDataExtractWithChildTables.extractOracleDataIntoJsonFile(IApplicationConstants.defaultOracleSchemaName, "productlines", 
				5L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\productlines.json");
		
		Assert.assertTrue(extractResult);
		
		

		conn.close();

	}

}

package com.data.mig.mysql.extract.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MysqlDataSubsequentExtractWithChildTablesTest {
	
	@Test
	public void getMysqlDataExtractTest1() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
		
		Map<String, String> primaryKeyValues = new LinkedHashMap<String, String>();
		
		primaryKeyValues.put("customerNumber", "112");

		boolean extractResult = mysqlDataExtractWithChildTables.subsequentExtractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers", 
				10L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\subextract.json", primaryKeyValues);
		
		Assert.assertTrue(extractResult);

		conn.close();

	}
	
	@Test
	public void getMysqlDataExtractTest2() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
		
		Map<String, String> primaryKeyValues = new LinkedHashMap<String, String>();
		
		primaryKeyValues.put("customerNumber", "112");
		primaryKeyValues.put("checkNumber", "BO864823");

		boolean extractResult = mysqlDataExtractWithChildTables.subsequentExtractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "payments", 
				10L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\subextract.json", primaryKeyValues);
		
		Assert.assertTrue(extractResult);

		conn.close();

	}	
	

}

package com.data.mig.mysql.extract.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MysqlDataExtractWithChildTablesTest {
	@Test
	public void getMysqlDataExtractTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();

		boolean extractResult = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers", 
				2L, "D:\\Habi\\MS\\Git\\MySQL\\extract.json");
		
		Assert.assertTrue(extractResult);

		conn.close();

	}
	
	@Test
	public void getMysqlDataExtractWithGivenChildTableTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();

		boolean extractResult = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers","orders", 
				2L, "D:\\Habi\\MS\\Git\\MySQL\\customers_orders.json");
		
		Assert.assertTrue(extractResult);

		conn.close();

	}

}

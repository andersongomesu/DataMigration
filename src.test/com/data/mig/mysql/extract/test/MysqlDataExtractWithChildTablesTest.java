package com.data.mig.mysql.extract.test;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;

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
				10L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\extract.json");
		
		Assert.assertTrue(extractResult);

		conn.close();

	}
	

}

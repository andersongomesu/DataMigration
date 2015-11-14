package com.data.mig.mysql.extract.test;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.extract.MysqlDataExtract;

@SuppressWarnings("deprecation")
public class MysqlDataExtractTest {
	@Test
	public void getMysqlDataExtractTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlDataExtract mysqlDataExtract = new MysqlDataExtract();

		boolean extractResult = mysqlDataExtract.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "customers", 
				10L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\extract.json");
		
		Assert.assertTrue(extractResult);

		conn.close();

	}
	

}

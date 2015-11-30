package com.data.mig.mysql.extract.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.extract.MysqlDataExtract;

import junit.framework.Assert;

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

		boolean extractResult = mysqlDataExtract.extractMysqlDataIntoJsonFile(IApplicationConstants.defaultMySqlSchemaName, "payments", 
				2L, "D:\\Habi\\MS\\Git\\MySQL\\extract.json");
		
		Assert.assertTrue(extractResult);

		conn.close();

	}
	


}

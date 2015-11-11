package com.data.mig.mysql.db.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableColumnDetails;

@SuppressWarnings("deprecation")
public class MysqlTableColumnDetailsTest {
	
	@Test
	public void getMysqlTableRelationshipTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();

		Map<String, String> columnDetailsMap = mysqlTableColumnDetails.getMysqlTableColumnDetails(conn,
				IApplicationConstants.defaultMySqlSchemaName, "customers");
		
		Assert.assertNotNull(columnDetailsMap);

		conn.close();

	}
	
	@Test
	public void getMysqlTableRelationshipTableNotFoundTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();

		Map<String, String> columnDetailsMap = mysqlTableColumnDetails.getMysqlTableColumnDetails(conn,
				IApplicationConstants.defaultMySqlSchemaName, "tablenotfound");
		
		Assert.assertNull(columnDetailsMap);

		conn.close();

	}	

}

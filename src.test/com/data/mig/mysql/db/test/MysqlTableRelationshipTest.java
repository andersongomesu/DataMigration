package com.data.mig.mysql.db.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableRelationship;

@SuppressWarnings("deprecation")
public class MysqlTableRelationshipTest {
	
	@Test
	public void getMysqlTableRelationshipTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();

		Map<String, String> relationShipMap = mysqlTableRelationship.getMysqlTableRelationship(conn,
				IApplicationConstants.defaultMySqlSchemaName, "customers");
		
		Assert.assertNotNull(relationShipMap);

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

		MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();

		Map<String, String> relationShipMap = mysqlTableRelationship.getMysqlTableRelationship(conn,
				IApplicationConstants.defaultMySqlSchemaName, "tablenotfound");
		
		Assert.assertNull(relationShipMap);

		conn.close();

	}	
}

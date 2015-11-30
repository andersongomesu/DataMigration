package com.data.mig.mysql.db.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableRelationship;
import com.data.mig.mysql.db.TableDetails;

import junit.framework.Assert;

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
	
	@Test
	public void getMysqlTableRelationshipAsObjectTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);

		MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();

		List<TableDetails> tableDetailsList = mysqlTableRelationship.getMysqlTableRelationshipDetailsAsObject(conn,
				IApplicationConstants.defaultMySqlSchemaName, "customers");
		
		Assert.assertNotNull(tableDetailsList);
		Assert.assertEquals("List of tables not matching", 2, tableDetailsList.size());
		
		System.out.println("Table Details List : " + tableDetailsList.toString());

		conn.close();

	}	
}

package com.data.mig.oracle.db.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.TableDetails;
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.db.OracleTableRelationship;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OracleTableRelationshipTest {
	
	@Test
	public void getOracleTableRelationshipTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(
				IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleTableRelationship oracleTableRelationship = new OracleTableRelationship();

		Map<String, String> relationShipMap = oracleTableRelationship.getOracleTableRelationship(conn,
				IApplicationConstants.defaultOracleSchemaName, "EMPLOYEES");
		
		Assert.assertNotNull(relationShipMap);

		conn.close();

	}
	
	@Test
	public void getOracleTableRelationshipTableNotFoundTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(
				IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleTableRelationship oracleTableRelationship = new OracleTableRelationship();

		Map<String, String> relationShipMap = oracleTableRelationship.getOracleTableRelationship(conn,
				IApplicationConstants.defaultOracleSchemaName, "tablenotfound");
		
		Assert.assertNull(relationShipMap);

		conn.close();

	}
	
	@Test
	public void getOracleTableRelationshipAsObjectTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(
				IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleTableRelationship oracleTableRelationship = new OracleTableRelationship();

		List<TableDetails> tableDetailsList = oracleTableRelationship.getOracleTableRelationshipDetailsAsObject(conn,
				IApplicationConstants.defaultOracleSchemaName, "DEPARTMENTS");
		
		Assert.assertNotNull(tableDetailsList);
		Assert.assertEquals("List of tables not matching", 2, tableDetailsList.size());
		
		System.out.println("Table Details List : " + tableDetailsList.toString());

		conn.close();

	}	
}

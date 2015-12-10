package com.data.mig.oracle.db.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.db.OracleTableColumnDetails;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OracleTableColumnDetailsTest {

	@Test
	public void getOracleTableRelationshipTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleTableColumnDetails oracleTableColumnDetails = new OracleTableColumnDetails();

		Map<String, String> columnDetailsMap = oracleTableColumnDetails.getOracleTableColumnDetails(conn,
				IApplicationConstants.defaultOracleUserId, "DEPARTMENTS");

		Assert.assertNotNull(columnDetailsMap);

		System.out.println("Oracle column details : " + columnDetailsMap.toString());

		conn.close();

	}

	@Test
	public void getOracleTablePrimaryKeyTest() throws SQLException {

		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleTableColumnDetails oracleTableColumnDetails = new OracleTableColumnDetails();

		Map<String, String> columnDetailsMap = oracleTableColumnDetails.getOracleTablePrimaryKey(conn,
				IApplicationConstants.defaultOracleUserId, "DEPARTMENTS");

		Assert.assertNotNull(columnDetailsMap);
		System.out.println("Primary column details : " + columnDetailsMap.toString());

		conn.close();

	}

}

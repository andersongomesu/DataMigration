package com.data.mig.oracle.db.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.db.OracleSchemaDetails;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OracleSchemaDetailsTest {

	@Test
	public void getOracleTableRelationshipTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleSchemaDetails oracleSchemaDetails = new OracleSchemaDetails();

		List<String> tableNameList = oracleSchemaDetails.getOracleTableDetails(conn, IApplicationConstants.defaultOracleSchemaName);

		Assert.assertNotNull(tableNameList);

		System.out.println("Oracle tables list for default schema : " + tableNameList.toString());

		conn.close();

	}

}

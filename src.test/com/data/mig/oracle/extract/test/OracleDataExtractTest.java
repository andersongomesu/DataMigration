package com.data.mig.oracle.extract.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.extract.OracleDataExtract;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class OracleDataExtractTest {
	@Test
	public void getOracleDataExtractTest() throws SQLException {
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn = oracleDatabaseConnect.getOracleDBConnection(
				IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		Assert.assertNotNull(conn);

		OracleDataExtract oracleDataExtract = new OracleDataExtract();

		boolean extractResult = oracleDataExtract.extractOracleDataIntoJsonFile(IApplicationConstants.defaultOracleSchemaName, "EMPLOYEES", 
				2L, "D:\\Sampath\\MS\\Dissertation\\MySQL\\oracleextract.json");
		
		Assert.assertTrue(extractResult);

		conn.close();

	}
	


}

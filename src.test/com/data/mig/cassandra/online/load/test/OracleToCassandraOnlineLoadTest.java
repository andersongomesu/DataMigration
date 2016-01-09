package com.data.mig.cassandra.online.load.test;

import org.junit.Test;
import org.springframework.util.Assert;

import com.data.mig.cassandra.online.load.OracleToCassandraOnlineLoad;
import com.data.mig.constants.IApplicationConstants;

public class OracleToCassandraOnlineLoadTest {

	@Test
	public void loadDataFromOracleToCassandraIncludingChildTablesTest() {
		OracleToCassandraOnlineLoad oracleToCassandraOnlineLoad = new OracleToCassandraOnlineLoad();

		// Extract including the child tables
		Boolean loadStatus = oracleToCassandraOnlineLoad.loadDataFromOracleToCassandra(
				IApplicationConstants.defaultOracleSchemaName, "EMPLOYEES", "mykeyspace", "EMPLOYEES", 2L, true); //payments, order
		
		
		Assert.isTrue(loadStatus);
	}

	@Test
	public void loadDataFromOracleToCassandraWithoutChildTablesTest() {
		OracleToCassandraOnlineLoad oracleToCassandraOnlineLoad = new OracleToCassandraOnlineLoad();

		// Extract without the child tables
		Boolean loadStatus = oracleToCassandraOnlineLoad.loadDataFromOracleToCassandra(
				IApplicationConstants.defaultOracleSchemaName, "EMPLOYEES", "mykeyspace", "EMPLOYEES", 10L, false);
		
		loadStatus = oracleToCassandraOnlineLoad.loadDataFromOracleToCassandra(
				IApplicationConstants.defaultOracleSchemaName, "DEPARMENTS", "mykeyspace", "DEPARMENTS", 10L, false);
		


		Assert.isTrue(loadStatus);
	}

}

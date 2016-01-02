package com.data.mig.mongo.online.load.test;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mongo.online.load.OracleToMongoOnlineLoad;

public class OracleToMongoOnlineLoadTest {

	@Test
	public void loadDataFromOracleToMongoIncludingChildTablesTest() {
		OracleToMongoOnlineLoad oracleToMongoOnlineLoad = new OracleToMongoOnlineLoad();

		// Extract including the child tables
		Boolean loadStatus = oracleToMongoOnlineLoad.loadDataFromOracleToMongo(
				IApplicationConstants.defaultOracleSchemaName, "EMPLOYEES", "test", "mycol111", 10L, true);

		Assert.isTrue(loadStatus);
	}

	@Test
	@Ignore
	public void loadDataFromOracleToMongoWithoutChildTablesTest() {
		OracleToMongoOnlineLoad oracleToMongoOnlineLoad = new OracleToMongoOnlineLoad();

		// Extract without the child tables
		Boolean loadStatus = oracleToMongoOnlineLoad.loadDataFromOracleToMongo(
				IApplicationConstants.defaultOracleSchemaName, "EMPLOYEES", "test", "mycol111", 10L, false);

		Assert.isTrue(loadStatus);
	}

}

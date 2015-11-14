package com.data.mig.mongo.online.load.test;

import org.junit.Test;
import org.springframework.util.Assert;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mongo.online.load.MysqlToMongoOnlineLoad;

public class MyqlToMongoOnlineLoadTest {
	
	@Test
	public void loadDataFromMysqlToMongoIncludingChildTablesTest () {
		MysqlToMongoOnlineLoad mysqlToMongoOnlineLoad = new MysqlToMongoOnlineLoad();
		
		//Extract including the child tables
		Boolean loadStatus = mysqlToMongoOnlineLoad.loadDataFromMysqlToMongo(IApplicationConstants.defaultMySqlSchemaName, "customers","test", "mycol111", 1L, true);
		
		Assert.isTrue(loadStatus);
	}

	@Test
	public void loadDataFromMysqlToMongoWithoutChildTablesTest () {
		MysqlToMongoOnlineLoad mysqlToMongoOnlineLoad = new MysqlToMongoOnlineLoad();
		
		//Extract without the child tables
		Boolean loadStatus = mysqlToMongoOnlineLoad.loadDataFromMysqlToMongo(IApplicationConstants.defaultMySqlSchemaName, "customers","test", "mycol111", 1L, false);
		
		Assert.isTrue(loadStatus);
	}

}

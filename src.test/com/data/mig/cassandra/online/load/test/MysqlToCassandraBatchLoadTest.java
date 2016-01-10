package com.data.mig.cassandra.online.load.test;

import org.junit.Test;
import org.springframework.util.Assert;

import com.data.mig.cassandra.online.load.MysqlToCassandraBatchLoad;
import com.data.mig.constants.IApplicationConstants;

public class MysqlToCassandraBatchLoadTest {
	
	@Test
	public void writeDataFromMysqlToJsonFileTest() {
		MysqlToCassandraBatchLoad mysqlToCassandraBatchLoad = new MysqlToCassandraBatchLoad();

		// Extract including the child tables
		Boolean loadStatus = mysqlToCassandraBatchLoad.writeDataFromMysqlToJsonFile(
				IApplicationConstants.defaultMySqlSchemaName, "customers", "customers", 2L, "D:\\Sampath\\MS\\Dissertation\\MySQL", true); 
		
		Assert.isTrue(loadStatus);
	}
	
	@Test
	public void writeDataFromMysqlToJsonFileWithoutChildTableTest() {
		MysqlToCassandraBatchLoad mysqlToCassandraBatchLoad = new MysqlToCassandraBatchLoad();

		// Extract without the child tables
		Boolean loadStatus = mysqlToCassandraBatchLoad.writeDataFromMysqlToJsonFile(
				IApplicationConstants.defaultMySqlSchemaName, "customers", "customers", 2L, "D:\\Sampath\\MS\\Dissertation\\MySQL", false); 
		
		Assert.isTrue(loadStatus);
	}	

}

package com.data.mig.cassandra.online.load.test;

import org.junit.Test;
import org.springframework.util.Assert;

import com.data.mig.cassandra.online.load.MysqlToCassandraOnlineLoad;
import com.data.mig.constants.IApplicationConstants;

public class MysqlToCassandraOnlineLoadTest {

	@Test
	public void loadDataFromMysqlToCassandraIncludingChildTablesTest() {
		MysqlToCassandraOnlineLoad mysqlToCassandraOnlineLoad = new MysqlToCassandraOnlineLoad();

		// Extract including the child tables
		Boolean loadStatus = mysqlToCassandraOnlineLoad.loadDataFromMysqlToCassandra(
				IApplicationConstants.defaultMySqlSchemaName, "customers", "mykeyspace", "customers", 10L, true);

		Assert.isTrue(loadStatus);
	}

	@Test
	public void loadDataFromMysqlToCassandraWithoutChildTablesTest() {
		MysqlToCassandraOnlineLoad mysqlToCassandraOnlineLoad = new MysqlToCassandraOnlineLoad();

		// Extract without the child tables
		Boolean loadStatus = mysqlToCassandraOnlineLoad.loadDataFromMysqlToCassandra(
				IApplicationConstants.defaultMySqlSchemaName, "customers", "mykeyspace", "customers", 10L, false);

		Assert.isTrue(loadStatus);
	}

}

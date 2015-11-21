package com.data.mig.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.data.mig.cassandra.online.load.test.MysqlToCassandraOnlineLoadTest;
import com.data.mig.db.test.CassandraDatabaseColumnFamilyTest;
import com.data.mig.db.test.CassandraDatabaseConnectTest;
import com.data.mig.db.test.CassandraDatabaseKeyspaceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CassandraDatabaseConnectTest.class,
	CassandraDatabaseKeyspaceTest.class,
	CassandraDatabaseColumnFamilyTest.class,
	MysqlToCassandraOnlineLoadTest.class
	
})
public class MysqlToCassandraOnlineTestSuite {

}

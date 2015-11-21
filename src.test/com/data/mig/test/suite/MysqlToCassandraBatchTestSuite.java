package com.data.mig.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.data.mig.db.test.CassandraDatabaseColumnFamilyTest;
import com.data.mig.db.test.CassandraDatabaseConnectTest;
import com.data.mig.db.test.CassandraDatabaseKeyspaceTest;
import com.data.mig.read.json.extract.test.ReadJsonDataFromFileToCassandraTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CassandraDatabaseConnectTest.class,
	CassandraDatabaseKeyspaceTest.class,
	CassandraDatabaseColumnFamilyTest.class,
	ReadJsonDataFromFileToCassandraTest.class
	
})
public class MysqlToCassandraBatchTestSuite {

}

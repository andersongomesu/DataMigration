package com.data.mig.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.data.mig.db.test.MongoDatabaseCollectionRemoveAllTest;
import com.data.mig.db.test.MongoDatabaseCollectionTest;
import com.data.mig.db.test.MongoDatabaseConnectTest;
import com.data.mig.mongo.online.load.test.MysqlToMongoOnlineLoadTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MongoDatabaseConnectTest.class,
	MongoDatabaseCollectionTest.class,
	MongoDatabaseCollectionRemoveAllTest.class,
	MysqlToMongoOnlineLoadTest.class
	
})
public class MysqlToMongoOnlineTestSuite {

}

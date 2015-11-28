package com.data.mig.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.data.mig.db.test.MongoDatabaseCollectionRemoveAllTest;
import com.data.mig.db.test.MongoDatabaseCollectionTest;
import com.data.mig.db.test.MongoDatabaseConnectTest;
import com.data.mig.mysql.extract.test.MysqlDataSubsequentExtractTest;
import com.data.mig.read.json.extract.test.ReadJsonDataFromSubsequentExtractFileToMongoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MongoDatabaseConnectTest.class,
	MongoDatabaseCollectionTest.class,
	MysqlDataSubsequentExtractTest.class,
	MongoDatabaseCollectionRemoveAllTest.class,
	ReadJsonDataFromSubsequentExtractFileToMongoTest.class
	
})
public class MysqlToMongoSubsequentBatchTestSuite {

}

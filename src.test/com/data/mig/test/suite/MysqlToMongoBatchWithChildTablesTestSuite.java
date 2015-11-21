package com.data.mig.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.data.mig.db.test.MongoDatabaseCollectionTest;
import com.data.mig.db.test.MongoDatabaseConnectTest;
import com.data.mig.mysql.extract.test.MysqlDataExtractWithChildTablesTest;
import com.data.mig.read.json.extract.test.ReadJsonDataFromFileToMongoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MongoDatabaseConnectTest.class,
	MongoDatabaseCollectionTest.class,
	//Add clean up class
	MysqlDataExtractWithChildTablesTest.class,
	ReadJsonDataFromFileToMongoTest.class
	
})
public class MysqlToMongoBatchWithChildTablesTestSuite {

}

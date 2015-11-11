package com.data.mig.db.test;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.db.MongoCollectionFind;
import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.DB;
import com.mongodb.DBCollection;

@SuppressWarnings("deprecation")
public class MongoCollectionFindTest {

	@Test
	public void findAllDataInCollection() {
		
		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect ();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection ();
		MongoCollectionFind mongoCollectionFind = new MongoCollectionFind ();
		
		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();
		
		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb,"mycol");
		
		Integer noOfRecords = mongoCollectionFind.findAllFromCollection(dbCollection);
		 
		System.out.println("No of records in collection :" + noOfRecords);
		
		Assert.assertNotNull(noOfRecords);
		
	}
}

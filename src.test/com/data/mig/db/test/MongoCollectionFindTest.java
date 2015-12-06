package com.data.mig.db.test;

import org.junit.Test;

import com.data.mig.db.MongoCollectionFind;
import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MongoCollectionFindTest {

	@Test
	public void findAllDataInCollectionTest () {
		
		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect ();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection ();
		MongoCollectionFind mongoCollectionFind = new MongoCollectionFind ();
		
		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();
		
		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb,"mycol111");
		
		Integer noOfRecords = mongoCollectionFind.findAllFromCollection(dbCollection);
		 
		System.out.println("No of records in collection :" + noOfRecords);
		
		Assert.assertNotNull(noOfRecords);
		
	}
	
	@Test
	public void findProductLinesByProductLineFromCollectionTest () {
		
		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect ();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection ();
		MongoCollectionFind mongoCollectionFind = new MongoCollectionFind ();
		
		DB mongodb = mangoDatabaseConnect.getMongoDBConnection("test");
		
		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb,"mycol111");
		
		DBObject productLineDbObject = mongoCollectionFind.findProductLinesByProductLineFromCollection(dbCollection, "Trains");
		 
		System.out.println("Product line image :" + productLineDbObject.get("productLine"));
		
		Assert.assertNotNull(productLineDbObject);
		Assert.assertNotNull(productLineDbObject.get("productLine"));
		
	}	
}

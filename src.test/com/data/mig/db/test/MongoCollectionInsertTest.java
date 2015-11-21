package com.data.mig.db.test;

import org.junit.Test;

import com.data.mig.db.MongoCollectionInsert;
import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MongoCollectionInsertTest {
	
	@Test
	public void insertDocumentIntoCollectionTest() {
		
		/*MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect ();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection ();*/
		MongoCollectionInsert mongoCollectionInsert = new MongoCollectionInsert ();
		
		//DB mongodb = mangoDatabaseConnect.getMongoDBConnection("test");
		
		//DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb,"mycol");
		
		BasicDBObject document = new BasicDBObject ();
		document.put("name", "Ganesh");

		
		Boolean writeAcked = mongoCollectionInsert.insertIntoCollection("test", "mycol", document);
		 
		System.out.println("Write ack status :" + writeAcked);
		
		Assert.assertTrue(writeAcked);
		
	}
	
	@Test
	public void insertDocumentIntoCollectionPassingCollectionTest() {
		
		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect ();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection ();
		MongoCollectionInsert mongoCollectionInsert = new MongoCollectionInsert ();
		
		DB mongodb = mangoDatabaseConnect.getMongoDBConnection("test");
		
		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb,"mycol");
		
		BasicDBObject document = new BasicDBObject ();
		document.put("name", "Siva");

		
		Boolean writeAcked = mongoCollectionInsert.insertIntoCollection(dbCollection, document);
		 
		System.out.println("Write ack status :" + writeAcked);
		
		Assert.assertTrue(writeAcked);
		
	}	

}

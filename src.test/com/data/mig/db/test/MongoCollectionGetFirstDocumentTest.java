package com.data.mig.db.test;

import java.util.Map;
import java.util.Map.Entry;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Test;

import com.data.mig.db.MongoCollectionGetFirstDocument;
import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MongoCollectionGetFirstDocumentTest {

	@Test
	public void findAllDataInCollectionUsingMongoClientTest() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();

		MongoClient mongoClient = mangoDatabaseConnect.getMongoDBClient();
		
		//CodecRegistry codecRegistry = mongoClient.getMongoClientOptions().getCodecRegistry();
		
		MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
		
		MongoCollection<BsonDocument> mongoCollectionCustomer = mongoDatabase.getCollection("mycol111", BsonDocument.class);
		
		FindIterable<BsonDocument> bsonDocumentAll = mongoCollectionCustomer.find();
		
		MongoCursor<BsonDocument> bsonDocumentCursor = bsonDocumentAll.iterator();
		
		Assert.assertNotNull(bsonDocumentCursor);

		int i = 0;
		while (bsonDocumentCursor.hasNext()) {

			System.out.println("Count :" + ++i);
			
			BsonDocument bsonDocument= bsonDocumentCursor.next();
			
			for (Map.Entry<String, BsonValue> bsonDocumentMap : bsonDocument.entrySet())
				
				System.out.println(bsonDocumentMap.getKey() + " ; " + bsonDocumentMap.getValue() + " ; "
						+ bsonDocumentMap.getValue().getBsonType());

		}

	}
	
	@Test
	public void findAllDataInCollectionUsingDBObjectTest() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();
		MongoCollectionGetFirstDocument mongoCollectionGetFirstDocument = new MongoCollectionGetFirstDocument();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();

		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb, "mycol111");
		

		BasicDBObject basicDBObject = (BasicDBObject) mongoCollectionGetFirstDocument
				.findAllFromCollection(dbCollection);

		Codec<BsonDocument> codec = MongoClient.getDefaultCodecRegistry().get(BsonDocument.class);
		
		 CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
		            MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(codec));

		BsonDocument bsonDocument = basicDBObject.toBsonDocument(new BsonDocument().getClass(), codecRegistry);

		Assert.assertNotNull(basicDBObject);

		int i = 0;
		for (Entry<String, BsonValue> bsonDocumentMap : bsonDocument.entrySet()) {

			System.out.println("Count :" + ++i);

			if (bsonDocumentMap.getKey().equals("customers")) {
				System.out.println(bsonDocumentMap.getKey() + " ; " + bsonDocumentMap.getValue() + " ; "
						+ bsonDocumentMap.getValue().getBsonType());
			}

		}

	}
}

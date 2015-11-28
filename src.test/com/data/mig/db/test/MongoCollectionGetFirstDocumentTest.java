package com.data.mig.db.test;

import java.util.Map.Entry;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;
import org.junit.Test;

import com.data.mig.db.MongoCollectionGetFirstDocument;
import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MongoCollectionGetFirstDocumentTest {

	@Test
	public void findAllDataInCollection() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();
		MongoCollectionGetFirstDocument mongoCollectionGetFirstDocument = new MongoCollectionGetFirstDocument();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();

		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb, "mycol111");

		BasicDBObject basicDBObject = (BasicDBObject) mongoCollectionGetFirstDocument.findAllFromCollection(dbCollection);
		
		
		BsonDocument bsonDocument  = basicDBObject.toBsonDocument(new BsonDocument().getClass(), new BsonDocumentCodec().getCodecRegistry() );

		Assert.assertNotNull(basicDBObject);

		int i = 0;
		for (Entry<String, BsonValue> bsonDocumentMap: bsonDocument.entrySet()) {
			
			System.out.println("Count :" + i);
			
			System.out.println(bsonDocumentMap.getKey() + " ; " + bsonDocumentMap.getValue() + " ; "
					+ bsonDocumentMap.getValue().getBsonType());
			

		}

	}
}

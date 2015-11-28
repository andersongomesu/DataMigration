package com.data.mig.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoCommandException;

public class MongoDatabaseCollection {

	public DBCollection getMongoCollection(DB db, String collectionName) {

		DBCollection dbCollection = db.getCollection(collectionName);

		return dbCollection;

	}

	public DBCollection createMongoCollection(DB db, String collectionName) {

		DBCollection dbCollection = null;

		try {

			dbCollection = db.createCollection(collectionName,
					new BasicDBObject());

		} catch (MongoCommandException me) {
			System.out.println("Collection create error code :" + me.getErrorCode());
			if (me.getErrorCode() == 48) {
				System.out.println("Collection create error message :" + me.getErrorMessage());
			}
			dbCollection = db.getCollection(collectionName);

		}

		return dbCollection;

	}
	
	public DBCollection dropMongoCollection(DB db, String collectionName) {

		DBCollection dbCollection = null;

		try {

			dbCollection = db.getCollection(collectionName);
			
			dbCollection.drop();

		} catch (MongoCommandException me) {
			System.out.println("Collection drop error code :" + me.getErrorCode());
			System.out.println(me.toString());

		}

		return dbCollection;

	}
	
	public DBCollection removeAllMongoCollection(DB db, String collectionName) {

		DBCollection dbCollection = null;

		try {

			dbCollection = db.getCollection(collectionName);
					
			dbCollection.remove(new BasicDBObject());

		} catch (MongoCommandException me) {			
			System.out.println("Collection remove all error code :" + me.getErrorCode());
			System.out.println(me.toString());

		}

		return dbCollection;

	}	
	
}

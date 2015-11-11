package com.data.mig.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoCommandException;
import com.mongodb.WriteResult;

public class MongoCollectionInsert {

	public Boolean insertIntoCollection(DBCollection dbCollection,
			BasicDBObject document) {

		WriteResult writeResult = dbCollection.insert(document);

		// System.out.println("Write acked :" + writeResult.wasAcknowledged());
		Boolean writeAcked = writeResult.wasAcknowledged();

		return writeAcked;

	}

	public Boolean insertIntoCollection(String dbName, String collectionName,
			BasicDBObject document) {

		MongoDatabaseConnect mongoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mongoDatabaseCollection = new MongoDatabaseCollection();

		DB mongoDB = null;
		DBCollection dbCollection = null;
		WriteResult writeResult = null;

		if (dbName == null) {
			System.out.println("DB name is not passed...");
			return false;
		} else {
			mongoDB = mongoDatabaseConnect.getMongoDBConnection(dbName);
			dbCollection = mongoDatabaseCollection.getMongoCollection(mongoDB,
					collectionName);

		}

		try {
			if (dbCollection != null) {
				System.out.println("Insert the doc : " + document);
				writeResult = dbCollection.insert(document);
			}

		} catch (MongoCommandException me) {
			me.printStackTrace();
		}

		Boolean writeAcked = writeResult.wasAcknowledged();

		System.out.println("Write acked :" + writeAcked);
		return writeAcked;

	}

	public Boolean insertIntoCollection(String dbName, String collectionName,
			DBObject document) {

		MongoDatabaseConnect mongoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mongoDatabaseCollection = new MongoDatabaseCollection();

		DB mongoDB = null;
		DBCollection dbCollection = null;
		WriteResult writeResult = null;

		if (dbName == null) {
			System.out.println("DB name is not passed...");
			return false;
		} else {
			mongoDB = mongoDatabaseConnect.getMongoDBConnection(dbName);
			dbCollection = mongoDatabaseCollection.getMongoCollection(mongoDB,
					collectionName);

		}

		try {
			if (dbCollection != null) {
				System.out.println("Insert the doc : " + document);
				writeResult = dbCollection.insert(document);
			}

		} catch (MongoCommandException me) {
			me.printStackTrace();
		}

		Boolean writeAcked = writeResult.wasAcknowledged();

		System.out.println("Write acked :" + writeAcked);
		return writeAcked;

	}

}

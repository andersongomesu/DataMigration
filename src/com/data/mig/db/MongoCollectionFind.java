package com.data.mig.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoCollectionFind {

	public Integer findAllFromCollection(DBCollection dbCollection) {

		DBCursor dbCursor = dbCollection.find();
		Integer noOfRecords = 0;

		if (dbCursor != null) {

			while (dbCursor.hasNext()) {
				noOfRecords++;
				DBObject dbObject = dbCursor.next();

				System.out.println(dbObject.toString());
			}

		}

		return noOfRecords;

	}

	public DBCursor findAllProductLinesFromCollection(DBCollection dbCollection) {

		DBCursor dbCursor = dbCollection.find();

		return dbCursor;

	}

	public DBObject findProductLinesByProductLineFromCollection(DBCollection dbCollection, String productLine) {

		DBObject productLineDbObject = null;

		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("productLine", productLine);

		DBCursor dbCursor = dbCollection.find(whereQuery);
		Integer noOfRecords = 0;

		if (dbCursor != null) {

			while (dbCursor.hasNext()) {
				noOfRecords++;
				productLineDbObject = dbCursor.next();
				break;

			}

		}

		return productLineDbObject;

	}

}

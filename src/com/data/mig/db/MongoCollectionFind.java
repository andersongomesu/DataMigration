package com.data.mig.db;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoCollectionFind {
	
	public Integer findAllFromCollection (DBCollection dbCollection) {
		
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

}

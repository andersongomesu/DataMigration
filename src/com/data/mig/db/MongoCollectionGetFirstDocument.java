package com.data.mig.db;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoCollectionGetFirstDocument {
	
	public DBObject findAllFromCollection (DBCollection dbCollection) {
		
		DBObject dbObject = null;
		DBCursor dbCursor = dbCollection.find();
		
		if (dbCursor != null) {
			
			while (dbCursor.hasNext()) {
				dbObject = dbCursor.next();
				
				System.out.println(dbObject.toString());
				break;
			}
			
		}
		
		return dbObject;
		
	}

}

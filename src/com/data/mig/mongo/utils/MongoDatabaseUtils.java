package com.data.mig.mongo.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.data.mig.db.MongoCollectionInsert;
import com.mongodb.BasicDBObject;

public class MongoDatabaseUtils {

	public Boolean writeMapIntoMongoCollection(String mongoDatabaseName, String collectionName, String rootTableName,
			Map<String, Object> jsonDataMap) {

		System.out.println("### Start of write map to mongo collection process ###");

		Boolean writeMapToCollectionSuccessFlag = false;

		MongoCollectionInsert mongoCollectionInsert = null;

		long noOfWritesDone = 0;

		if (jsonDataMap != null) {

			mongoCollectionInsert = new MongoCollectionInsert();
			// Loop through the input map and insert the records into mongo
			// collection
			for (Map.Entry<String, Object> entrySet : jsonDataMap.entrySet()) {

				Map<String, Object> rootTableMap = new LinkedHashMap<String, Object>();

				noOfWritesDone++;
				rootTableMap.put(rootTableName, entrySet.getValue());

				BasicDBObject basicDbObject = new BasicDBObject(rootTableMap);
				mongoCollectionInsert.insertIntoCollection(mongoDatabaseName, collectionName, basicDbObject);

				

			}

			writeMapToCollectionSuccessFlag = true;
		} else {
			writeMapToCollectionSuccessFlag = false;
		}

		System.out.println("No of records inserted into mongo collection :" + noOfWritesDone);

		System.out.println("### End of write map to mongo collection process ###");

		return writeMapToCollectionSuccessFlag;
	}
	
	
	public Boolean writeMapIntoMongoCollectionForOnlineLoad(String mongoDatabaseName, String collectionName, String rootTableName,
			Map<String, Object> jsonDataMap) {

		System.out.println("### Start of write map to mongo collection process ###");

		Boolean writeMapToCollectionSuccessFlag = false;

		MongoCollectionInsert mongoCollectionInsert = null;

		long noOfWritesDone = 0;

		if (jsonDataMap != null) {

			mongoCollectionInsert = new MongoCollectionInsert();
			// Loop through the input map and insert the records into mongo
			// collection
			for (Map.Entry<String, Object> entrySet : jsonDataMap.entrySet()) {

				Map<String, Object> rootTableMap = new HashMap<String, Object>();

				noOfWritesDone++;
				//rootTableMap.put(rootTableName, entrySet.getValue());
				
				rootTableMap.put(rootTableName, entrySet.getValue().toString());

				BasicDBObject basicDbObject = new BasicDBObject(rootTableMap);
				mongoCollectionInsert.insertIntoCollection(mongoDatabaseName, collectionName, basicDbObject);

				

			}

			writeMapToCollectionSuccessFlag = true;
		} else {
			writeMapToCollectionSuccessFlag = false;
		}

		System.out.println("No of records inserted into mongo collection :" + noOfWritesDone);

		System.out.println("### End of write map to mongo collection process ###");

		return writeMapToCollectionSuccessFlag;
	}

}

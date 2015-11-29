package com.data.mig.mongo.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;

import com.data.mig.db.MongoCollectionInsert;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

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

				//Map<String, Object> rootTableMap = new HashMap<String, Object>();

				noOfWritesDone++;
				//rootTableMap.put(rootTableName, entrySet.getValue());
				
				/*rootTableMap.put(rootTableName, entrySet.getValue().toString());

				BasicDBObject basicDbObject = new BasicDBObject(rootTableMap);*/
				BasicDBObject basicDbObject = (BasicDBObject) JSON.parse (entrySet.getValue().toString());
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
	
	public String dataTypeFinder(Object value) {

		String datatype = "text";

		if (value == null) {
			datatype = "text";
		} else if (value instanceof Integer) {
			datatype = "varint";
		} else if (value instanceof String) {
			datatype = "varchar";
		} else if (value instanceof Boolean) {
			datatype = "boolean";
		} else if (value instanceof Date) {
			datatype = "timestamp";
		} else if (value instanceof Long) {
			datatype = "varint";
		} else if (value instanceof Double) {
			datatype = "double";
		} else if (value instanceof Float) {
			datatype = "float";
		} else if (value instanceof BigDecimal) {
			datatype = "bigint";
		} else if (value instanceof Byte) {
			datatype = "int";
		} else if (value instanceof byte[]) {
			datatype = "list";
		} else if (value instanceof JSONArray) {
			datatype = "JSONArray";
		} else {
			datatype = value.getClass().toString();
		}

		return datatype;
	}

}

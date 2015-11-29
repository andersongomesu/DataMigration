package com.data.mig.db;

import java.util.List;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDatabaseConnect {
	
	private String host = "localhost";
	private Integer port = 27017;

	@SuppressWarnings({ "deprecation" })
	public DB getMongoDBConnection() {

		DB db = null;
		MongoClient mongoClient = null;

		try {

			mongoClient = new MongoClient(host, port);

			List<String> databases = mongoClient.getDatabaseNames();

			for (String dbName : databases) {
				System.out.println("- Database: " + dbName);

				db = mongoClient.getDB(dbName);

				Set<String> collections = db.getCollectionNames();
				for (String colName : collections) {
					System.out.println("\t + Collection: " + colName);
				}
			}

			//mongoClient.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return db;

	}
	
	@SuppressWarnings({ "deprecation" })
	public DB getMongoDBConnection(String databaseName) {

		DB db = null;
		MongoClient mongoClient = null;

		try {

			mongoClient = new MongoClient(host, port);

			db = mongoClient.getDB(databaseName);

			//mongoClient.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return db;

	}
	
	public MongoClient getMongoDBClient() {

		MongoClient mongoClient = null;

		try {

			mongoClient = new MongoClient(host, port);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return mongoClient;

	}
	
	public boolean closeMongoDBConnection(MongoClient mongoClient) {
		
		boolean success = false;

		try {

			mongoClient.close();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;

	}	
}

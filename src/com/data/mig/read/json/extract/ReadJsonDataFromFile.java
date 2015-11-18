package com.data.mig.read.json.extract;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.data.mig.cassandra.utils.CassandraDatabaseUtils;
import com.data.mig.mongo.utils.MongoDatabaseUtils;
import com.mysql.jdbc.StringUtils;

public class ReadJsonDataFromFile {

	public boolean readJsonDataFromFile(String mongoDatabaseName, String collectionName, String filePath,
			String rootTableName) {

		boolean loadStatus = false;

		System.out.println("### Start of JSON file read process ###");

		// 1. Read from file
		Map<String, Object> jsonDataMap = readFile(filePath, rootTableName);

		MongoDatabaseUtils mongoDatabaseUtils = new MongoDatabaseUtils();

		// 2. Load into Mongo collection
		loadStatus = mongoDatabaseUtils.writeMapIntoMongoCollection(mongoDatabaseName, collectionName, rootTableName,
				jsonDataMap);

		System.out.println("### End of JSON file read process ###");
		return loadStatus;
	}

	
	public boolean readJsonDataFromFileintoCassandraDatabase(String keyspaceName,String rootColumnFamilyName, String filePath) {

		boolean loadStatus = false;

		System.out.println("### Start of JSON file read process ###");

		// 1. Read from file
		Map<String, Object> jsonDataMap = readFile(filePath, rootColumnFamilyName);

		CassandraDatabaseUtils cassandradatabaseUtils = new CassandraDatabaseUtils();

		// 2. Load into Cassandra
		
		loadStatus = cassandradatabaseUtils.writeMapIntoCassandraDatabase(keyspaceName, rootColumnFamilyName, jsonDataMap);
				
		System.out.println("### End of JSON file read process ###");
		return loadStatus;
	}
	
	public Map<String, Object> readFile(String filePath, String rootTableName) {

		FileReader reader = null;

		Map<String, Object> jsonMap = null;

		try {
			// create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode rootNode = objectMapper.readTree(new File(filePath));

			long i = 1;
			while (true) {
				String rootTableKey = rootTableName + i++;
				JsonNode idNode = rootNode.path(rootTableKey);
				if (idNode == null || StringUtils.isNullOrEmpty(idNode.toString())) {
					break;
				} else {
					if (jsonMap == null) {
						jsonMap = new LinkedHashMap<String, Object>();
					}
					jsonMap.put(rootTableKey, idNode.toString());
				}
			}
			System.out.println("Number of records processed :" + i);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonMap;
	}

}

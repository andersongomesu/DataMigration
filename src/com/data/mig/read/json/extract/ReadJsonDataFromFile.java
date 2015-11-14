package com.data.mig.read.json.extract;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.data.mig.db.MongoCollectionInsert;
import com.mongodb.BasicDBObject;
import com.mysql.jdbc.StringUtils;

public class ReadJsonDataFromFile {

	public boolean readJsonDataFromFile(String filePath, String rootTableName) {

		boolean loadStatus = false;
		
		System.out.println("### Start of process ###");

		Map<String, Object> jsonDataMap = readFile(filePath, rootTableName);

		if (jsonDataMap != null) {

			System.out.println("JSON array from file is not null");

			MongoCollectionInsert mongoCollectionInsert = new MongoCollectionInsert();

			/*
			 * mongoCollectionInsert.insertIntoCollection("test", "mycol",
			 * MongoDbBasicConverters.fromMapToDBObject(jsonDataMap));
			 */

			for (Map.Entry<String, Object> entrySet : jsonDataMap.entrySet()) {
				
				Map <String, Object> rootTableMap = new LinkedHashMap <String, Object> ();
				
				//customerMap.put(entrySet.getKey().substring(0, entrySet.getKey().length()-1), entrySet.getValue());
				rootTableMap.put(rootTableName, entrySet.getValue());

				BasicDBObject basicDbObject = new BasicDBObject(rootTableMap);
				mongoCollectionInsert.insertIntoCollection("test", "mycol111",
						basicDbObject);

				System.out.println(entrySet.getValue());

			}

		}

		loadStatus = true;
		System.out.println("### End of process ###");
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
				if (idNode == null
						|| StringUtils.isNullOrEmpty(idNode.toString())) {
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

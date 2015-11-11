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

	public boolean readJsonDataFromFile(String filePath) {

		boolean loadStatus = false;

		Map<String, Object> jsonDataMap = readFile(filePath);

		if (jsonDataMap != null) {

			System.out.println("JSON array from file is not null");

			MongoCollectionInsert mongoCollectionInsert = new MongoCollectionInsert();

			/*
			 * mongoCollectionInsert.insertIntoCollection("test", "mycol",
			 * MongoDbBasicConverters.fromMapToDBObject(jsonDataMap));
			 */

			for (Map.Entry<String, Object> entrySet : jsonDataMap.entrySet()) {
				
				Map <String, Object> customerMap = new LinkedHashMap <String, Object> ();
				
				//customerMap.put(entrySet.getKey().substring(0, entrySet.getKey().length()-1), entrySet.getValue());
				customerMap.put("customers", entrySet.getValue());

				BasicDBObject basicDbObject = new BasicDBObject(customerMap);
				mongoCollectionInsert.insertIntoCollection("test", "mycol111",
						basicDbObject);

				System.out.println(entrySet.getValue());

			}

		}

		loadStatus = true;
		return loadStatus;
	}

	public Map<String, Object> readFile(String filePath) {

		FileReader reader = null;

		Map<String, Object> jsonMap = null;

		try {
			// create ObjectMapper instance
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode rootNode = objectMapper.readTree(new File(filePath));

			long i = 1;
			while (true) {
				String customerKey = "customers" + i++;
				JsonNode idNode = rootNode.path(customerKey);
				if (idNode == null
						|| StringUtils.isNullOrEmpty(idNode.toString())) {
					break;
				} else {
					if (jsonMap == null) {
						jsonMap = new LinkedHashMap<String, Object>();
					}
					jsonMap.put(customerKey, idNode.toString());
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

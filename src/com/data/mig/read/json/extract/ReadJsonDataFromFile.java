package com.data.mig.read.json.extract;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.data.mig.cassandra.utils.CassandraDatabaseUtils;
import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mongo.utils.MongoDatabaseUtils;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableRelationship;
import com.data.mig.mysql.db.TableDetails;
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

	public boolean readJsonDataFromFileintoCassandraDatabase(String schemaName, String tableName, String keySpaceName,
			
			
			String rootColumnFamilyName, String folderPath, boolean isChildColumnFamilyRequired) {

		boolean loadStatus = false;

		String childColumnFamilyName = null;
		String fileName = null;
		String filePath = null;

		System.out.println("### Start of JSON file read process ###");

		if (isChildColumnFamilyRequired) {

			// 1. Read & Verify all filenamesfrom Folder
			List<TableDetails> childColumnFamilyList = getMysqlTableRelationshipDetails(schemaName,
					rootColumnFamilyName);
			for (TableDetails childColumnFamily : childColumnFamilyList) {
				childColumnFamilyName = (String) childColumnFamily.getTableName();

				fileName = childColumnFamilyName + "_" + rootColumnFamilyName + ".json";

				boolean fileFound = verifyFileExists(folderPath, fileName);

				if (fileFound) {
					// 1. Read from file
					filePath = folderPath + "/" + fileName;
					Map<String, Object> jsonDataMap = readFile(filePath, rootColumnFamilyName);
					CassandraDatabaseUtils cassandradatabaseUtils = new CassandraDatabaseUtils();

					// 2. Load into Cassandra
					loadStatus = cassandradatabaseUtils.writeMapIntoCassandraDatabase(schemaName, tableName,
							keySpaceName, rootColumnFamilyName, childColumnFamilyName, jsonDataMap);
					System.out.println("### End of JSON file read process ###");
				}
			}
		} else {
			// 1. Read from file
			filePath = folderPath + "/" + rootColumnFamilyName + ".json";
			Map<String, Object> jsonDataMap = readFile(filePath, rootColumnFamilyName);
			CassandraDatabaseUtils cassandradatabaseUtils = new CassandraDatabaseUtils();

			// 2. Load into Mongo collection
			loadStatus = cassandradatabaseUtils.writeMapIntoCassandraDatabase(schemaName, tableName, keySpaceName,
					rootColumnFamilyName, null, jsonDataMap);
		}
		return loadStatus;
	}

	public boolean verifyFileExists(String folderPath, String fileName) {

		boolean fileFound = false;

		File filePath = new File(folderPath + "/" + fileName);

		if (filePath.exists()) {
			/*
			 * File[] listOfFiles = folder.listFiles(); for (int i = 0; i <
			 * listOfFiles.length; i++) { if (listOfFiles[i].isFile()) {
			 * System.out.println("File " + listOfFiles[i].getName());
			 * if(listOfFiles[i].getName().equalsIgnoreCase(fileName)) } else if
			 * (listOfFiles[i].isDirectory()) { System.out.println("Directory "
			 * + listOfFiles[i].getName()); } }
			 */
			fileFound = true;

		} else if (!filePath.exists()) {
			System.out.println("extract file not found " + filePath);
		}

		return fileFound;
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

	private List<TableDetails> getMysqlTableRelationshipDetails(String schemaName, String tableName) {

		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(schemaName,
				IApplicationConstants.defaultMySqlUserId, IApplicationConstants.defaultMySqlPassword);

		MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();
		List<TableDetails> childTableDetailsList = mysqlTableRelationship.getMysqlTableRelationshipDetailsAsObject(conn,
				IApplicationConstants.defaultMySqlSchemaName, tableName);
		return childTableDetailsList;
	}
}

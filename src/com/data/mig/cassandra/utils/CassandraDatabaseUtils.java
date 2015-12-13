package com.data.mig.cassandra.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.db.CassandraDatabaseConnect;
import com.data.mig.db.CassandraDatabaseExecute;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableColumnDetails;
import com.data.mig.mysql.db.MysqlTableRelationship;
import com.data.mig.mysql.db.TableDetails;
import com.datastax.driver.core.Cluster;

public class CassandraDatabaseUtils {

	public Boolean writeMapIntoCassandraDatabase(String sourceSchemaName, String sourceTableName, String keySpaceName,
			String rootColumnFamilyName, String childColumnFamilyName, Map<String, Object> jsonDataMap) {

		System.out.println("### Start of write into Cassandra database process ###");

		boolean createColumnFamilyExecutedOnce = false;
		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();

		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		boolean writeMapToCassandraDatabaseSuccessFlag = false;

		long noOfWritesDone = 0;

		if (jsonDataMap != null) {
			JSONParser parser = new JSONParser();

			for (Map.Entry<String, Object> entrySet : jsonDataMap.entrySet()) {

				noOfWritesDone++;

				try {

					if (childColumnFamilyName != null) {
						JSONArray jsonArray = (JSONArray) parser.parse(entrySet.getValue().toString());
						if (entrySet.getValue() != null) /// dataTypeFinder(e.getValue())
						{
							String datatype = dataTypeFinder(entrySet.getValue(), entrySet.getKey().toString());
							// System.out.println("key is " + entrySet.getKey()
							// + "value is " + entrySet.getValue()
							// + "Class is" + entrySet.getValue().getClass());

							if (!createColumnFamilyExecutedOnce) {
								createCassandraDataModel(sourceSchemaName, sourceTableName, cluster, keySpaceName,
										jsonArray, rootColumnFamilyName, childColumnFamilyName);
								createColumnFamilyExecutedOnce = true;
							}

							boolean insertIntoCassandraColumnFamilySuccessFlag = insertIntoCassandraDataModel(cluster,
									keySpaceName, jsonArray, rootColumnFamilyName, childColumnFamilyName);
							if (!insertIntoCassandraColumnFamilySuccessFlag) {
								// System.out.println("insertIntoCassandraColumnFamily
								// is falied for column family
								// "+childColumnFamilyName + "_By_" +
								// rootColumnFamilyName +" for record
								// "+jsonObject.toJSONString());
								writeMapToCassandraDatabaseSuccessFlag = false;
							}

						} // if
					} else {
						JSONObject jsonObject = (JSONObject) parser.parse(entrySet.getValue().toString());
						if (entrySet.getValue() != null) /// dataTypeFinder(e.getValue())
						{
							String datatype = dataTypeFinder(entrySet.getValue(), entrySet.getKey().toString());
							// System.out.println(" jsonObject key is " +
							// entrySet.getKey() + "value is " +
							// entrySet.getValue()
							// + "Class is" + entrySet.getValue().getClass());
							if (!createColumnFamilyExecutedOnce) {
								createCassandraColumnFamily(sourceSchemaName, sourceTableName, cluster, keySpaceName,
										jsonObject, rootColumnFamilyName, childColumnFamilyName);
								createColumnFamilyExecutedOnce = true;
							}
							boolean insertIntoCassandraColumnFamilySuccessFlag = insertIntoCassandraColumnFamily(
									cluster, keySpaceName, jsonObject, rootColumnFamilyName, childColumnFamilyName);
							if (!insertIntoCassandraColumnFamilySuccessFlag) {
								// System.out.println("insertIntoCassandraColumnFamily
								// is falied for column family
								// "+childColumnFamilyName + "_By_" +
								// rootColumnFamilyName +" for record
								// "+jsonObject.toJSONString());
								writeMapToCassandraDatabaseSuccessFlag = false;
							}
						}
					}
				} // try
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			writeMapToCassandraDatabaseSuccessFlag = true;
		}

		else {
			writeMapToCassandraDatabaseSuccessFlag = false;
		}

		System.out.println("No of records inserted into write into Cassandra database :" + noOfWritesDone);

		System.out.println("### End of write into Cassandra database process ###");

		return writeMapToCassandraDatabaseSuccessFlag;
	}

	public String dataTypeFinder(Object value, String key) {

		String datatype = "text";

		if (value == null) {
			datatype = "text";
		} else if (value instanceof Integer) {
			datatype = "varint";
		} else if (value instanceof String) {
			if (key.equalsIgnoreCase("image")) {
				datatype = "blob";
			} else
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

	public boolean createCassandraDataModel(String sourceSchemaName, String sourceTableName, Cluster cluster,
			String keySpaceName, JSONArray jsonArray, String rootColumnFamilyName, String childColumnFamilyName) {
		boolean createCassandraDataModelSuccessFlag = false;

		JSONObject jsonObject = new JSONObject();

		if (jsonArray.get(0) != null) {
			jsonObject = (JSONObject) jsonArray.get(0);
			createCassandraDataModelSuccessFlag = createCassandraColumnFamily(sourceSchemaName, sourceTableName,
					cluster, keySpaceName, jsonObject, rootColumnFamilyName, childColumnFamilyName);
		}

		return createCassandraDataModelSuccessFlag;
	}

	public boolean createCassandraColumnFamily(String sourceSchemaName, String sourceTableName, Cluster cluster,
			String keySpaceName, JSONObject jsonObject, String rootColumnFamilyName, String childColumnFamilyName) {
		boolean columnfamilyCreateSuccessFlag = false;

		String parentTablePrimaryKey = null;
		String childTablePrimaryKey = null;
		String columnFamilyName = null;
		if (childColumnFamilyName != null) {
			columnFamilyName = childColumnFamilyName + "_By_" + rootColumnFamilyName;
		} else
			columnFamilyName = rootColumnFamilyName;

		String createColumnFamilyCommand = "Create TABLE " + columnFamilyName + " (" + columnFamilyName + "_id uuid, ";

		Iterator jsonIterator = jsonObject.entrySet().iterator();

		while (jsonIterator.hasNext()) {
			Map.Entry e = (Map.Entry) jsonIterator.next();

			if (e.getValue() != null) {
				String datatype = dataTypeFinder(e.getValue(), e.getKey().toString());
				if (!datatype.equalsIgnoreCase("JSONArray")) {
					createColumnFamilyCommand += e.getKey() + " " + dataTypeFinder(e.getValue(), e.getKey().toString())
							+ ",  ";
				}

			} else {
				createColumnFamilyCommand += e.getKey() + " text, ";

			}
		}
		try {

			Connection conn = getDatabaseConnection();
			MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();
			parentTablePrimaryKey = getPrimayKeyColumnDetails(conn, sourceSchemaName, sourceTableName);
			if (childColumnFamilyName != null) {
				childTablePrimaryKey = getPrimayKeyColumnDetails(conn, sourceSchemaName, childColumnFamilyName);
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		if (childColumnFamilyName != null) {
				if(childTablePrimaryKey.equalsIgnoreCase(parentTablePrimaryKey))
				{
					createColumnFamilyCommand += "PRIMARY KEY (" + parentTablePrimaryKey + "));";
				}
				else
				{
					createColumnFamilyCommand += "PRIMARY KEY (" + parentTablePrimaryKey + "," + childTablePrimaryKey + "));";
				}
		}
		else
		{
			createColumnFamilyCommand += "PRIMARY KEY (" + parentTablePrimaryKey + "));";
		}
		/*if (childColumnFamilyName != null) {
			createColumnFamilyCommand += "PRIMARY KEY (" + parentTablePrimaryKey + "," + childTablePrimaryKey + "));";
		} else {
			createColumnFamilyCommand += "PRIMARY KEY (" + parentTablePrimaryKey + "));";
		}*/
		System.out.println(createColumnFamilyCommand);
		columnfamilyCreateSuccessFlag = executeQuery(cluster, keySpaceName, createColumnFamilyCommand);

		return columnfamilyCreateSuccessFlag;
	}

	public boolean insertIntoCassandraDataModel(Cluster cluster, String keySpaceName, JSONArray jsonArray,
			String rootColumnFamilyName, String childColumnFamilyName) {

		boolean insertAllRecordsSuccessFlag = true;
		boolean insertIntoCassandraColumnFamilySuccessFlag = false;

		Iterator jsonArrayIterator = jsonArray.iterator();

		while (jsonArrayIterator.hasNext()) {

			insertIntoCassandraColumnFamilySuccessFlag = false;
			JSONObject jsonObject = (JSONObject) jsonArrayIterator.next();

			insertIntoCassandraColumnFamilySuccessFlag = insertIntoCassandraColumnFamily(cluster, keySpaceName,
					jsonObject, rootColumnFamilyName, childColumnFamilyName);
			if (!insertIntoCassandraColumnFamilySuccessFlag) {
				// System.out.println("insertIntoCassandraColumnFamily is falied
				// for column family "+childColumnFamilyName + "_By_" +
				// rootColumnFamilyName +" for record
				// "+jsonObject.toJSONString());
				insertAllRecordsSuccessFlag = false;
			}

		}
		return insertAllRecordsSuccessFlag;
	}

	public boolean insertIntoCassandraColumnFamily(Cluster cluster, String keySpaceName, JSONObject jsonObject,
			String rootColumnFamilyName, String childColumnFamilyName) {

		boolean insertAllRecordsSuccessFlag = true;
		String insertKeysCommand = null;
		String insertValuesCommand = null;
		String columnFamilyName = null;
		if (childColumnFamilyName != null) {
			columnFamilyName = childColumnFamilyName + "_By_" + rootColumnFamilyName;
		} else {
			columnFamilyName = rootColumnFamilyName;
		}

		boolean insertIntoCassandraColumnFamilySuccessFlag = false;

		insertKeysCommand = "INSERT INTO " + keySpaceName + "." + columnFamilyName + " (" + columnFamilyName + "_id,";
		insertValuesCommand = " ) VALUES ( " + UUID.randomUUID() + ", ";

		System.out.println(jsonObject.toString());

		Iterator jsonIterator = jsonObject.entrySet().iterator();

		while (jsonIterator.hasNext()) {
			Map.Entry e = (Map.Entry) jsonIterator.next();

			if (e.getValue() != null) {
				String datatype = dataTypeFinder(e.getValue(), e.getKey().toString());
				if (datatype.equalsIgnoreCase("int") || datatype.equalsIgnoreCase("double")
						|| datatype.equalsIgnoreCase("varint") || datatype.equalsIgnoreCase("float")
						|| datatype.equalsIgnoreCase("bigint")) {
					insertKeysCommand += e.getKey() + ",";
					insertValuesCommand += e.getValue() + ", ";
				} else if (!datatype.equalsIgnoreCase("JSONArray")) {
					insertKeysCommand += e.getKey() + ",";
					insertValuesCommand += "'" + e.getValue() + "', ";
				}

			} else if (e.getValue() == null) {
				insertKeysCommand += e.getKey() + ", ";
				insertValuesCommand += " null, ";
			}

		}

		String insertIntoCassandraCommand = insertKeysCommand.substring(0, insertKeysCommand.lastIndexOf(',')) + " "
				+ insertValuesCommand.substring(0, insertValuesCommand.lastIndexOf(',')) + ");";
		// System.out.println(insertIntoCassandraCommand);

		insertIntoCassandraColumnFamilySuccessFlag = executeQuery(cluster, keySpaceName, insertIntoCassandraCommand);
		if (!insertIntoCassandraColumnFamilySuccessFlag) {
			if (childColumnFamilyName != null) {
				System.out
						.println("insertIntoCassandraColumnFamily is falied for column family  " + childColumnFamilyName
								+ "_By_" + rootColumnFamilyName + " for record   " + jsonObject.toJSONString());
			} else {
				System.out.println("insertIntoCassandraColumnFamily is falied for column family  "
						+ rootColumnFamilyName + " for record   " + jsonObject.toJSONString());
			}
			insertAllRecordsSuccessFlag = false;
		}

		return insertAllRecordsSuccessFlag;
	}

	public boolean executeQuery(Cluster cluster, String keyspaceName, String query) {
		boolean cassandraQueryExecuteSuccessFlag = false;
		CassandraDatabaseExecute cassandraQueryExecute = new CassandraDatabaseExecute();
		cassandraQueryExecuteSuccessFlag = cassandraQueryExecute.executeCassandraQuery(cluster, keyspaceName, query);

		return cassandraQueryExecuteSuccessFlag;
	}

	private Connection getDatabaseConnection() {

		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		return mysqlDatabaseConnect.getMySqlDBConnection(IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId, IApplicationConstants.defaultMySqlPassword);

	}

	private String getPrimayKeyColumnDetails(Connection conn, String schemaName, String tableName) {

		String primaryKeyName = null;
		MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();

		Map<String, String> primaryKeyMap = mysqlTableColumnDetails.getMysqlTablePrimaryKey(conn, schemaName,
				tableName);

		Iterator primaryKeyiterator = primaryKeyMap.keySet().iterator();

		while (primaryKeyiterator.hasNext()) {
			primaryKeyName = (String) primaryKeyiterator.next();
		}

		return primaryKeyName;
	}

	private List<TableDetails> getMysqlTableRelationshipDetails(Connection conn, String tableName) {
		MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();
		List<TableDetails> childTableDetailsList = mysqlTableRelationship.getMysqlTableRelationshipDetailsAsObject(conn,
				IApplicationConstants.defaultMySqlSchemaName, tableName);
		return childTableDetailsList;
	}

}

package com.data.mig.cassandra.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.data.mig.db.CassandraDatabaseColumnFamily;
import com.data.mig.db.CassandraDatabaseConnect;
import com.data.mig.db.CassandraDatabaseExecute;
import com.datastax.driver.core.Cluster;

public class CassandraDatabaseUtils {

	public Boolean writeMapIntoCassandraDatabase(String keyspaceName, String rootColumnFamilyName,
			Map<String, Object> jsonDataMap) {

		System.out.println("### Start of write into Cassandra database process ###");

		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
		//CassandraDatabaseColumnFamily cassandraColumnFamily = new CassandraDatabaseColumnFamily();

		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();

		//Session dbsession = null;

		try {

			// dbsession =
			// cassandraColumnFamily.createCassandraColumnFamily(cluster,
			// keyspaceName, rootTableName);
		} catch (Exception e) {
			System.out.println(
					"Error in Cassandra create Column Family in writeMapIntoCassandraDatabase method" + e.getMessage());
		}

		Boolean writeMapToCassandraDatabaseSuccessFlag = false;

		// MongoCollectionInsert mongoCollectionInsert = null;

		long noOfWritesDone = 0;

		boolean rootcolumnfamilyAlteredSuccessFlag = false;
		boolean rootcolumnfamilyAlteredOnce = false;
		if (jsonDataMap != null) {

			for (Map.Entry<String, Object> entrySet : jsonDataMap.entrySet()) {
				noOfWritesDone++;
				JSONParser parser = new JSONParser();
				try {
					JSONObject json = (JSONObject) parser.parse(entrySet.getValue().toString());
					Iterator jsonIterator = json.entrySet().iterator();

					while (jsonIterator.hasNext()) {
						Map.Entry e = (Map.Entry) jsonIterator.next();

						if (!rootcolumnfamilyAlteredSuccessFlag && !rootcolumnfamilyAlteredOnce) {
							rootcolumnfamilyAlteredSuccessFlag = alterRootCoulmnFamily(json, cluster, keyspaceName,
									rootColumnFamilyName);
							rootcolumnfamilyAlteredOnce = true;
						}

						if (e.getValue() != null)/// dataTypeFinder(e.getValue())
													/// != "JSONArray")
						{
							String datatype = dataTypeFinder(e.getValue());
							if (datatype.equalsIgnoreCase("JSONArray")) {
								createChildColumnFamily(cluster, keyspaceName, (JSONArray) e.getValue(),
										e.getKey().toString());
							}
						}

						insertintoColumnFamily(json, cluster, keyspaceName, rootColumnFamilyName);

					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			writeMapToCassandraDatabaseSuccessFlag = true;
		} else {
			writeMapToCassandraDatabaseSuccessFlag = false;
		}

		System.out.println("No of records inserted into write into Cassandra database :" + noOfWritesDone);

		System.out.println("### End of write into Cassandra database process ###");

		return writeMapToCassandraDatabaseSuccessFlag;
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

	public boolean alterRootCoulmnFamily(JSONObject json, Cluster cluster, String keyspaceName,
			String ColumnFamilyName) {

		boolean columnfamilyAlteredSuccessFlag = false;
		String alterTableCommand = "ALTER TABLE " + ColumnFamilyName;

		Iterator i = json.entrySet().iterator();

		while (i.hasNext()) {
			Map.Entry e = (Map.Entry) i.next();

			if (e.getValue() != null) {

				String datatype = dataTypeFinder(e.getValue());

				if (!datatype.equalsIgnoreCase("JSONArray")) {
					alterTableCommand = "ALTER TABLE " + ColumnFamilyName + " ADD " + e.getKey() + " "
							+ dataTypeFinder(e.getValue()) + "; ";
				}

			} else {
				alterTableCommand = "ALTER TABLE " + ColumnFamilyName + " ADD " + e.getKey() + " text; ";
			}

			columnfamilyAlteredSuccessFlag = executeQuery(cluster, keyspaceName, alterTableCommand);

		}

		return columnfamilyAlteredSuccessFlag;
	}

	public boolean insertintoColumnFamily(JSONObject json, Cluster cluster, String keyspaceName,
			String ColumnFamilyName) {

		boolean insertSuccessFlag = false;

		/*
		 * "INSERT INTO simplex.playlists (id, song_id, title, album, artist) "
		 * + "VALUES (" + "2cc9ccb7-6221-4ccb-8387-f22b6a1b354d," +
		 * "756716f7-2e54-4715-9f00-91dcbea6cf50," + "'La Petite Tonkinoise'," +
		 * "'Bye Bye Blackbird'," + "'Joséphine Baker'" + ");"
		 */
		String insertKeysCommand = "INSERT INTO " + keyspaceName + "." + ColumnFamilyName + " (" + ColumnFamilyName
				+ "_id,";
		String insertValuesCommand = " ) VALUES ( " + UUID.randomUUID() + ", ";
		Iterator i = json.entrySet().iterator();

		while (i.hasNext()) {
			Map.Entry e = (Map.Entry) i.next();

			if (e.getValue() != null) {
				String datatype = dataTypeFinder(e.getValue());
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

		String insertCommand = insertKeysCommand.substring(0, insertKeysCommand.lastIndexOf(',')) + " "
				+ insertValuesCommand.substring(0, insertValuesCommand.lastIndexOf(',')) + ");";
		// System.out.println(insertCommand);

		insertSuccessFlag = executeQuery(cluster, keyspaceName, insertCommand);

		return insertSuccessFlag;
	}

	public boolean executeQuery(Cluster cluster, String keyspaceName, String query) {
		boolean cassandraQueryExecuteSuccessFlag = false;
		CassandraDatabaseExecute cassandraQueryExecute = new CassandraDatabaseExecute();
		cassandraQueryExecuteSuccessFlag = cassandraQueryExecute.executeCassandraQuery(cluster, keyspaceName, query);

		return cassandraQueryExecuteSuccessFlag;
	}

	public void createChildColumnFamily(Cluster cluster, String keyspaceName, JSONArray JSONArrayValue,
			String childColumFamilyName) {
		try {

			boolean childcolumnfamilyAlteredSuccessFlag = false;
			boolean childcolumnfamilyAlteredOnce = false;

			CassandraDatabaseColumnFamily cassandraColumnFamily = new CassandraDatabaseColumnFamily();
			cassandraColumnFamily.createCassandraColumnFamily(cluster, keyspaceName, childColumFamilyName);

			Iterator i = JSONArrayValue.iterator();
			while (i.hasNext()) {
				JSONObject rootJSONObj = (JSONObject) i.next();
				// String finalValue = (String)jsnObj.get("customerNumber");
				Iterator ji = rootJSONObj.entrySet().iterator();
				while (ji.hasNext()) {
					Map.Entry e = (Map.Entry) ji.next();
					// System.out.println("Key: " + e.getKey()+" Value: " +
					// e.getValue());

					if (!childcolumnfamilyAlteredSuccessFlag && !childcolumnfamilyAlteredOnce) {
						childcolumnfamilyAlteredSuccessFlag = alterRootCoulmnFamily(rootJSONObj, cluster, keyspaceName,
								childColumFamilyName);
						childcolumnfamilyAlteredOnce = true;
					}

					if (e.getValue() != null)/// dataTypeFinder(e.getValue()) !=
												/// "JSONArray")
					{
						String datatype = dataTypeFinder(e.getValue());
						// System.out.println("Key: " + e.getKey()+" Value: " +
						// e.getValue()+datatype+e.getValue().getClass().toString());
						if (datatype.equalsIgnoreCase("JSONArray")) {
							createChildColumnFamily(cluster, keyspaceName, (JSONArray) e.getValue(),
									e.getKey().toString());
						}
					}

					insertintoColumnFamily(rootJSONObj, cluster, keyspaceName, childColumFamilyName);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

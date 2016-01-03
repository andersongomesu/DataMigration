package com.data.mig.cassandra.utils;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.db.CassandraDatabaseColumnFamily;
import com.data.mig.db.CassandraDatabaseConnect;
import com.data.mig.db.CassandraDatabaseExecute;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableColumnDetails;
import com.data.mig.mysql.db.MysqlTableRelationship;
import com.data.mig.mysql.db.TableDetails;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraDatabaseUtils {

	public String boundStatementForCassandraInsert;
	public Map<String, String> columnDetails =new HashMap<String,String>(); 
	
	public Connection conn=null;
	
	public Boolean writeMapIntoCassandraDatabase(String sourceSchemaName, String sourceTableName, String keySpaceName,
			String rootColumnFamilyName, String childColumnFamilyName, Map<String, Object> jsonDataMap) {

		System.out.println("### Start of write into Cassandra database process ###");

		boolean createColumnFamilyExecutedOnce = false;
		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();

		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		boolean writeMapToCassandraDatabaseSuccessFlag = false;
		boolean insertIntoCassandraColumnFamilySuccessFlag=false;
		
		boolean blobColumnFound = false;
		
		long noOfWritesDone = 0;
		conn = getDatabaseConnection();
		getColumnDetails(sourceSchemaName,sourceTableName,childColumnFamilyName);

		if (childColumnFamilyName != null) 
		{
			if(blobColumnFinder(conn,sourceSchemaName,sourceTableName) || blobColumnFinder(conn,sourceSchemaName,childColumnFamilyName))
			{
				blobColumnFound=true;
				
			}
		}
		else
		{
			if(blobColumnFinder(conn,sourceSchemaName,sourceTableName))
					{
						blobColumnFound=true;
					}
		}

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

							if(blobColumnFound)
							{
								
								System.out.println("Blob Coulmn Found");
								 insertIntoCassandraColumnFamilySuccessFlag = createCassandraDataModelBoundStatement(cluster,
										keySpaceName, jsonArray, rootColumnFamilyName, childColumnFamilyName);
								if (!insertIntoCassandraColumnFamilySuccessFlag) {
									
									writeMapToCassandraDatabaseSuccessFlag = false;
								}
							}
							else
							{
								insertIntoCassandraColumnFamilySuccessFlag = insertIntoCassandraDataModel(cluster,
										keySpaceName, jsonArray, rootColumnFamilyName, childColumnFamilyName);
								if (!insertIntoCassandraColumnFamilySuccessFlag) {
									// System.out.println("insertIntoCassandraColumnFamily
									// is falied for column family
									// "+childColumnFamilyName + "_By_" +
									// rootColumnFamilyName +" for record
									// "+jsonObject.toJSONString());
									writeMapToCassandraDatabaseSuccessFlag = false;
								}
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
							if(blobColumnFound)
							{
								
								if (boundStatementForCassandraInsert ==null) {
									boundStatementForCassandraInsert= createCassandraInsertBoundStatement(cluster,
											keySpaceName, jsonObject, rootColumnFamilyName, childColumnFamilyName);
									//System.out.println(boundStatementForCassandraInsert);
								}
	
	                            if(boundStatementForCassandraInsert!=null)
	                            {
	                            	insertWithBoundStatementIntoCassandraColumnFamily(cluster,
											keySpaceName, jsonObject, rootColumnFamilyName, childColumnFamilyName,boundStatementForCassandraInsert);
	                            }
							}
							else
							{
								 insertIntoCassandraColumnFamilySuccessFlag = insertIntoCassandraColumnFamily(
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
		if (key.equalsIgnoreCase("image")) {
			datatype = "blob";
		}
		else if (value == null) {
			datatype = "text";
		} else if (value instanceof Integer) {
			//datatype = "varint";
			datatype = "int";
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
			datatype = "int";
			//datatype = "varint";
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
				if(e.getKey().toString().equalsIgnoreCase("image"))
					createColumnFamilyCommand += e.getKey() + " blob, ";
				else
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
		 System.out.println(insertIntoCassandraCommand);

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

	public boolean createCassandraDataModelBoundStatement( Cluster cluster,
			String keySpaceName, JSONArray jsonArray, String rootColumnFamilyName, String childColumnFamilyName) {
		
		String  cassandraInsertBoundStatement=null;
		boolean insertAllRecordsSuccessFlag = true;
		boolean insertIntoCassandraColumnFamilySuccessFlag = false;
		
		JSONObject jsonObject = new JSONObject();

		if (jsonArray.get(0) != null) {
			jsonObject = (JSONObject) jsonArray.get(0);
			cassandraInsertBoundStatement= createCassandraInsertBoundStatement(cluster, keySpaceName, jsonObject, rootColumnFamilyName, childColumnFamilyName);
		}

		Iterator jsonArrayIterator = jsonArray.iterator();
		while (jsonArrayIterator.hasNext()) {

			insertIntoCassandraColumnFamilySuccessFlag = false;
			jsonObject = (JSONObject) jsonArrayIterator.next();
			
			insertIntoCassandraColumnFamilySuccessFlag = insertWithBoundStatementIntoCassandraColumnFamily(cluster, keySpaceName,
					jsonObject, rootColumnFamilyName, childColumnFamilyName,cassandraInsertBoundStatement);
			if (!insertIntoCassandraColumnFamilySuccessFlag) {
				insertAllRecordsSuccessFlag = false;
			}

		}
		return insertAllRecordsSuccessFlag;
		
	}
	public String createCassandraInsertBoundStatement(Cluster cluster, String keySpaceName, JSONObject jsonObject,
			String rootColumnFamilyName, String childColumnFamilyName) {
			String insertKeysBoundCommand = null;
			String insertKeyValuesBoundCommand = null;
			String columnFamilyName = null;
			
			
			if (childColumnFamilyName != null) {
				columnFamilyName = childColumnFamilyName + "_By_" + rootColumnFamilyName;
			} else {
				columnFamilyName = rootColumnFamilyName;
			}
		  
			
		    insertKeysBoundCommand = "INSERT INTO " + keySpaceName + "." + columnFamilyName + " (" + columnFamilyName + "_id,";
		    insertKeyValuesBoundCommand ="VALUES (?,";
			//insertKeysBoundCommand = "INSERT INTO " + keySpaceName + "." + columnFamilyName + " (" ;
			//insertKeyValuesBoundCommand ="VALUES (";
		   Iterator jsonIterator = jsonObject.entrySet().iterator();

		   while (jsonIterator.hasNext()) {
			  Map.Entry e = (Map.Entry) jsonIterator.next();
			  insertKeysBoundCommand+=e.getKey().toString()+",";
			  insertKeyValuesBoundCommand +="?,";
		   }
		   String insertIntoCassandraBoundCommand = insertKeysBoundCommand.substring(0, insertKeysBoundCommand.lastIndexOf(',')) + ") "
					+ insertKeyValuesBoundCommand.substring(0, insertKeyValuesBoundCommand.lastIndexOf(',')) + ");";
		   return insertIntoCassandraBoundCommand;
	}
	

	public boolean insertWithBoundStatementIntoCassandraColumnFamily(Cluster cluster, String keySpaceName, JSONObject jsonObject,
			String rootColumnFamilyName, String childColumnFamilyName,String boundStatement) {

		
		String insertKeysCommand = null;
		String insertValuesCommand = null;
		String columnFamilyName = null;
		boolean insertIntoCassandraColumnFamilySuccessFlag = false;
	
		
		if (childColumnFamilyName != null) {
			columnFamilyName = childColumnFamilyName + "_By_" + rootColumnFamilyName;
		} else {
			columnFamilyName = rootColumnFamilyName;
		}

		CassandraDatabaseColumnFamily cassandraColumnFamily = new CassandraDatabaseColumnFamily();
		Session dbsession = null;
		try {
			dbsession= cluster.connect(keySpaceName);
		} catch (Exception e) {
			System.out.println("Error in Cassandra create Column Family in writeMapIntoCassandraDatabase method" + e.getMessage());
		}

		BoundStatement insertStatement= dbsession.prepare(boundStatement).bind();
		BoundStatement stmt = insertStatement;
		
		//System.out.println(boundStatement);
		//System.out.println(stmt.toString());
		stmt.setUUID(columnFamilyName + "_id", UUID.randomUUID());
		Iterator jsonIterator = jsonObject.entrySet().iterator();

	
		while (jsonIterator.hasNext()) {
				Map.Entry e = (Map.Entry) jsonIterator.next();

				
				//System.out.println(e.getKey().toString()+" "+e.getValue());
				String datatype = cassandraDataTypeFinder(columnDetails.get(e.getKey()));
				
				//System.out.println(e.getKey()+" "+e.getValue());
				if(e.getValue()!=null)
				{
					if(datatype.equalsIgnoreCase("int")) {
					//	stmt.setLong(e.getKey().toString(), (Long)e.getValue());
						stmt.setInt(e.getKey().toString(), Integer.valueOf(e.getValue().toString()));
						
					}else if ((datatype.equalsIgnoreCase("text")) || (datatype.equalsIgnoreCase("varchar"))||  (datatype.equalsIgnoreCase("JSONArray"))){
						stmt.setString(e.getKey().toString(), (String)e.getValue()); 	
					}else if (datatype.equalsIgnoreCase("double")){
						stmt.setDouble(e.getKey().toString(), (Double)e.getValue()); 	
					}else if (datatype.equalsIgnoreCase("float")){
						stmt.setFloat(e.getKey().toString(), (Float)e.getValue()); 	
					}else if (datatype.equalsIgnoreCase("boolean")){
						stmt.setBool(e.getKey().toString(), (Boolean)e.getValue()); 	
					}else if (datatype.equalsIgnoreCase("timestamp")){
						stmt.setDate(e.getKey().toString(), (Date)e.getValue()); 	
					}else if (datatype.equalsIgnoreCase("blob")){
						stmt.setBytes(e.getKey().toString(),ByteBuffer.wrap(e.getValue().toString().getBytes())); 	
					}else if (datatype.equalsIgnoreCase("bigint")){
						//stmt.setLong(e.getKey().toString(), (Long)e.getValue());	
						stmt.setInt(e.getKey().toString(), (int)e.getValue());
						stmt.setInt(e.getKey().toString(), Integer.valueOf(e.getValue().toString()));
					}
				}
				else if(e.getValue()==null)
				{
					if(datatype.equalsIgnoreCase("int")) {
						stmt.setLong(e.getKey().toString(), 0);
					}else if ((datatype.equalsIgnoreCase("text")) || (datatype.equalsIgnoreCase("varchar"))||  (datatype.equalsIgnoreCase("JSONArray"))){
						stmt.setString(e.getKey().toString(), " "); 	
					}else if (datatype.equalsIgnoreCase("double")){
						stmt.setDouble(e.getKey().toString(), 0); 	
					}else if (datatype.equalsIgnoreCase("float")){
						stmt.setFloat(e.getKey().toString(), 0); 	
					}else if (datatype.equalsIgnoreCase("boolean")){
						stmt.setBool(e.getKey().toString(), false); 	
					}else if (datatype.equalsIgnoreCase("timestamp")){
						stmt.setDate(e.getKey().toString(), new Date()); 	
					}else if (datatype.equalsIgnoreCase("blob")){
						String encodedstring ="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";
						stmt.setBytes(e.getKey().toString(),ByteBuffer.wrap(encodedstring.getBytes())); 	
					}else if (datatype.equalsIgnoreCase("bigint")){
						stmt.setLong(e.getKey().toString(), 0);	
						
					}
				}
				//System.out.println()
		}
		
		
		try{
			//System.out.println(stmt.toString());
			
			ResultSet result = dbsession.execute(stmt);
			
			insertIntoCassandraColumnFamilySuccessFlag =true;
		}
		catch(Exception e)
		{
			System.out.println("Error in executing statement :"+e.getMessage());
			insertIntoCassandraColumnFamilySuccessFlag=false;
		}
		
		return insertIntoCassandraColumnFamilySuccessFlag;
	}
	public String cassandraDataTypeFinder(String columnDataType) {

		String cassandradatatype = "text";

		if (columnDataType == null) {
			cassandradatatype = "text";
		} else if (columnDataType.equalsIgnoreCase("int")) {
			cassandradatatype = "bigint";
		} else if ( (columnDataType.equalsIgnoreCase("text")) || (columnDataType.equalsIgnoreCase("mediumtext"))) {
			cassandradatatype = "text";
		} else if ( (columnDataType.equalsIgnoreCase("blob")) || (columnDataType.equalsIgnoreCase("mediumblob"))) {
			cassandradatatype = "blob";
		} else if (columnDataType.equalsIgnoreCase("bigint")) {
			cassandradatatype = "bigint";
		} else if (columnDataType.equalsIgnoreCase("date")) {
			cassandradatatype = "timestamp";
		} else if (columnDataType.equalsIgnoreCase("long")) {
			cassandradatatype = "bigint";
		} else if (columnDataType.equalsIgnoreCase("double")) {
			cassandradatatype = "double";
		} else if (columnDataType.equalsIgnoreCase("float")) {
			cassandradatatype = "float";
		} else if (columnDataType.equalsIgnoreCase("smallint")) {
			cassandradatatype = "int";
		} else if (columnDataType.equalsIgnoreCase("varchar")) {
			cassandradatatype = "varchar";
		}
		//System.out.println(columnDataType + " "+cassandradatatype);
		return cassandradatatype;
	}

	private void getColumnDetails(String sourceSchemaName, String sourceTableName,String childColumnFamilyName)
	{
		try {

			
			MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();
			//parentTablePrimaryKey = getPrimayKeyColumnDetails(conn, sourceSchemaName, sourceTableName);
			Map<String, String> parentcolumnDetails = new HashMap<String,String>();;
			parentcolumnDetails = getColumnDetails(conn, sourceSchemaName, sourceTableName);
			Map<String, String> childcolumnDetails =  null;
			if (childColumnFamilyName != null) {
				childcolumnDetails=new HashMap<String,String>();
				//childTablePrimaryKey = getPrimayKeyColumnDetails(conn, sourceSchemaName, childColumnFamilyName);
				childcolumnDetails=getColumnDetails(conn, sourceSchemaName, childColumnFamilyName);
			}
	
			columnDetails.putAll(parentcolumnDetails);
			if(childcolumnDetails!=null)
			{
				columnDetails.putAll(childcolumnDetails);
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	private Map<String, String> getColumnDetails(Connection conn, String schemaName, String tableName) {

		String primaryKeyName = null;
		MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();

		Map<String, String> coulmndetailsMap = mysqlTableColumnDetails.getMysqlTableColumnDetails(conn, schemaName,
				tableName);
		
		return coulmndetailsMap;
		
	}
	private boolean blobColumnFinder(Connection conn, String schemaName, String tableName) {

		String primaryKeyName = null;
		System.out.println("Searching for Blob column in :"+tableName);
		MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();

		Map<String, String> coulmndetailsMap = mysqlTableColumnDetails.getMysqlTableColumnDetails(conn, schemaName,
				tableName);
		for (Map.Entry<String, String> e : coulmndetailsMap.entrySet()) {
			//System.out.println("Key : " + e.getKey() + " Value : " + e.getValue());
			if(e.getValue()!=null)
			{
				//System.out.println(e.getKey().toString()+ e.getValue().toString());
				if(e.getValue().toString().equals("blob") || e.getValue().toString().equals("mediumblob") ||e.getValue().toString().equals("bigblob"))
				{
					System.out.println("Blob column found in :"+tableName);
					return true;
				}
			}
		}
		
		return false;
	}
}
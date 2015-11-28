package com.data.mig.mysql.extract;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.io.file.FileIoUtils;
import com.data.mig.json.mapper.JsonNodeRowMapper;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableColumnDetails;

public class MysqlDataExtract {

	public boolean extractMysqlDataIntoJsonFile(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted, String filePath) throws SQLException {

		System.out.println("### Start of extract and file write ###");
		boolean extractStatus = false;

		Map<String, Object> targetObject = extractMysqlDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted);

		FileIoUtils fileIoUtils = new FileIoUtils();
		extractStatus = fileIoUtils.write(targetObject, filePath);
		
		System.out.println("### End of extract and file write ###");

		return extractStatus;

	}

	public boolean subsequentExtractMysqlDataIntoJsonFile(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted, String filePath, Map<String, String> primaryColumnValues)
					throws SQLException {

		System.out.println("### Start of mysql extract process ###");
		boolean extractStatus = false;

		Map<String, Object> targetObject = subsequentExtractMysqlDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted, primaryColumnValues);

		FileIoUtils fileIoUtils = new FileIoUtils();
		extractStatus = fileIoUtils.write(targetObject, filePath);
		
		System.out.println("### End of mysql extract process ###");

		return extractStatus;

	}

	public Map<String, Object> subsequentExtractMysqlDataIntoObject(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted, Map<String, String> primaryColumnValues) throws SQLException {

		System.out.println("### Start of mysql subsequent extract process into object ###");
		ResultSet parentTableResultSet = null;
		Statement parentTableStatement = null;

		Map<String, Object> targetObject = new HashMap<String, Object>();

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			Map<String, String> parentTableColumnDetails = getColumnDetails(conn, schemaName, parentTableName);

			// Map<String, String> primaryKeyOfTableMap =
			// getPrimaryColumnDetails(conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = constructSelectQueryForSubsequentExtract(schemaName,parentTableName,
					parentTableColumnDetails, noOfRecordsToBeExtracted, primaryColumnValues);

			if (parentTableSelectQuery != null) {
				parentTableStatement = conn.createStatement();
				parentTableResultSet = parentTableStatement.executeQuery(parentTableSelectQuery.toString());

				if (parentTableResultSet != null) {

					while (parentTableResultSet.next()) {
						jsonNode = new JsonNodeRowMapper(objectMapper).mapRow(parentTableResultSet,
								parentTableResultSet.getRow());
						targetObject.put(parentTableName + parentTableResultSet.getRow(), jsonNode);

					}
					System.out.println("No of records in target object :" + targetObject.size());

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}

		System.out.println("### End of mysql subsequent extract process into object ###");
		return targetObject;

	}

	public Map<String, Object> extractMysqlDataIntoObject(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted) throws SQLException {
		
		System.out.println("### Start of mysql extract process into object ###");

		ResultSet parentTableResultSet = null;
		Statement parentTableStatement = null;

		Map<String, Object> targetObject = new HashMap<String, Object>();

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			Map<String, String> parentTableColumnDetails = getColumnDetails(conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = constructSelectQuery(parentTableName, parentTableColumnDetails,
					noOfRecordsToBeExtracted);

			if (parentTableSelectQuery != null) {
				parentTableStatement = conn.createStatement();
				parentTableResultSet = parentTableStatement.executeQuery(parentTableSelectQuery.toString());

				if (parentTableResultSet != null) {

					while (parentTableResultSet.next()) {
						jsonNode = new JsonNodeRowMapper(objectMapper).mapRow(parentTableResultSet,
								parentTableResultSet.getRow());
						targetObject.put(parentTableName + parentTableResultSet.getRow(), jsonNode);

					}
					System.out.println("No of records in target object :" + targetObject.size());

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}
		
		System.out.println("### End of mysql extract process into object ###");

		return targetObject;

	}

	private Connection getDatabaseConnection(String schemaName) {

		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		return mysqlDatabaseConnect.getMySqlDBConnection(schemaName, IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

	}

	private Map<String, String> getColumnDetails(Connection conn, String schemaName, String tableName) {

		MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();

		return mysqlTableColumnDetails.getMysqlTableColumnDetails(conn, schemaName, tableName);

	}

	private StringBuilder constructSelectQuery(String tableName, Map<String, String> tableColumnDetailsMap,
			Long limitNoOfRows) {

		StringBuilder query = null;

		if (tableColumnDetailsMap != null) {

			for (Map.Entry<String, String> entry : tableColumnDetailsMap.entrySet()) {

				if (query == null) {
					query = new StringBuilder();
					query.append("select ");
					query.append(entry.getKey());
				} else {
					query.append(", " + entry.getKey());
				}

			}

			if (query != null) {
				query.append(" from  ");
				query.append(tableName);
				query.append(" limit ");
				query.append(limitNoOfRows);

			}

		}

		return query;

	}

	private StringBuilder constructSelectQueryForSubsequentExtract(String schemaName, String tableName,
			Map<String, String> tableColumnDetailsMap, Long limitNoOfRows, Map<String, String> primaryKeyOfTableMap) {

		StringBuilder query = null;

		if (tableColumnDetailsMap != null) {

			for (Map.Entry<String, String> entry : tableColumnDetailsMap.entrySet()) {

				if (query == null) {
					query = new StringBuilder();
					query.append("select ");
					query.append(entry.getKey());
				} else {
					query.append(", " + entry.getKey());
				}

			}

			if (query != null) {
				query.append(" from  ");
				query.append(schemaName);
				query.append(".");
				query.append(tableName);
			}

			if (primaryKeyOfTableMap != null) {
				
				int noOfPrimaryKeys = 0;
				
				query.append(" where ");
				for (Map.Entry<String, String> primaryKeyColumns : primaryKeyOfTableMap.entrySet()) {
					
					noOfPrimaryKeys++;
					
					query.append(primaryKeyColumns.getKey());
					query.append(" > ");
					query.append("'");
					query.append(primaryKeyColumns.getValue());
					query.append("'");
					
					if (noOfPrimaryKeys != primaryKeyOfTableMap.size()) {
						query.append(" and ");	
					}
					
				}
				
				query.append(" order by ");
				
				noOfPrimaryKeys = 0;
				for (Map.Entry<String, String> primaryKeyColumns : primaryKeyOfTableMap.entrySet()) {
					
					noOfPrimaryKeys++;
					query.append(primaryKeyColumns.getKey());
					if (noOfPrimaryKeys != primaryKeyOfTableMap.size()) {
						query.append(" , ");	
					}
					
				}

			}

			if (query != null) {
				query.append(" limit ");
				query.append(limitNoOfRows);

			}

		}

		System.out.println("Extract query : " + query.toString());
		return query;

	}

}

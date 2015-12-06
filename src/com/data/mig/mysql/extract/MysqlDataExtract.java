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

	private MysqlDataExtractQueryUtils mysqlDataExtractQueryUtils = new MysqlDataExtractQueryUtils();

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

		System.out.println("### Start of mysql subsequent extract process ###");
		boolean extractStatus = false;

		Map<String, Object> targetObject = subsequentExtractMysqlDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted, primaryColumnValues);

		FileIoUtils fileIoUtils = new FileIoUtils();
		extractStatus = fileIoUtils.write(targetObject, filePath);

		System.out.println("### End of mysql subsequent extract process ###");

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

			StringBuilder parentTableSelectQuery = mysqlDataExtractQueryUtils.constructSelectQueryForSubsequentExtract(
					schemaName, parentTableName, parentTableColumnDetails, noOfRecordsToBeExtracted,
					primaryColumnValues);

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
		//JSONObject jsonObject = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			Map<String, String> parentTableColumnDetails = getColumnDetails(conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = mysqlDataExtractQueryUtils.constructSelectQuery(schemaName,
					parentTableName, parentTableColumnDetails, noOfRecordsToBeExtracted);

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


}

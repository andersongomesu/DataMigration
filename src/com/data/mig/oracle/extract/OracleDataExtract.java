package com.data.mig.oracle.extract;

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
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.db.OracleTableColumnDetails;

public class OracleDataExtract {

	private OracleDataExtractQueryUtils oracleDataExtractQueryUtils = new OracleDataExtractQueryUtils();

	public boolean extractOracleDataIntoJsonFile(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted, String filePath) throws SQLException {

		System.out.println("### Start of extract and file write ###");
		boolean extractStatus = false;

		Map<String, Object> targetObject = extractOracleDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted);

		FileIoUtils fileIoUtils = new FileIoUtils();
		extractStatus = fileIoUtils.write(targetObject, filePath);

		System.out.println("### End of extract and file write ###");

		return extractStatus;

	}

	public boolean subsequentExtractOracleDataIntoJsonFile(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted, String filePath, Map<String, String> primaryColumnValues)
					throws SQLException {

		System.out.println("### Start of Oracle subsequent extract process ###");
		boolean extractStatus = false;

		Map<String, Object> targetObject = subsequentExtractOracleDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted, primaryColumnValues);

		FileIoUtils fileIoUtils = new FileIoUtils();
		extractStatus = fileIoUtils.write(targetObject, filePath);

		System.out.println("### End of Oracle subsequent extract process ###");

		return extractStatus;

	}

	public Map<String, Object> subsequentExtractOracleDataIntoObject(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted, Map<String, String> primaryColumnValues) throws SQLException {

		System.out.println("### Start of Oracle subsequent extract process into object ###");
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

			StringBuilder parentTableSelectQuery = oracleDataExtractQueryUtils.constructSelectQueryForSubsequentExtract(
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

		System.out.println("### End of Oracle subsequent extract process into object ###");
		return targetObject;

	}

	public Map<String, Object> extractOracleDataIntoObject(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted) throws SQLException {

		System.out.println("### Start of Oracle extract process into object ###");

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

			StringBuilder parentTableSelectQuery = oracleDataExtractQueryUtils.constructSelectQuery(schemaName,
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

		System.out.println("### End of Oracle extract process into object ###");

		return targetObject;

	}

	private Connection getDatabaseConnection(String schemaName) {

		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		return oracleDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

	}

	private Map<String, String> getColumnDetails(Connection conn, String schemaName, String tableName) {

		OracleTableColumnDetails oracleTableColumnDetails = new OracleTableColumnDetails();

		return oracleTableColumnDetails.getOracleTableColumnDetails(conn, schemaName, tableName);

	}


}

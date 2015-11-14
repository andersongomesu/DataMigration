package com.data.mig.mysql.extract;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.node.ObjectNode;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.io.file.FileIoUtils;
import com.data.mig.json.mapper.JsonNodeRowMapper;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableColumnDetails;

public class MysqlDataExtract {

	public boolean extractMysqlDataIntoJsonFile(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted, String filePath) throws SQLException {

		boolean extractStatus = false;

		Map<String, Object> targetObject = extractMysqlDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted);
		
		FileIoUtils fileIoUtils = new FileIoUtils();
		extractStatus = fileIoUtils.write(targetObject, filePath);
		
		return extractStatus;

	}
	
	public Map<String, Object> extractMysqlDataIntoObject(String schemaName, String parentTableName,
			long noOfRecordsToBeExtracted) throws SQLException {

		ResultSet parentTableResultSet = null;
		Statement parentTableStatement = null;

		Map<String, Object> targetObject = new HashMap<String, Object>();

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			Map<String, String> parentTableColumnDetails = getColumnDetails(conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = constructSelectQuery(parentTableName, parentTableColumnDetails, noOfRecordsToBeExtracted);

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


}

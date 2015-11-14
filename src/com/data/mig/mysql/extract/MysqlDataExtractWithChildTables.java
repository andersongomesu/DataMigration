package com.data.mig.mysql.extract;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.json.mapper.JsonNodeRowMapper;
import com.data.mig.mysql.db.ChildTableDetails;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableColumnDetails;
import com.data.mig.mysql.db.MysqlTableRelationship;
import com.data.mig.mysql.db.TableDetails;

public class MysqlDataExtractWithChildTables {

	public boolean extractMysqlDataIntoJsonFile(String schemaName,
			String parentTableName, long noOfRecordsToBeExtracted,
			String filePath) throws SQLException {

		System.out.println("### Start of process ###");
		boolean extractStatus = false;

		ResultSet parentTableResultSet = null;
		Statement parentTableStatement = null;

		Map<String, Object> targetObject = new HashMap<String, Object>();
		
		List <TableDetails> childTableDetailsList = null; 
		List<ChildTableDetails> childTableDetailsDBQueryList = null;

		ObjectMapper objectMapper = new ObjectMapper();

		// ObjectNode objectNode = objectMapper.createObjectNode();

		JsonNode jsonNode = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();

			childTableDetailsList = mysqlTableRelationship
					.getMysqlTableRelationshipDetailsAsObject(null, schemaName,
							parentTableName);
			
			Map<String, String> parentTableColumnDetails = getColumnDetails(
					conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = constructSelectQuery(
					parentTableName, parentTableColumnDetails, 10L);

			if (parentTableSelectQuery != null) {
				parentTableStatement = conn.createStatement();
				
				//Execute the parent table select query
				parentTableResultSet = parentTableStatement
						.executeQuery(parentTableSelectQuery.toString());

				if (parentTableResultSet != null) {


					//Get the child table details
					if (childTableDetailsList != null) {
						childTableDetailsDBQueryList = new ArrayList<ChildTableDetails>();
						for (TableDetails childTableDetails : childTableDetailsList) {
							
							//Child table details in the object
							System.out.println("Child table name :"
									+ childTableDetails.getTableName());
							Map<String, String> childTableColumnDetailsMap = getColumnDetails(
									conn, schemaName, childTableDetails.getTableName());
							childTableDetailsDBQueryList.add(constructSelectQueryWithWhereClause(childTableDetailsList,
									childTableColumnDetailsMap,
									childTableDetails.getTableName(),
									conn));

						}
	
					}
					
					//Loop through the parent table result set
					while (parentTableResultSet.next()) {
						jsonNode = new JsonNodeRowMapper(objectMapper).mapRow(
								parentTableResultSet,
								parentTableResultSet.getRow());

						//Check whether child tables exists
						if (childTableDetailsList != null) {
							ArrayNode childArrayNode = null;
							for (ChildTableDetails childTableDetails : childTableDetailsDBQueryList) {
								
								int columnIndex = 0;
								
								//Set the child table keys
								for (Map.Entry<String, String> keyColumnNameAndDataType: childTableDetails.getKeyColumnNameAndDataType().entrySet()) {
									
									if (keyColumnNameAndDataType.getValue() != null && keyColumnNameAndDataType.getValue().equalsIgnoreCase("int")) {
										childTableDetails.getPreparedStatement().setInt(++columnIndex, Integer.valueOf(jsonNode.get(keyColumnNameAndDataType.getKey()).toString()));	
									} else if (keyColumnNameAndDataType.getValue() != null && keyColumnNameAndDataType.getValue().equalsIgnoreCase("String")) {
										childTableDetails.getPreparedStatement().setString(++columnIndex, jsonNode.get(keyColumnNameAndDataType.getKey()).toString());
									}
										
								}
								
								//Execute the child table query
								ResultSet childTableResultSet = childTableDetails.getPreparedStatement().executeQuery();
								
								
								
								if (childTableResultSet != null) {
									
									childArrayNode = objectMapper.createArrayNode();
									
									//Loop through the child table result
									while (childTableResultSet.next()) {
										
										JsonNode childJsonNode = new JsonNodeRowMapper(objectMapper).mapRow(
												childTableResultSet,
												childTableResultSet.getRow());
										
										
										//Add the child table records to array
										childArrayNode.add(childJsonNode);
									}
									
								}
								
								//Add the child table array into main node
								((ObjectNode) jsonNode).put(childTableDetails.getTableName(), childArrayNode);
							}
							
						}

						//Put the node into target object
						targetObject
								.put(parentTableName
										+ parentTableResultSet.getRow(),
										jsonNode);

					}
					System.out.println("No of records :" + targetObject.size());
					
					//Write the target object into file
					objectMapper
							.writeValue(
									new File(
											"D:\\Sampath\\MS\\Dissertation\\MySQL\\extract.json"),
									targetObject);

				}

				conn.close();
				extractStatus = true;		
			}

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}

		System.out.println("### End of process ###");
		return extractStatus;

	}

	private Connection getDatabaseConnection(String schemaName) {

		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		return mysqlDatabaseConnect.getMySqlDBConnection(schemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

	}

	private Map<String, String> getColumnDetails(Connection conn,
			String schemaName, String tableName) {

		MysqlTableColumnDetails mysqlTableColumnDetails = new MysqlTableColumnDetails();

		return mysqlTableColumnDetails.getMysqlTableColumnDetails(conn,
				schemaName, tableName);

	}

	private StringBuilder constructSelectQuery(String tableName,
			Map<String, String> tableColumnDetailsMap, Long limitNoOfRows) {

		
		StringBuilder query = null;

		if (tableColumnDetailsMap != null) {

			for (Map.Entry<String, String> entry : tableColumnDetailsMap
					.entrySet()) {

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

	private ChildTableDetails constructSelectQueryWithWhereClause(
			List <TableDetails> childTableDetailsList,
			Map<String, String> tableColumnDetailsMap, String tableName,
			Connection conn) {

		ChildTableDetails childTableDetailsDBQuery = null;
		
		Map<String, String> keyColumnNameAndDataType = null;

		StringBuilder query = null;

		if (tableColumnDetailsMap != null) {

			for (Map.Entry<String, String> entry : tableColumnDetailsMap
					.entrySet()) {

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
				query.append(" where ");

			}

			int noOfKeyColumns = 0;
			if (query != null) {

				for (TableDetails childTableDetails : childTableDetailsList) {
					if (childTableDetails.getTableName() != null
							&& childTableDetails.getTableName().equals(tableName)) {
						if (noOfKeyColumns == 0) {
							// query.append(childTableKey.getKey().substring(childTableKey.getKey().indexOf(".")));
							query.append(childTableDetails.getColumnName());
							query.append(" = ? ");
							
							keyColumnNameAndDataType = new LinkedHashMap<String, String>();
							
						} else {
							query.append(" and ");
							query.append(childTableDetails.getColumnName());
							query.append(" = ? ");
							
						}
						keyColumnNameAndDataType.put(childTableDetails.getColumnName(), childTableDetails.getDataType());
						noOfKeyColumns++;
					}

				}

				try {

					System.out.println("Child table query string :"
							+ query.toString());

					PreparedStatement preparedStatement = conn
							.prepareStatement(query.toString());

					childTableDetailsDBQuery = new ChildTableDetails();

					childTableDetailsDBQuery.setPreparedStatement(preparedStatement);
					childTableDetailsDBQuery.setTableName(tableName);
					childTableDetailsDBQuery.setQueryString(query.toString());
					childTableDetailsDBQuery.setKeyColumnNameAndDataType(keyColumnNameAndDataType);
					childTableDetailsDBQuery.setNoOfKeyFields(noOfKeyColumns);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return childTableDetailsDBQuery;

	}

}

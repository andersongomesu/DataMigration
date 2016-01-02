package com.data.mig.oracle.extract;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.io.file.FileIoUtils;
import com.data.mig.json.mapper.JsonNodeRowMapper;
import com.data.mig.mysql.db.ChildTableDetails;
import com.data.mig.mysql.db.TableDetails;
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.db.OracleTableColumnDetails;
import com.data.mig.oracle.db.OracleTableRelationship;

public class OracleDataExtractWithChildTables {

	private OracleDataExtractQueryUtils oracleDataExtractQueryUtils = new OracleDataExtractQueryUtils();

	public Boolean extractOracleDataIntoJsonFile(String schemaName, String parentTableName,
			Long noOfRecordsToBeExtracted, String filePath) throws SQLException {

		System.out.println("### Start of extract and file write ###");
		Boolean extractStatus = false;

		Map<String, Object> targetObject = extractOracleDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted);

		FileIoUtils fileIoUtils = null;

		if (targetObject != null) {

			fileIoUtils = new FileIoUtils();

			extractStatus = fileIoUtils.write(targetObject, filePath);
		}

		System.out.println("### End of extract and file write ###");
		return extractStatus;
	}

	public Boolean extractOracleDataIntoJsonFile(String schemaName, String parentTableName, String childTableName,
			Long noOfRecordsToBeExtracted, String filePath) throws SQLException {

		System.out.println("### Start of extract and file write ###");
		Boolean extractStatus = false;

		Map<String, Object> targetObject = extractWithGivenChildOracleDataIntoObject(schemaName, parentTableName,
				childTableName, noOfRecordsToBeExtracted);

		FileIoUtils fileIoUtils = null;

		if (targetObject != null) {

			fileIoUtils = new FileIoUtils();

			extractStatus = fileIoUtils.write(targetObject, filePath);
		}

		System.out.println("### End of extract and file write ###");
		return extractStatus;
	}

	public Boolean subsequentExtractOracleDataIntoJsonFile(String schemaName, String parentTableName,
			Long noOfRecordsToBeExtracted, String filePath, Map<String, String> primaryColumnValues)
					throws SQLException {

		System.out.println("### Start of subsequent extract and file write ###");
		Boolean extractStatus = false;

		Map<String, Object> targetObject = subsequentExtractOracleDataIntoObject(schemaName, parentTableName,
				noOfRecordsToBeExtracted, primaryColumnValues);

		FileIoUtils fileIoUtils = null;

		if (targetObject != null) {

			fileIoUtils = new FileIoUtils();

			extractStatus = fileIoUtils.write(targetObject, filePath);
		}

		System.out.println("### End of subsequent extract and file write ###");
		return extractStatus;
	}

	public Map<String, Object> subsequentExtractOracleDataIntoObject(String schemaName, String parentTableName,
			Long noOfRecordsToBeExtracted, Map<String, String> primaryColumnValues) throws SQLException {

		System.out.println("### Start of Oracle subsequent extract process ###");

		ResultSet parentTableResultSet = null;
		Statement parentTableStatement = null;

		Map<String, Object> targetObject = new HashMap<String, Object>();

		List<TableDetails> childTableDetailsList = null;
		List<ChildTableDetails> childTableDetailsDBQueryList = null;

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			OracleTableRelationship oracleTableRelationship = new OracleTableRelationship();

			childTableDetailsList = oracleTableRelationship.getOracleTableRelationshipDetailsAsObject(null, schemaName,
					parentTableName);

			Map<String, String> parentTableColumnDetails = getColumnDetails(conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = oracleDataExtractQueryUtils.constructSelectQueryForSubsequentExtract(
					schemaName, parentTableName, parentTableColumnDetails, noOfRecordsToBeExtracted,
					primaryColumnValues);

			if (parentTableSelectQuery != null) {
				parentTableStatement = conn.createStatement();

				// Execute the parent table select query
				parentTableResultSet = parentTableStatement.executeQuery(parentTableSelectQuery.toString());

				if (parentTableResultSet != null) {

					// Get the child table details
					if (childTableDetailsList != null) {
						childTableDetailsDBQueryList = new ArrayList<ChildTableDetails>();
						for (TableDetails childTableDetails : childTableDetailsList) {

							// Child table details in the object
							System.out.println("Child table name :" + childTableDetails.getTableName());
							Map<String, String> childTableColumnDetailsMap = getColumnDetails(conn, schemaName,
									childTableDetails.getTableName());
							childTableDetailsDBQueryList.add(
									oracleDataExtractQueryUtils.constructSelectQueryWithForeignKeyColumnsInWhereClause(
											childTableDetailsList, childTableColumnDetailsMap,
											childTableDetails.getTableName(), schemaName, conn));

						}

					}

					// Loop through the parent table result set
					while (parentTableResultSet.next()) {
						jsonNode = new JsonNodeRowMapper(objectMapper).mapRow(parentTableResultSet,
								parentTableResultSet.getRow());

						// Check whether child tables exists
						if (childTableDetailsList != null) {
							ArrayNode childArrayNode = null;
							for (ChildTableDetails childTableDetails : childTableDetailsDBQueryList) {

								int columnIndex = 0;

								// Set the child table keys
								for (Map.Entry<String, String> keyColumnNameAndDataType : childTableDetails
										.getKeyColumnNameAndDataType().entrySet()) {

									if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("int")) {
										String key = keyColumnNameAndDataType.getKey();
										if (key.equals("salesRepEmployeeNumber")) {
											childTableDetails.getPreparedStatement().setInt(++columnIndex,
													Integer.valueOf(jsonNode.get("employeeNumber").toString()));
										} else {
											childTableDetails.getPreparedStatement().setInt(++columnIndex,
													Integer.valueOf(jsonNode.get(keyColumnNameAndDataType.getKey())
															.toString()));
										}
									} else if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("String")) {
										childTableDetails.getPreparedStatement().setString(++columnIndex,
												jsonNode.get(keyColumnNameAndDataType.getKey()).toString());
									} else if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("varchar")) {
										System.out.println("Key is :"
												+ jsonNode.get(keyColumnNameAndDataType.getKey()).toString());
										childTableDetails.getPreparedStatement().setString(++columnIndex,
												jsonNode.get(keyColumnNameAndDataType.getKey()).getTextValue());
									}

								}

								// Execute the child table query
								ResultSet childTableResultSet = childTableDetails.getPreparedStatement().executeQuery();

								if (childTableResultSet != null) {

									childArrayNode = objectMapper.createArrayNode();

									// Loop through the child table result
									while (childTableResultSet.next()) {

										JsonNode childJsonNode = new JsonNodeRowMapper(objectMapper)
												.mapRow(childTableResultSet, childTableResultSet.getRow());

										// Add the child table records to array
										childArrayNode.add(childJsonNode);
									}

								}

								// Add the child table array into main node
								((ObjectNode) jsonNode).put(childTableDetails.getTableName(), childArrayNode);
							}

						}

						// Put the node into target object
						targetObject.put(parentTableName + parentTableResultSet.getRow(), jsonNode);

					}
					System.out.println("No of records in target object :" + targetObject.size());

				}

				conn.close();
			}

		} catch (SQLException e) {

			System.out.println("SQL exception code :" + e.getErrorCode());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}

		System.out.println("### End of Oracle subsequent extract process ###");

		return targetObject;

	}

	public Map<String, Object> extractOracleDataIntoObject(String schemaName, String parentTableName,
			Long noOfRecordsToBeExtracted) throws SQLException {

		System.out.println("### Start of Oracle extract process ###");

		ResultSet parentTableResultSet = null;
		Statement parentTableStatement = null;

		Map<String, Object> targetObject = new HashMap<String, Object>();

		List<TableDetails> childTableDetailsList = null;
		List<ChildTableDetails> childTableDetailsDBQueryList = null;

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			OracleTableRelationship oracleTableRelationship = new OracleTableRelationship();

			childTableDetailsList = oracleTableRelationship.getOracleTableRelationshipDetailsAsObject(null, schemaName,
					parentTableName);

			Map<String, String> parentTableColumnDetails = getColumnDetails(conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = oracleDataExtractQueryUtils.constructSelectQuery(schemaName,
					parentTableName, parentTableColumnDetails, noOfRecordsToBeExtracted);

			if (parentTableSelectQuery != null) {
				parentTableStatement = conn.createStatement();

				// Execute the parent table select query
				parentTableResultSet = parentTableStatement.executeQuery(parentTableSelectQuery.toString());

				if (parentTableResultSet != null) {

					// Get the child table details
					if (childTableDetailsList != null) {
						childTableDetailsDBQueryList = new ArrayList<ChildTableDetails>();
						for (TableDetails childTableDetails : childTableDetailsList) {

							// Child table details in the object
							System.out.println("Child table name :" + childTableDetails.getTableName());
							Map<String, String> childTableColumnDetailsMap = getColumnDetails(conn, schemaName,
									childTableDetails.getTableName());
							childTableDetailsDBQueryList.add(
									oracleDataExtractQueryUtils.constructSelectQueryWithForeignKeyColumnsInWhereClause(
											childTableDetailsList, childTableColumnDetailsMap,
											childTableDetails.getTableName(), schemaName, conn));

						}

					}

					// Loop through the parent table result set
					while (parentTableResultSet.next()) {
						jsonNode = new JsonNodeRowMapper(objectMapper).mapRow(parentTableResultSet,
								parentTableResultSet.getRow());

						// Check whether child tables exists
						if (childTableDetailsList != null) {
							ArrayNode childArrayNode = null;
							for (ChildTableDetails childTableDetails : childTableDetailsDBQueryList) {

								int columnIndex = 0;

								// Set the child table keys
								for (Map.Entry<String, String> keyColumnNameAndDataType : childTableDetails
										.getKeyColumnNameAndDataType().entrySet()) {

									if (keyColumnNameAndDataType.getValue()	 != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("NUMBER")) {
										String key = keyColumnNameAndDataType.getKey();
										if (key.equals("salesRepEmployeeNumber")) {
											childTableDetails.getPreparedStatement().setInt(++columnIndex,
													Integer.valueOf(jsonNode.get("employeeNumber").toString()));
										} else {
											childTableDetails.getPreparedStatement().setInt(++columnIndex,
													Integer.valueOf(jsonNode.get(keyColumnNameAndDataType.getKey())
															.toString()));
										}

									} else if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("String")) {
										childTableDetails.getPreparedStatement().setString(++columnIndex,
												jsonNode.get(keyColumnNameAndDataType.getKey()).toString());
									} else if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("varchar2")) {
										System.out.println("Key is :"
												+ jsonNode.get(keyColumnNameAndDataType.getKey()).toString());
										childTableDetails.getPreparedStatement().setString(++columnIndex,
												jsonNode.get(keyColumnNameAndDataType.getKey()).getTextValue());
									}

								}

								// Execute the child table query
								ResultSet childTableResultSet = childTableDetails.getPreparedStatement().executeQuery();

								if (childTableResultSet != null) {

									childArrayNode = objectMapper.createArrayNode();

									// Loop through the child table result
									while (childTableResultSet.next()) {

										JsonNode childJsonNode = new JsonNodeRowMapper(objectMapper)
												.mapRow(childTableResultSet, childTableResultSet.getRow());

										// Add the child table records to array
										childArrayNode.add(childJsonNode);
									}

								}

								// Add the child table array into main node
								((ObjectNode) jsonNode).put(childTableDetails.getTableName(), childArrayNode);
							}

						}

						// Put the node into target object
						targetObject.put(parentTableName + parentTableResultSet.getRow(), jsonNode);

					}
					System.out.println("No of records in target object :" + targetObject.size());

				}

				conn.close();
			}

		} catch (SQLException e) {

			System.out.println("SQL exception code :" + e.getErrorCode());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}

		System.out.println("### End of Oracle extract process ###");

		return targetObject;

	}

	// new
	public Map<String, Object> extractWithGivenChildOracleDataIntoObject(String schemaName, String parentTableName,
			String childTableName, Long noOfRecordsToBeExtracted) throws SQLException {

		System.out.println("### Start of Oracle extract process ###");

		ResultSet parentTableResultSet = null;
		Statement parentTableStatement = null;

		Map<String, Object> targetObject = new HashMap<String, Object>();

		List<TableDetails> childTableDetailsList = null;
		List<ChildTableDetails> childTableDetailsDBQueryList = null;

		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		Connection conn = null;

		try {
			conn = getDatabaseConnection(schemaName);

			OracleTableRelationship OracleTableRelationship = new OracleTableRelationship();

			// OracleTableColumnDetails OracleTableColumnDetails = new
			// OracleTableColumnDetails();

			childTableDetailsList = OracleTableRelationship.getOracleTableRelationshipDetailsAsObject(null, schemaName,
					parentTableName);

			// Map<String, String> parentTablePrimaryKeyMap =
			// OracleTableColumnDetails.getOracleTablePrimaryKey(conn, schemaName,
			// parentTableName);

			Map<String, String> parentTableColumnDetails = getColumnDetails(conn, schemaName, parentTableName);

			StringBuilder parentTableSelectQuery = oracleDataExtractQueryUtils.constructSelectQuery(schemaName,
					parentTableName, parentTableColumnDetails, noOfRecordsToBeExtracted);

			if (parentTableSelectQuery != null) {
				parentTableStatement = conn.createStatement();

				// Execute the parent table select query
				parentTableResultSet = parentTableStatement.executeQuery(parentTableSelectQuery.toString());

				if (parentTableResultSet != null) {

					// Get the child table details
					if (childTableDetailsList != null) {
						childTableDetailsDBQueryList = new ArrayList<ChildTableDetails>();
						for (TableDetails childTableDetails : childTableDetailsList) {

							if (childTableDetails.getTableName().equalsIgnoreCase(childTableName)) {
								// Child table details in the object
								System.out.println("Child table name :" + childTableDetails.getTableName());
								Map<String, String> childTableColumnDetailsMap = getColumnDetails(conn, schemaName,
										childTableDetails.getTableName());
								childTableColumnDetailsMap.putAll(parentTableColumnDetails);
								childTableDetailsDBQueryList
										.add(oracleDataExtractQueryUtils.constructSelectQueryWithChildTablesUsingJoin(
												childTableDetailsList, childTableColumnDetailsMap, parentTableName,
												childTableDetails.getTableName(), schemaName, conn));
							}
						}

					}

					// Loop through the parent table result set
					while (parentTableResultSet.next()) {
						jsonNode = new JsonNodeRowMapper(objectMapper).mapRow(parentTableResultSet,
								parentTableResultSet.getRow());

						// Check whether child tables exists
						if (childTableDetailsList != null) {
							ArrayNode childArrayNode = null;
							for (ChildTableDetails childTableDetails : childTableDetailsDBQueryList) {

								int columnIndex = 0;

								// Set the child table keys
								for (Map.Entry<String, String> keyColumnNameAndDataType : childTableDetails
										.getKeyColumnNameAndDataType().entrySet()) {

									if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("int")) {
										String key = keyColumnNameAndDataType.getKey();
										if (key.equals("salesRepEmployeeNumber")) {
											childTableDetails.getPreparedStatement().setInt(++columnIndex,
													Integer.valueOf(jsonNode.get("employeeNumber").toString()));
										} else {
											childTableDetails.getPreparedStatement().setInt(++columnIndex,
													Integer.valueOf(jsonNode.get(keyColumnNameAndDataType.getKey())
															.toString()));
										}

									} else if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("String")) {
										childTableDetails.getPreparedStatement().setString(++columnIndex,
												jsonNode.get(keyColumnNameAndDataType.getKey()).toString());
									} else if (keyColumnNameAndDataType.getValue() != null
											&& keyColumnNameAndDataType.getValue().equalsIgnoreCase("varchar")) {
										System.out.println("Key is :"
												+ jsonNode.get(keyColumnNameAndDataType.getKey()).toString());
										childTableDetails.getPreparedStatement().setString(++columnIndex,
												jsonNode.get(keyColumnNameAndDataType.getKey()).getTextValue());

									}

								}

								System.out.println(childTableDetails.getPreparedStatement().toString());

								/*
								 * PreparedStatement preparedStatement =
								 * conn.prepareStatement(
								 * "select MSRP, productScale, quantityInStock, productDescription ,productlines.productLine as  'productLine', productCode, buyPrice, "
								 * +
								 * "image, productVendor, htmlDescription, productName, textDescription from  classicmodels.productlines, classicmodels.products "
								 * +
								 * "where products.productLine = productlines.productLine and productlines.productLine = ? "
								 * );
								 * 
								 * preparedStatement.setString(1, "Classic Cars"
								 * );
								 */

								// Execute the child table query
								ResultSet childTableResultSet = childTableDetails.getPreparedStatement().executeQuery();

								if (childTableResultSet != null) {

									// System.out.println("Resultset fetch size
									// :" + childTableResultSet.getFetchSize());

									childArrayNode = objectMapper.createArrayNode();

									// Loop through the child table result
									while (childTableResultSet.next()) {

										JsonNode childJsonNode = new JsonNodeRowMapper(objectMapper)
												.mapRow(childTableResultSet, childTableResultSet.getRow());

										// Add the child table records to array
										childArrayNode.add(childJsonNode);
									}

								}

								// Add the child table array into main node
								// ((ObjectNode)
								// jsonNode).put(childTableDetails.getTableName(),
								// childArrayNode);
								// ((ObjectNode) jsonNode).put(parentTableName ,
								// childArrayNode);
								targetObject.put(parentTableName + parentTableResultSet.getRow(), childArrayNode);

							}

						}

						// Put the node into target object
						// targetObject.put(parentTableName +
						// parentTableResultSet.getRow(), jsonNode);
						// targetObject =(ObjectNode)jsonNode;

					}
					System.out.println("No of records in target object :" + targetObject.size());

				}

				conn.close();
			}

		} catch (SQLException e) {

			System.out.println("SQL exception code :" + e.getErrorCode());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}

		System.out.println("### End of Oracle extract process ###");

		return targetObject;

	}

	private Connection getDatabaseConnection(String schemaName) {

		OracleDatabaseConnect OracleDatabaseConnect = new OracleDatabaseConnect();

		return OracleDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

	}

	private Map<String, String> getColumnDetails(Connection conn, String schemaName, String tableName) {

		OracleTableColumnDetails OracleTableColumnDetails = new OracleTableColumnDetails();

		return OracleTableColumnDetails.getOracleTableColumnDetails(conn, schemaName, tableName);

	}

}

package com.data.mig.oracle.extract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.data.mig.mysql.db.ChildTableDetails;
import com.data.mig.mysql.db.TableDetails;

public class OracleDataExtractQueryUtils {

	public StringBuilder constructSelectQuery(String schemaName, String tableName,
			Map<String, String> tableColumnDetailsMap, Long limitNoOfRows) {

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
				query.append(" where ");
				query.append(" rownum <= ");
				query.append(limitNoOfRows);

			}

		}

		System.out.println("Oracle extract query :" + query.toString());
		return query;

	}

	public ChildTableDetails constructSelectQueryWithForeignKeyColumnsInWhereClause(
			List<TableDetails> childTableDetailsList, Map<String, String> tableColumnDetailsMap, String tableName,
			String schemaName, Connection conn) {

		ChildTableDetails childTableDetailsDBQuery = null;

		Map<String, String> keyColumnNameAndDataType = null;

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
						keyColumnNameAndDataType.put(childTableDetails.getColumnName(),
								childTableDetails.getDataType());
						noOfKeyColumns++;
					}

				}

				try {

					System.out.println("Child table query string :" + query.toString());

					PreparedStatement preparedStatement = conn.prepareStatement(query.toString());

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

	public ChildTableDetails constructSelectQueryWithChildTablesUsingJoin(List<TableDetails> childTableDetailsList,
			Map<String, String> tableColumnDetailsMap, String parentTableName,
			String childTableName, String schemaName, Connection conn) {

		ChildTableDetails childTableDetailsDBQuery = null;

		Map<String, String> keyColumnNameAndDataType = null;

		StringBuilder query = null;

		if (tableColumnDetailsMap != null) {
			
			keyColumnNameAndDataType = new LinkedHashMap<String, String>();
			for (TableDetails childTableDetails : childTableDetailsList) {
				if (childTableDetails.getTableName() != null
						&& childTableDetails.getTableName().equals(childTableName)) {
					keyColumnNameAndDataType.put(childTableDetails.getColumnName(),
							childTableDetails.getDataType());
				}
			}

			for (Map.Entry<String, String> entry : tableColumnDetailsMap.entrySet()) {

				if (query == null) {
					query = new StringBuilder();
					query.append("select ");
					if (keyColumnNameAndDataType.containsKey(entry.getKey())) {
						query.append(childTableName);
						query.append(".");
						query.append(entry.getKey());
						query.append(" as ");
						query.append(" '" + entry.getKey() + "'");
					} else {
						query.append(entry.getKey());	
					}
					
				} else {
					if (keyColumnNameAndDataType.containsKey(entry.getKey())) {
						query.append(" ,");
						query.append(childTableName);
						query.append(".");
						query.append(entry.getKey());
						query.append(" as ");
						query.append(" '" + entry.getKey() + "'");
					} else {
						query.append(", " + entry.getKey());
					}
				}

			}

			if (query != null) {
				query.append(" from  ");
				query.append(schemaName);
				query.append(".");
				query.append(parentTableName);
				query.append(", ");
				query.append(schemaName);
				query.append(".");
				query.append(childTableName);
				query.append(" where ");

			}

			int noOfKeyColumns = 0;
			if (query != null) {

				for (TableDetails childTableDetails : childTableDetailsList) {
					if (childTableDetails.getTableName() != null
							&& childTableDetails.getTableName().equals(childTableName)) {
						
						if (noOfKeyColumns == 0) {
							query.append(childTableDetails.getForeignTableAndColumn());
							query.append(" = ");
							query.append(childTableDetails.getParentTableAndColumn());
							query.append(" and ");							
							// query.append(childTableKey.getKey().substring(childTableKey.getKey().indexOf(".")));
							query.append(childTableDetails.getParentTableAndColumn());
							query.append(" = ? ");

							

						} else {
							query.append(" and ");
							query.append(childTableDetails.getColumnName());
							query.append(" = ? ");

						}
						noOfKeyColumns++;
					}

				}

				try {

					System.out.println("Child table query string :" + query.toString());

					PreparedStatement preparedStatement = conn.prepareStatement(query.toString());

					childTableDetailsDBQuery = new ChildTableDetails();

					childTableDetailsDBQuery.setPreparedStatement(preparedStatement);
					childTableDetailsDBQuery.setTableName(childTableName);
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

	public StringBuilder constructSelectQueryForSubsequentExtract(String schemaName, String tableName,
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
				query.append(" rownum <= ");
				query.append(limitNoOfRows);

			}

		}

		System.out.println("Extract query : " + query.toString());
		return query;

	}

}

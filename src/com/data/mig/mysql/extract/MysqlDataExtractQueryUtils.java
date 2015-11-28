package com.data.mig.mysql.extract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.data.mig.mysql.db.ChildTableDetails;
import com.data.mig.mysql.db.TableDetails;

public class MysqlDataExtractQueryUtils {
	
	public StringBuilder constructSelectQuery(String schemaName, String tableName, Map<String, String> tableColumnDetailsMap,
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
				query.append(schemaName);
				query.append(".");
				query.append(tableName);
				query.append(" limit ");
				query.append(limitNoOfRows);

			}

		}

		return query;

	}
	
	public ChildTableDetails constructSelectQueryWithForeignKeyColumnsInWhereClause(List<TableDetails> childTableDetailsList,
			Map<String, String> tableColumnDetailsMap, String tableName, String schemaName, Connection conn) {

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
				query.append(" limit ");
				query.append(limitNoOfRows);

			}

		}

		System.out.println("Extract query : " + query.toString());
		return query;

	}	

}

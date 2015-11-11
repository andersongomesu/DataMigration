package com.data.mig.mysql.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data.mig.constants.IApplicationConstants;

public class MysqlTableRelationship {

	public Map<String, String> getMysqlTableRelationship(Connection conn,
			String schemaName, String tableName) {

		Map<String, String> tableRelationship = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			if (conn == null) {
				MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();
				conn = mysqlDatabaseConnect.getMySqlDBConnection(schemaName,
						IApplicationConstants.defaultMySqlUserId,
						IApplicationConstants.defaultMySqlPassword);

			}

			pstmt = conn
					.prepareStatement(IApplicationConstants.retriveMySqlRelationshipQuery);
			pstmt.setString(1, schemaName);
			pstmt.setString(2, tableName);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				tableRelationship = new HashMap<String, String>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					tableRelationship.put(
							rs.getString("foreign_table_and_column"),
							rs.getString("foreign_table"));
				}

			}

			if (!isResultsetHasRecords) {
				tableRelationship = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableRelationship;

	}
	
	
	public List<TableDetails> getMysqlTableRelationshipDetailsAsObject(Connection conn,
			String schemaName, String tableName) {

		List<TableDetails> tableDetailsList = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			if (conn == null) {
				MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();
				conn = mysqlDatabaseConnect.getMySqlDBConnection(schemaName,
						IApplicationConstants.defaultMySqlUserId,
						IApplicationConstants.defaultMySqlPassword);

			}

			pstmt = conn
					.prepareStatement(IApplicationConstants.retriveMySqlRelationshipQuery_2);
			pstmt.setString(1, schemaName);
			pstmt.setString(2, tableName);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				tableDetailsList = new ArrayList<TableDetails>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					TableDetails tableDetails = new TableDetails();
					tableDetails.setColumnKey(rs.getString("column_key"));
					tableDetails.setColumnName(rs.getString("foreign_key"));
					tableDetails.setTableName(rs.getString("foreign_table"));
					tableDetails.setDataType(rs.getString("data_type"));
					tableDetailsList.add(tableDetails);
				}

			}

			if (!isResultsetHasRecords) {
				tableDetailsList = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableDetailsList;

	}
}

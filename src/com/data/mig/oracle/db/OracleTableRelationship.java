package com.data.mig.oracle.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.TableDetails;

public class OracleTableRelationship {

	public Map<String, String> getOracleTableRelationship(Connection conn,
			String schemaName, String tableName) {

		Map<String, String> tableRelationship = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			if (conn == null) {
				OracleDatabaseConnect mysqlDatabaseConnect = new OracleDatabaseConnect();
				conn = mysqlDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
						IApplicationConstants.defaultOraclePassword);

			}

			pstmt = conn
					.prepareStatement(IApplicationConstants.retriveOracleRelationshipQuery);
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
	
	
	public List<TableDetails> getOracleTableRelationshipDetailsAsObject(Connection conn,
			String schemaName, String tableName) {

		List<TableDetails> tableDetailsList = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			if (conn == null) {
				OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();
				conn = oracleDatabaseConnect.getOracleDBConnection(
						IApplicationConstants.defaultOracleUserId,
						IApplicationConstants.defaultOraclePassword);

			}

			pstmt = conn
					.prepareStatement(IApplicationConstants.retriveOracleRelationshipQuery_2);
			pstmt.setString(1, schemaName);
			pstmt.setString(2, tableName);
			
			System.out.println("Relationships of a table :" + IApplicationConstants.retriveOracleRelationshipQuery_2);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				tableDetailsList = new ArrayList<TableDetails>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					TableDetails tableDetails = new TableDetails();
					tableDetails.setForeignTableAndColumn(rs.getString("foreign_table_and_column"));
					tableDetails.setParentTableAndColumn(rs.getString("references"));
					//tableDetails.setColumnKey(rs.getString("column_key"));
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

		System.out.println("List of table details :" + tableDetailsList.toString());
		return tableDetailsList;

	}
}

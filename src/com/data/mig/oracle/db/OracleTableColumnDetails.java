package com.data.mig.oracle.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;

public class OracleTableColumnDetails {

	public Map<String, String> getOracleTableColumnDetails(Connection conn, String schemaName, String tableName) {

		Map<String, String> columnDetailsOfATable = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			if (conn == null) {
				OracleDatabaseConnect mysqlDatabaseConnect = new OracleDatabaseConnect();
				mysqlDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
						IApplicationConstants.defaultOraclePassword);

			}

			pstmt = conn.prepareStatement(IApplicationConstants.retriveOracleColumnDetails);

			System.out.println("Oracle column details :" + IApplicationConstants.retriveOracleColumnDetails);
			pstmt.setString(1, schemaName);
			pstmt.setString(2, tableName);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				columnDetailsOfATable = new HashMap<String, String>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					columnDetailsOfATable.put(rs.getString("column_name"), rs.getString("data_type"));
				}

			}

			if (!isResultsetHasRecords) {
				columnDetailsOfATable = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return columnDetailsOfATable;

	}

	public Map<String, String> getOracleTablePrimaryKey(Connection conn, String schemaName, String tableName) {

		Map<String, String> primaryColumnOfATable = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			if (conn == null) {
				MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();
				mysqlDatabaseConnect.getMySqlDBConnection(schemaName, IApplicationConstants.defaultMySqlUserId,
						IApplicationConstants.defaultMySqlPassword);

			}

			pstmt = conn.prepareStatement(IApplicationConstants.retrieveOraclePrimaryDetails);

			System.out.println("Oracle primary details :" + IApplicationConstants.retrieveOraclePrimaryDetails);
			pstmt.setString(1, schemaName);
			pstmt.setString(2, tableName);
			pstmt.setString(3, schemaName);
			pstmt.setString(4, tableName);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				primaryColumnOfATable = new HashMap<String, String>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					primaryColumnOfATable.put(rs.getString("column_name"), rs.getString("data_type"));
				}

			}

			if (!isResultsetHasRecords) {
				primaryColumnOfATable = null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return primaryColumnOfATable;

	}

}

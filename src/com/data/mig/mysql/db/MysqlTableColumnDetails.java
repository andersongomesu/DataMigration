package com.data.mig.mysql.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.data.mig.constants.IApplicationConstants;

public class MysqlTableColumnDetails {
	
	public Map<String, String> getMysqlTableColumnDetails(Connection conn,
			String schemaName, String tableName) {

		Map<String, String> columnDetailsOfATable = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			if (conn == null) {
				MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();
				mysqlDatabaseConnect.getMySqlDBConnection(schemaName,
						IApplicationConstants.defaultMySqlUserId,
						IApplicationConstants.defaultMySqlPassword);

			}

			pstmt = conn
					.prepareStatement(IApplicationConstants.retriveMySqlColumnDetails);
			pstmt.setString(1, schemaName);
			pstmt.setString(2, tableName);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				columnDetailsOfATable = new HashMap<String, String>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					columnDetailsOfATable.put(
							rs.getString("column_name"),
							rs.getString("data_type"));
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

}

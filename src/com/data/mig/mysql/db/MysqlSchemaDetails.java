package com.data.mig.mysql.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.data.mig.constants.IApplicationConstants;

public class MysqlSchemaDetails {
	
	public List<String> getMysqlTableDetails(Connection conn,
			String schemaName) {

		List<String> tableDetailsList = null;
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
					.prepareStatement(IApplicationConstants.retrieveMySQLTableDetails);
			pstmt.setString(1, schemaName);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				tableDetailsList = new ArrayList<String>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					tableDetailsList.add(schemaName + "." + rs.getString("table_name"));
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

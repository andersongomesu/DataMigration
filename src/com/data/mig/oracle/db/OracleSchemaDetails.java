package com.data.mig.oracle.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.data.mig.constants.IApplicationConstants;

public class OracleSchemaDetails {
	
	public List<String> getOracleTableDetails(Connection conn,
			String schemaName) {

		List<String> tableDetailsList = null;
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
					.prepareStatement(IApplicationConstants.retrieveOracleTableDetails);
			pstmt.setString(1, schemaName);

			rs = pstmt.executeQuery();

			boolean isResultsetHasRecords = false;

			if (rs != null) {

				tableDetailsList = new ArrayList<String>();

				while (rs.next()) {
					isResultsetHasRecords = true;
					tableDetailsList.add(rs.getString("table_name"));
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

package com.data.mig.cassandra.online.load;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.data.mig.cassandra.utils.CassandraDatabaseUtils;
import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.TableDetails;
import com.data.mig.oracle.db.OracleDatabaseConnect;
import com.data.mig.oracle.db.OracleTableRelationship;
import com.data.mig.oracle.extract.OracleDataExtract;
import com.data.mig.oracle.extract.OracleDataExtractWithChildTables;

public class OracleToCassandraOnlineLoad {

	private List<TableDetails> getOracleTableRelationshipDetails(String schemaName, String tableName) {
		
		OracleDatabaseConnect oracleDatabaseConnect = new OracleDatabaseConnect();

		Connection conn= oracleDatabaseConnect.getOracleDBConnection(IApplicationConstants.defaultOracleUserId,
				IApplicationConstants.defaultOraclePassword);

		
		OracleTableRelationship oracleTableRelationship = new OracleTableRelationship();
		List<TableDetails> childTableDetailsList = oracleTableRelationship.getOracleTableRelationshipDetailsAsObject(conn,
				IApplicationConstants.defaultOracleSchemaName, tableName);
		return childTableDetailsList;
	}


	public Boolean loadDataFromOracleToCassandra(String sourceSchemaName, String sourceTableName, String targetKeySpaceName,
			String targetColumnFamilyName, Long numberOfRecordToBeExtracted,boolean isChildColumnFamilyRequired) {

		Boolean dataLoadStatus = false;
		CassandraDatabaseUtils cassandraDatabaseUtils = new CassandraDatabaseUtils();
		OracleDataExtractWithChildTables oracleDataExtractWithChildTables = null;
		OracleDataExtract oracleDataExtract = null;
		Map<String, Object> dataExtractMap = null;
		
		try {

			if (isChildColumnFamilyRequired) {
				List<TableDetails> childTableDetailsList = getOracleTableRelationshipDetails(sourceSchemaName,sourceTableName);
				for(TableDetails childTableDetails : childTableDetailsList) {
					   String childTableName  = (String) childTableDetails.getTableName();
			           System.out.println("In loadDataFromOracleToCassandra Child Table Name :"+childTableName);
						try {
				
							oracleDataExtractWithChildTables = new OracleDataExtractWithChildTables();
							dataExtractMap = oracleDataExtractWithChildTables.extractWithGivenChildOracleDataIntoObject(sourceSchemaName, sourceTableName, childTableName, numberOfRecordToBeExtracted);
							dataLoadStatus = cassandraDatabaseUtils.writeMapIntoCassandraDatabase(sourceSchemaName,sourceTableName,targetKeySpaceName, targetColumnFamilyName,childTableName,dataExtractMap);

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
			}
			}else {
				oracleDataExtract = new OracleDataExtract();
				dataExtractMap = oracleDataExtract.extractOracleDataIntoObject(sourceSchemaName,sourceTableName, numberOfRecordToBeExtracted);	
				dataLoadStatus = cassandraDatabaseUtils.writeMapIntoCassandraDatabase(sourceSchemaName,sourceTableName,targetKeySpaceName, targetColumnFamilyName,null,dataExtractMap);
			}

		

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataLoadStatus;

	}

}

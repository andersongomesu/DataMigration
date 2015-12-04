package com.data.mig.cassandra.online.load;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.data.mig.cassandra.utils.CassandraDatabaseUtils;
import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableRelationship;
import com.data.mig.mysql.db.TableDetails;
import com.data.mig.mysql.extract.MysqlDataExtract;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;

public class MysqlToCassandraOnlineLoad {

	private List<TableDetails> getMysqlTableRelationshipDetails(String schemaName, String tableName) {
		
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn= mysqlDatabaseConnect.getMySqlDBConnection(schemaName, IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		
		MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();
		List<TableDetails> childTableDetailsList = mysqlTableRelationship.getMysqlTableRelationshipDetailsAsObject(conn,
				IApplicationConstants.defaultMySqlSchemaName, tableName);
		return childTableDetailsList;
	}


	public Boolean loadDataFromMysqlToCassandra(String sourceSchemaName, String sourceTableName, String targetKeySpaceName,
			String targetColumnFamilyName, Long numberOfRecordToBeExtracted,boolean isChildColumnFamilyRequired) {

		Boolean dataLoadStatus = false;
		CassandraDatabaseUtils cassandraDatabaseUtils = new CassandraDatabaseUtils();
		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = null;
		MysqlDataExtract mysqlDataExtract = null;
		Map<String, Object> dataExtractMap = null;
		
		try {

			if (isChildColumnFamilyRequired) {
				List<TableDetails> childTableDetailsList = getMysqlTableRelationshipDetails(sourceSchemaName,sourceTableName);
				for(TableDetails childTableDetails : childTableDetailsList) {
					   String childTableName  = (String) childTableDetails.getTableName();
			           System.out.println("In loadDataFromMysqlToCassandra Child Table Name :"+childTableName);
						try {
				
							mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
							dataExtractMap = mysqlDataExtractWithChildTables.extractWithGivenChildMysqlDataIntoObject(sourceSchemaName, sourceTableName, childTableName, numberOfRecordToBeExtracted);
							dataLoadStatus = cassandraDatabaseUtils.writeMapIntoCassandraDatabase(sourceSchemaName,sourceTableName,targetKeySpaceName, targetColumnFamilyName,childTableName,dataExtractMap);

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
			}
			}else {
				mysqlDataExtract = new MysqlDataExtract();
				dataExtractMap = mysqlDataExtract.extractMysqlDataIntoObject(sourceSchemaName,sourceTableName, numberOfRecordToBeExtracted);	
				dataLoadStatus = cassandraDatabaseUtils.writeMapIntoCassandraDatabase(sourceSchemaName,sourceTableName,targetKeySpaceName, targetColumnFamilyName,null,dataExtractMap);
			}

		

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataLoadStatus;

	}

}

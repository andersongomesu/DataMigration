package com.data.mig.mongo.online.load;

import java.sql.SQLException;
import java.util.Map;

import com.data.mig.mongo.utils.MongoDatabaseUtils;
import com.data.mig.oracle.extract.OracleDataExtract;
import com.data.mig.oracle.extract.OracleDataExtractWithChildTables;

public class OracleToMongoOnlineLoad {

	public Boolean loadDataFromOracleToMongo(String sourceSchemaName, String sourceTableName, String targetDatabaseName,
			String targetCollectionName, Long numberOfRecordToBeExtracted, boolean isChildTablesRequired) {

		Boolean dataLoadStatus = false;

		OracleDataExtractWithChildTables oracleDataExtractWithChildTables = null;
		OracleDataExtract oracleDataExtract = null;
		Map<String, Object> dataExtractMap = null;
		MongoDatabaseUtils mongoDatabaseUtils = null;

		try {

			if (isChildTablesRequired) {
				oracleDataExtractWithChildTables = new OracleDataExtractWithChildTables();
				dataExtractMap = oracleDataExtractWithChildTables.extractOracleDataIntoObject(sourceSchemaName,
						sourceTableName, numberOfRecordToBeExtracted);
				
			} else {
				oracleDataExtract = new OracleDataExtract();
				dataExtractMap = oracleDataExtract.extractOracleDataIntoObject(sourceSchemaName,
						sourceTableName, numberOfRecordToBeExtracted);				
			}

			mongoDatabaseUtils = new MongoDatabaseUtils();
			dataLoadStatus = mongoDatabaseUtils.writeMapIntoMongoCollectionForOnlineLoad(targetDatabaseName, targetCollectionName,
					sourceTableName, dataExtractMap);
			
			System.out.println("Online load from oracle to Mongo has been completed successfully !!!");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataLoadStatus;

	}

}

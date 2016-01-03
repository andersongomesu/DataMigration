package com.data.mig.mongo.online.load;

import java.sql.SQLException;
import java.util.Map;

import com.data.mig.mongo.utils.MongoDatabaseUtils;
import com.data.mig.mysql.extract.MysqlDataExtract;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;

public class MysqlToMongoOnlineLoad {

	public Boolean loadDataFromMysqlToMongo(String sourceSchemaName, String sourceTableName, String targetDatabaseName,
			String targetCollectionName, Long numberOfRecordToBeExtracted, boolean isChildTablesRequired) {

		Boolean dataLoadStatus = false;

		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = null;
		MysqlDataExtract mysqlDataExtract = null;
		Map<String, Object> dataExtractMap = null;
		MongoDatabaseUtils mongoDatabaseUtils = null;

		try {

			if (isChildTablesRequired) {
				mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
				dataExtractMap = mysqlDataExtractWithChildTables.extractMysqlDataIntoObject(sourceSchemaName,
						sourceTableName, numberOfRecordToBeExtracted);
				
			} else {
				mysqlDataExtract = new MysqlDataExtract();
				dataExtractMap = mysqlDataExtract.extractMysqlDataIntoObject(sourceSchemaName,
						sourceTableName, numberOfRecordToBeExtracted);				
			}

			if (dataExtractMap != null && dataExtractMap.size() > 0) {
				System.out.println("Extract size is :" + dataExtractMap.size());
				mongoDatabaseUtils = new MongoDatabaseUtils();
				dataLoadStatus = mongoDatabaseUtils.writeMapIntoMongoCollectionForOnlineLoad(targetDatabaseName, targetCollectionName,
						sourceTableName, dataExtractMap);
			} else {
				System.out.println("No data to extract !!!");
			}
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataLoadStatus;

	}

}

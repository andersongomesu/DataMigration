package com.data.mig.cassandra.online.load;

import java.sql.SQLException;
import java.util.Map;

import com.data.mig.cassandra.utils.CassandraDatabaseUtils;
import com.data.mig.mongo.utils.MongoDatabaseUtils;
import com.data.mig.mysql.extract.MysqlDataExtract;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;

public class MysqlToCassandraOnlineLoad {

	public Boolean loadDataFromMysqlToCassandra(String sourceSchemaName, String sourceTableName, String targetKeySpaceName,
			String targetColumnFamilyName, Long numberOfRecordToBeExtracted, boolean isChildColumnFamilyRequired) {

		Boolean dataLoadStatus = false;

		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = null;
		MysqlDataExtract mysqlDataExtract = null;
		Map<String, Object> dataExtractMap = null;
		MongoDatabaseUtils mongoDatabaseUtils = null;

		try {

			if (isChildColumnFamilyRequired) {
				mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
				dataExtractMap = mysqlDataExtractWithChildTables.extractMysqlDataIntoObject(sourceSchemaName,
						sourceTableName, numberOfRecordToBeExtracted);
				
			} else {
				mysqlDataExtract = new MysqlDataExtract();
				dataExtractMap = mysqlDataExtract.extractMysqlDataIntoObject(sourceSchemaName,
						sourceTableName, numberOfRecordToBeExtracted);				
			}

			CassandraDatabaseUtils cassandraDatabaseUtils = new CassandraDatabaseUtils();
			dataLoadStatus = cassandraDatabaseUtils.writeMapIntoCassandraDatabase(targetKeySpaceName, targetColumnFamilyName,dataExtractMap);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataLoadStatus;

	}

}

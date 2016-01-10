package com.data.mig.cassandra.online.load;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;
import com.data.mig.mysql.db.MysqlTableRelationship;
import com.data.mig.mysql.db.TableDetails;
import com.data.mig.mysql.extract.MysqlDataExtract;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;

public class MysqlToCassandraBatchLoad {

	private List<TableDetails> getMysqlTableRelationshipDetails(String schemaName, String tableName) {

		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(schemaName,
				IApplicationConstants.defaultMySqlUserId, IApplicationConstants.defaultMySqlPassword);

		MysqlTableRelationship mysqlTableRelationship = new MysqlTableRelationship();
		List<TableDetails> childTableDetailsList = mysqlTableRelationship.getMysqlTableRelationshipDetailsAsObject(conn,
				IApplicationConstants.defaultMySqlSchemaName, tableName);
		return childTableDetailsList;
	}

	public Boolean writeDataFromMysqlToJsonFile(String sourceSchemaName, String sourceTableName,
			String targetColumnFamilyName, Long numberOfRecordToBeExtracted, String filePath,
			boolean isChildColumnFamilyRequired) {

		Boolean dataExtractStatus = false;
		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = null;

		try {

			if (isChildColumnFamilyRequired) {
				List<TableDetails> childTableDetailsList = getMysqlTableRelationshipDetails(sourceSchemaName,
						sourceTableName);
				if (childTableDetailsList != null && childTableDetailsList.size() > 0) {

					System.out.println("Number of child tables :" + childTableDetailsList.size());

					for (TableDetails childTableDetails : childTableDetailsList) {
						String childTableName = (String) childTableDetails.getTableName();
						System.out.println("In loadDataFromMysqlToCassandra Child Table Name :" + childTableName);
						try {

							String fileName = filePath + "\\" + childTableName + "_" + sourceTableName + ".json";

							System.out.println("Extract file for cassandra :" + fileName);
							mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
							dataExtractStatus = mysqlDataExtractWithChildTables
									.extractMysqlDataIntoJsonFileForCassandra(sourceSchemaName, sourceTableName,
											childTableName, numberOfRecordToBeExtracted, fileName);
							if (!dataExtractStatus) {
								break;
							}

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else {
					System.out.println("No child table present for table :" + sourceTableName);
					dataExtractStatus = mysqlDataExtractWithoutChild(sourceSchemaName, sourceTableName,
							numberOfRecordToBeExtracted, filePath);
				}

			} else {

				System.out.println("Cassandra extract for table  :" + sourceTableName);
				dataExtractStatus = mysqlDataExtractWithoutChild(sourceSchemaName, sourceTableName,
						numberOfRecordToBeExtracted, filePath);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataExtractStatus;

	}

	private Boolean mysqlDataExtractWithoutChild(String sourceSchemaName, String sourceTableName,
			Long numberOfRecordToBeExtracted, String filePath) throws SQLException {

		MysqlDataExtract mysqlDataExtract = new MysqlDataExtract();

		String fileName = filePath + "\\" + sourceTableName + ".json";
		System.out.println("Extract file for cassandra :" + fileName);

		return mysqlDataExtract.extractMysqlDataIntoJsonFile(sourceSchemaName, sourceTableName,
				numberOfRecordToBeExtracted, fileName);

	}

}

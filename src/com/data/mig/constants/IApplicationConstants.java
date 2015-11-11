package com.data.mig.constants;

public interface IApplicationConstants {
	
	String defaultMySqlSchemaName = "classicmodels";
	
	String defaultMySqlUserId = "root";
	
	String defaultMySqlPassword = "root";
	
	String retriveMySqlRelationshipQuery = "select  " +
				"concat(table_name, '.', column_name) as 'foreign_table_and_column', " +
				"table_name as 'foreign_table', " +
				"column_name as 'foreign_key', " +
				"concat(referenced_table_name, '.', referenced_column_name) as 'references', " +
				"constraint_name as 'constraint name' " +
				"from information_schema.key_column_usage " +
				"where referenced_table_name is not null and table_schema = ? " +
				"and referenced_table_name = ? ";
	
	String retriveMySqlRelationshipQuery_2 = "select  " +
			"table_name as 'foreign_table', " +
			"column_name as 'foreign_key', " +
			"concat(referenced_table_name, '.', referenced_column_name) as 'references', " +
			"data_type as 'data_type', " +
			"column_key as 'column_key', " +
			"constraint_name as 'constraint_name' " +
			"from information_schema.key_column_usage " +
			"where referenced_table_name is not null and table_schema = ? " +
			"and referenced_table_name = ? ";	

	String retriveMySqlColumnDetails = "select  table_name, column_name, data_type, column_key " +
			"FROM information_schema.COLUMNS " +
			"WHERE table_schema = ? " +
			"and table_name = ? ";

	
}

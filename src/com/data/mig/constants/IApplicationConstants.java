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
	
	String retriveMySqlRelationshipQuery_2 =  "select " +
			"concat(cu.table_name, '.', cu.column_name) as 'foreign_table_and_column', " +
		    "cu.table_name as 'foreign_table', " +
		    "cu.column_name as 'foreign_key', " +  
		    "concat(cu.referenced_table_name, '.', cu.referenced_column_name) as 'references', " +
		    "cu.constraint_name as 'constraint name', " +
		    "col.data_type as 'data_type', " +
		    "col.column_key as 'column_key' " +
		"from " +
		    "information_schema.key_column_usage cu, information_schema.COLUMNS col " +
		"where " +
		    "referenced_table_name is not null " +
		    "and cu.table_schema = ? " +
		    "and cu.referenced_table_name = ? " +
		    "and cu.table_schema = col.table_schema " +
		    "and cu.table_name = col.table_name " +
		    "and cu.column_name = col.column_name ";
	
	String retrieveMySQLTableDetails = "Select table_name from information_schema.tables where table_schema = ?";
	
	String retriveMySqlColumnDetails = "select  table_name, column_name, data_type, column_key " +
			"FROM information_schema.COLUMNS " +
			"WHERE table_schema = ? " +
			"and table_name = ? ";	

	String retriveMySqlPrimaryDetails = "select  column_name " +
			"FROM information_schema.COLUMNS " +
			"WHERE table_schema = ? " +
			"and table_name = ? " +
			"and column_key = 'PRI' ";

	
}

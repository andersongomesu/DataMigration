package com.data.mig.mysql.db;

import java.sql.PreparedStatement;
import java.util.Map;

public class ChildTableDetails {
	
	PreparedStatement preparedStatement;
	
	String queryString;
	
	String tableName;
	
	int noOfKeyFields;
	
	Map<String, String> keyColumnNameAndDataType;

	
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getNoOfKeyFields() {
		return noOfKeyFields;
	}

	public void setNoOfKeyFields(int noOfKeyFields) {
		this.noOfKeyFields = noOfKeyFields;
	}

	public Map<String, String> getKeyColumnNameAndDataType() {
		return keyColumnNameAndDataType;
	}

	public void setKeyColumnNameAndDataType(
			Map<String, String> keyColumnNameAndDataType) {
		this.keyColumnNameAndDataType = keyColumnNameAndDataType;
	}

}

package com.data.mig.mysql.db;

public class TableDetails {
	
	String tableName;
	String columnName;
	String dataType;
	String columnKey;
	
	String foreignTableAndColumn;
	String parentTableAndColumn;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getColumnKey() {
		return columnKey;
	}
	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
	
	public String getForeignTableAndColumn() {
		return foreignTableAndColumn;
	}
	public void setForeignTableAndColumn(String foreignTableAndColumn) {
		this.foreignTableAndColumn = foreignTableAndColumn;
	}
	public String getParentTableAndColumn() {
		return parentTableAndColumn;
	}
	public void setParentTableAndColumn(String parentTableAndColumn) {
		this.parentTableAndColumn = parentTableAndColumn;
	}
	
	@Override
	public String toString() {
		return "TableDetails [tableName=" + tableName + ", columnName=" + columnName + ", dataType=" + dataType
				+ ", columnKey=" + columnKey + ", foreignTableAndColumn=" + foreignTableAndColumn
				+ ", parentTableAndColumn=" + parentTableAndColumn + "]";
	}
	
}

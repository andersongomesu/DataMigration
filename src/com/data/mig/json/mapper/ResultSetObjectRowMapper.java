// convenient Spring JDBC RowMapper for when you want the flexibility of Jackson's TreeModel API
// Note: Jackson can also serialize standard Java Collections (Maps and Lists) to JSON: if you don't need JsonNode,
//   it's simpler and more portable to have Spring JDBC simply return a Map or List<Map>.

package com.data.mig.json.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class ResultSetObjectRowMapper implements RowMapper<Map<String, Object>> {

	@Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        //ObjectNode objectNode = mapper.();
    	Map<String, Object> resultSetObjectMap = new LinkedHashMap<String, Object>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            Object value = rs.getObject(column);
            if (value == null) {
            	resultSetObjectMap.put(column, column);
            } else if (value instanceof Integer) {
            	resultSetObjectMap.put(column, (Integer) value);
            } else if (value instanceof String) {
                resultSetObjectMap.put(column, (String) value);                
            } else if (value instanceof Boolean) {
                resultSetObjectMap.put(column, (Boolean) value);           
            } else if (value instanceof Date) {
                resultSetObjectMap.put(column, ((Date) value));                
            } else if (value instanceof Long) {
                resultSetObjectMap.put(column, (Long) value);                
            } else if (value instanceof Double) {
                resultSetObjectMap.put(column, (Double) value);                
            } else if (value instanceof Float) {
                resultSetObjectMap.put(column, (Float) value);                
            } else if (value instanceof BigDecimal) {
                resultSetObjectMap.put(column, (BigDecimal) value);
            } else if (value instanceof Byte) {
                resultSetObjectMap.put(column, (Byte) value);
            } else if (value instanceof byte[]) {
                resultSetObjectMap.put(column, (byte[]) value);                
            } else {
                throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
            }
        }
        return resultSetObjectMap;
    }

}

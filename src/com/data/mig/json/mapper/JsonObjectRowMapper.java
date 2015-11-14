// convenient Spring JDBC RowMapper for when you want the flexibility of Jackson's TreeModel API
// Note: Jackson can also serialize standard Java Collections (Maps and Lists) to JSON: if you don't need JsonNode,
//   it's simpler and more portable to have Spring JDBC simply return a Map or List<Map>.

package com.data.mig.json.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class JsonObjectRowMapper implements RowMapper<JSONObject> {

    private final ObjectMapper mapper;
    
    public JsonObjectRowMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
	@Override
    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        //ObjectNode objectNode = mapper.();
    	JSONObject jsonObject = new JSONObject();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            Object value = rs.getObject(column);
            if (value == null) {
            	jsonObject.put(column, column);
            } else if (value instanceof Integer) {
            	jsonObject.put(column, (Integer) value);
            } else if (value instanceof String) {
                jsonObject.put(column, (String) value);                
            } else if (value instanceof Boolean) {
                jsonObject.put(column, (Boolean) value);           
            } else if (value instanceof Date) {
                jsonObject.put(column, ((Date) value).getTime());                
            } else if (value instanceof Long) {
                jsonObject.put(column, (Long) value);                
            } else if (value instanceof Double) {
                jsonObject.put(column, (Double) value);                
            } else if (value instanceof Float) {
                jsonObject.put(column, (Float) value);                
            } else if (value instanceof BigDecimal) {
                jsonObject.put(column, (BigDecimal) value);
            } else if (value instanceof Byte) {
                jsonObject.put(column, (Byte) value);
            } else if (value instanceof byte[]) {
                jsonObject.put(column, (byte[]) value);                
            } else {
                throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
            }
        }
        return jsonObject;
    }

}

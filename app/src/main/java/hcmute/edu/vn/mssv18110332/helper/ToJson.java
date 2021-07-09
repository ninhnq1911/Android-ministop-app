package hcmute.edu.vn.mssv18110332.helper;

import com.google.gson.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ToJson {
    public static JsonArray mapResultSet(ResultSet rs) throws SQLException, JsonIOException
    {
        JsonArray jArray = new JsonArray();
        JsonObject jsonObject = null;
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while(rs.next()){
            jsonObject = new JsonObject();
            for (int index = 1; index <= columnCount; index++)
            {
                String column = rsmd.getColumnName(index);
                Object value = rs.getObject(column);
                if (value == null)
                {
                    jsonObject.addProperty(column, "");
                } else if (value instanceof Integer) {
                    jsonObject.addProperty(column, (Integer) value);
                } else if (value instanceof String) {
                    jsonObject.addProperty(column, (String) value);
                } else if (value instanceof Boolean) {
                    jsonObject.addProperty(column, (Boolean) value);
                } else if (value instanceof Date) {
                    jsonObject.addProperty(column, ((Date) value).getTime());
                } else if (value instanceof Long) {
                    jsonObject.addProperty(column, (Long) value);
                } else if (value instanceof Double) {
                    jsonObject.addProperty(column, (Double) value);
                } else if (value instanceof Float) {
                    jsonObject.addProperty(column, (Float) value);
                } else if (value instanceof BigDecimal) {
                    jsonObject.addProperty(column, (BigDecimal) value);
                } else if (value instanceof Byte) {
                    jsonObject.addProperty(column, (Byte) value);
                } else if (value instanceof byte[]) {
                    jsonObject.addProperty(column, String.valueOf((byte[]) value));
                } else {
                    throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
                }
            }
            jArray.add(jsonObject);
        }
        return jArray;
    }
}

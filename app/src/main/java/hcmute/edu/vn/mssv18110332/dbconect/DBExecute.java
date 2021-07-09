package hcmute.edu.vn.mssv18110332.dbconect;

import android.util.Log;

import com.google.gson.JsonArray;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import hcmute.edu.vn.mssv18110332.helper.ToJson;

public class DBExecute {
    Connection conn;

    public static boolean execute_non_query(String Query)
    {
        Log.d("DB EXECUTE", Query);
        try {
            Connection conn = DBConnect.getConnection();
            Statement stmt = conn.createStatement();
            boolean b = stmt.execute(Query);
            conn.close();
            return b;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public static int execute_update(String Query)
    {
        Log.d("DB EXECUTE", Query);
        try {
            Connection conn = DBConnect.getConnection();
            Statement stmt = conn.createStatement();
            int rowseffected = stmt.executeUpdate(Query);
            conn.close();
            return rowseffected;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /*public static ResultSet execute_query(String Query)
    {
        try {
            Connection conn = DBConnect.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Query);
            conn.close();
            return rs;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }*/

    public static JsonArray execute_query(String Query)
    {
        Log.d("DB EXECUTE", Query);
        try {
            Connection conn = DBConnect.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Query);
            JsonArray ja = ToJson.mapResultSet(rs);
            conn.close();
            return ja;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}

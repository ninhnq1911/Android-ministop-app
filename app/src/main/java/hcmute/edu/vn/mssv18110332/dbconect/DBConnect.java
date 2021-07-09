package hcmute.edu.vn.mssv18110332.dbconect;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public static Connection getConnection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //String url = "jdbc:mysql://sql6.freemysqlhosting.net/sql6422522?useUnicode=true&characterEncoding=utf8";
            String url = "jdbc:mysql://192.168.20.107/onlineshoppingapp?useUnicode=true&characterEncoding=utf8";
            //String user = "sql6422522";
            //String pass = "Q8rWSQvq8E";
            String user = "ninhnq";
            String pass = "Nmaster2000";
            con = DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return con;
    }
}

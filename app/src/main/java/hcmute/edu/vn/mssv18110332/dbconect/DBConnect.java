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
<<<<<<< HEAD
            //String url = "jdbc:mysql://sql6.freemysqlhosting.net/sql6422522?useUnicode=true&characterEncoding=utf8";
            String url = "jdbc:mysql://192.168.42.148/onlineshoppingapp?useUnicode=true&characterEncoding=utf8";
            //String user = "sql6422522";
            //String pass = "Q8rWSQvq8E";
            String user = "ninhnq";
            String pass = "Nmaster2000";
=======
            String url = "jdbc:mysql://192.168.20.107/onlineshoppingapp?useUnicode=true&characterEncoding=utf8";
            String user = ""; // your username 
            String pass = ""; //  your passowrd
>>>>>>> 85de1e0770cf5c31720977d87cbd76a8ab813490
            con = DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return con;
    }
}

package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Provinces;

public class ProvincesDAO {
    public static List<Provinces> get_all(){
        List<Provinces> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `provinces`;");
        Type listType = new TypeToken<List<Provinces>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }


    public static Provinces get_by_id(int id) {
        List<Provinces> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `provinces` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Provinces>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static boolean update(Provinces u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(Provinces u) {
        String query = "DELETE FROM `provinces` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert_user(Provinces u) {
        String query = "INSERT INTO `provinces` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `provinces` VALUES (%id%,'%name%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",String.valueOf(u.getName()));
        DBExecute.execute_non_query(query);
        return true;
    }
}

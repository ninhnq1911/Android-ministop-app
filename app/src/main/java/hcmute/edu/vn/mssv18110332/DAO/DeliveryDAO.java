package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Delivery;
import hcmute.edu.vn.mssv18110332.model.Provinces;

public class DeliveryDAO {

    public static List<Delivery> get_all(){
        List<Delivery> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `delivery`;");
        Type listType = new TypeToken<List<Delivery>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }


    public static Delivery get_by_id(int id) {
        List<Delivery> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `delivery` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Delivery>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static boolean update(Delivery u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(Delivery u) {
        String query = "DELETE FROM `delivery` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert_user(Delivery u) {
        String query = "INSERT INTO `delivery` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `delivery` VALUES (%id%,'%name%',%cost%,%time%,'%icon%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",u.getName());
        query = query.replace("%cost%",String.valueOf(u.getCost()));
        query = query.replace("%time%",String.valueOf(u.getTime()));
        query = query.replace("%icon%",u.getIcon());
        DBExecute.execute_non_query(query);
        return true;
    }
}

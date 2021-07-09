package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Category;

public class CategoryDAO {
    public static List<Category> get_all(){
        List<Category> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `category` WHERE `id`!=11;");
        Type listType = new TypeToken<List<Category>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }


    public static Category get_by_id(int id) {
        List<Category> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `category` WHERE `id` = '" + String.valueOf(id) + "';");
        Type listType = new TypeToken<List<Category>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static boolean update(Category u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(Category u) {
        String query = "DELETE FROM `category` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert_user(Category u) {
        String query = "INSERT INTO `category` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `category` VALUES (%id%,'%name%','%image%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",String.valueOf(u.getName()));
        query = query.replace("%image%",String.valueOf(u.getImage()));
        DBExecute.execute_non_query(query);
        return true;
    }
}

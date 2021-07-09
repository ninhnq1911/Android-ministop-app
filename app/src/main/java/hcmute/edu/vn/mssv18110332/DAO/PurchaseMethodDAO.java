package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Delivery;
import hcmute.edu.vn.mssv18110332.model.Provinces;
import hcmute.edu.vn.mssv18110332.model.PurchaseMethod;

public class PurchaseMethodDAO {
    public static List<PurchaseMethod> get_all(){
        List<PurchaseMethod> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `purchase_method`;");
        Type listType = new TypeToken<List<PurchaseMethod>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }


    public static PurchaseMethod get_by_id(int id) {
        List<PurchaseMethod> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `purchase_method` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<PurchaseMethod>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static boolean update(PurchaseMethod u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(PurchaseMethod u) {
        String query = "DELETE FROM `purchase_method` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert_user(PurchaseMethod u) {
        String query = "INSERT INTO `purchase_method` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `purchase_method` VALUES (%id%,'%name%','%icon%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",u.getName());
        query = query.replace("%icon%",u.getIcon());
        DBExecute.execute_non_query(query);
        return true;
    }
}

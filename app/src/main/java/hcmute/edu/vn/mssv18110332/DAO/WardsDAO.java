package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Districts;
import hcmute.edu.vn.mssv18110332.model.Wards;

public class WardsDAO {
    public static List<Wards> get_all(){
        List<Wards> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `wards`;");
        Type listType = new TypeToken<List<Wards>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static List<Wards> get_by_disid(int id){
        List<Wards> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `wards` WHERE `district_id` = "+String.valueOf(id)+";");
        Type listType = new TypeToken<List<Wards>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static Wards get_by_id(int id) {
        List<Wards> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `wards` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Wards>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static boolean update(Wards u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(Wards u) {
        String query = "DELETE FROM `wards` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert_user(Wards u) {
        String query = "INSERT INTO `Wards` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `wards` VALUES (%id%,'%name%','%proid%','%level%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",String.valueOf(u.getName()));
        query = query.replace("%proid%",String.valueOf(u.getDistrictId()));
        query = query.replace("%level%",u.getLevel());

        DBExecute.execute_non_query(query);
        return true;
    }
}

package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Districts;
import hcmute.edu.vn.mssv18110332.model.Provinces;

public class DistrictsDAO {
    public static List<Districts> get_all(){
        List<Districts> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `districts`;");
        Type listType = new TypeToken<List<Districts>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static List<Districts> get_by_proid(int id){
        List<Districts> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `districts` WHERE province_id = "+String.valueOf(id)+";");
        Type listType = new TypeToken<List<Districts>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static Districts get_by_id(int id) {
        List<Districts> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `districts` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Districts>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst == null) return null;
        return lst.get(0);
    }

    public static boolean update(Districts u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(Districts u) {
        String query = "DELETE FROM `districts` WHERE id = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert_user(Districts u) {
        String query = "INSERT INTO `Districts` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `districts` VALUES (%id%,'%name%','%proid%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",String.valueOf(u.getName()));
        query = query.replace("%proid%",String.valueOf(u.getProvinceId()));
        DBExecute.execute_non_query(query);
        return true;
    }
}

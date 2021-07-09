package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Payitem;
import hcmute.edu.vn.mssv18110332.model.Provinces;

public class PayitemDAO {
    public static List<Payitem> get_all(){
        List<Payitem> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `payitem`;");
        Type listType = new TypeToken<List<Payitem>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }


    public static Payitem get_by_id(int id) {
        List<Payitem> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `payitem` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Payitem>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static List<Payitem> get_by_payment(int pid) {
        List<Payitem> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `payitem` WHERE `pid` = " + pid + ";");
        Type listType = new TypeToken<List<Payitem>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst;
    }

    public static boolean update(Payitem u)
    {
        delete(u);
        insert(u);
        return true;
    }

    public static boolean delete(Payitem u) {
        String query = "DELETE FROM `payitem` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert(Payitem u) {
        String query = "INSERT INTO `payitem` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `payitem` VALUES (%id%, %pid%, %item%,'%services%',%cost%,%sl%);";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%pid%",String.valueOf(u.getPid()));
        query = query.replace("%item%",String.valueOf(u.getItem()));
        query = query.replace("%services%",u.getServices()==null?"":u.getServices());
        query = query.replace("%cost%",String.valueOf(u.getCost()));
        query = query.replace("%sl%",String.valueOf(u.getSl()));
        DBExecute.execute_non_query(query);
        return true;
    }
}

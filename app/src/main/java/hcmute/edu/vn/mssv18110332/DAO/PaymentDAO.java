package hcmute.edu.vn.mssv18110332.DAO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Address;
import hcmute.edu.vn.mssv18110332.model.Payment;

public class PaymentDAO {
    public static List<Payment> get_all(){
        List<Payment> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `payment`;");
        Type listType = new TypeToken<List<Payment>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static List<Payment> get_by_user(int id) {
        List<Payment> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `payment` WHERE `uid` = '" + String.valueOf(id) + "';");
        Type listType = new TypeToken<List<Address>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static Payment get_by_id(int id) {
        List<Payment> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `payment` WHERE `pid` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Payment>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst==null || lst.isEmpty()) return null;
        return lst.get(0);
    }

    static int get_by_payment(Payment p) {
        List<Payment> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `payment` WHERE `uid` = " + p.getUid() + " AND `date`= '" + p.getDate() + "';");
        Type listType = new TypeToken<List<Payment>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst == null || lst.isEmpty()) return 0;
        return lst.get(0).getPid();
    }

    public static boolean update(Payment u)
    {
        delete(u);
        insert(u);
        return true;
    }

    public static boolean delete(Payment u) {
        String query = "DELETE FROM `payment` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getPid()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert(Payment u) {
        String query = "INSERT INTO `payment` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
                                                //"(`pid`, `name`, `uid`, `uname`, `uphone`, `uaddress`, `amount`, `method`, `date`, `status`)"
        query = "INSERT INTO `payment` VALUES (%pid%,'%name%',%uid%,'%uname%','%uphone%','%uaddress%',%amount%,%method%,'%date%','%status%');";
        query = query.replace("%pid%",String.valueOf(u.getPid()));
        query = query.replace("%name%",u.getName());
        query = query.replace("%uid%",String.valueOf(u.getUid()));
        query = query.replace("%uname%",u.getUname());
        query = query.replace("%uphone%",u.getUphone());
        query = query.replace("%uaddress%",u.getUaddress());
        query = query.replace("%amount%",String.valueOf(u.getAmount()));
        query = query.replace("%method%",String.valueOf(u.getMethod()));
        query = query.replace("%date%",u.getDate());
        query = query.replace("%status%",u.getStatus());
        DBExecute.execute_non_query(query);
        u.setPid(get_by_payment(u));
        return true;
    }
}

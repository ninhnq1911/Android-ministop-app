package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Orders;

public class OrdersDAO {
    public static List<Orders> get_all(){
        List<Orders> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `orders`;");
        Type listType = new TypeToken<List<Orders>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static List<Orders> get_by_user(int id) {
        List<Orders> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `orders` WHERE `uid` = '" + String.valueOf(id) + "';");
        Type listType = new TypeToken<List<Orders>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static Orders get_by_id(int id) {
        List<Orders> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `orders` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Orders>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static boolean update(Orders u)
    {
        delete(u);
        insert(u);
        return true;
    }

    public static boolean delete(Orders u) {
        String query = "DELETE FROM `orders` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }

    public static boolean insert(Orders u) {
        String query = "INSERT INTO `orders` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `orders` VALUES (`id`, `uid`, '`uname`', '`uphone`', '`uaddress`', '`start_date`', '`end_date`', `pid`, '`state`', `did`);";
        query = query.replace("`id`",String.valueOf(u.getId()));
        query = query.replace("`uid`",String.valueOf(u.getUid()));
        query = query.replace("`uname`",u.getUname());
        query = query.replace("`uphone`",u.getUphone());
        query = query.replace("`uaddress`",u.getUaddress());
        query = query.replace("`start_date`",u.getStart_date());
        query = query.replace("`end_date`",u.getEnd_date());
        query = query.replace("`pid`",String.valueOf(u.getPid()));
        query = query.replace("`state`",u.getState());
        query = query.replace("`did`",String.valueOf(u.getDid()));
        DBExecute.execute_non_query(query);
        return true;
    }
}

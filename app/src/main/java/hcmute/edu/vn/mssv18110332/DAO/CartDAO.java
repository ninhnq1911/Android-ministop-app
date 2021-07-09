package hcmute.edu.vn.mssv18110332.DAO;

import android.util.Log;

import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Address;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Items;
import hcmute.edu.vn.mssv18110332.model.Wards;

public class CartDAO {
    public static List<Cart> get_all(){
        List<Cart> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `cart`;");
        Type listType = new TypeToken<List<Cart>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static int getTotal_Cost()
    {
        int total = 0;
        List<Cart> list = get_by_user(AppUtils.getCurrentUserID());
        for (Cart c: list)
        {
            if (c.getCheck()==0) continue;
            Items item = ItemsDAO.get_by_id(c.getItem());
            total += item.getDiscounted() * c.getSl();
        }
        return total;
    }

    public static int getTotal_Cost(List<Cart> list)
    {
        int total = 0;
        for (Cart c: list)
        {
            if (c.getCheck()==0) continue;
            Items item = ItemsDAO.get_by_id(c.getItem());
            total += item.getDiscounted() * c.getSl();
        }
        return total;
    }

    public static int getTotal_Cost_Price(List<Cart> list)
    {
        int total = 0;
        for (Cart c: list)
        {
            if (c.getCheck()==0) continue;
            Items item = ItemsDAO.get_by_id(c.getItem());
            total += item.getPrice() * c.getSl();
        }
        return total;
    }

    public static void setAllChecked()
    {
        List<Cart> list = get_by_user(AppUtils.getCurrentUserID());
        for (Cart c: list)
            if (c.getCheck() == 0){
                c.setCheck(1);
                update(c);
            }
    }

    public static Cart get_by_id(int id) {
        List<Cart> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `cart` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Cart>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static Cart get_by_user_item(int uid, int item) {
        List<Cart> lst = null;
        String query = "SELECT * FROM `cart` WHERE `user` = %uid% AND `item` = %item%;";
        query = query.replace("%uid%",String.valueOf(uid));
        query = query.replace("%item%",String.valueOf(item));
        JsonArray ja = DBExecute.execute_query(query);
        Type listType = new TypeToken<List<Cart>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst==null || lst.size()==0) return null;
        String msg = "The number of rows: " + String.valueOf(lst.size());
        Log.d("CART_DAO",msg);
        return lst.get(0);
    }

    public static List<Cart> get_by_user(int id) {
        List<Cart> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `cart` WHERE `user` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Cart>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst==null) return null;
        String msg = "The number of rows: " + String.valueOf(lst.size());
        Log.d("CART_DAO",msg);
        return lst;
    }

    public static List<Cart> get_selected(int uid) {
        List<Cart> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `cart` WHERE `user` = " + String.valueOf(uid) + " AND `check` = 1;");
        Type listType = new TypeToken<List<Cart>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst==null) return null;
        String msg = "The number of rows: " + String.valueOf(lst.size());
        Log.d("CART_DAO",msg);
        return lst;
    }

    public static String get_list_cart_string(List<Cart> list) {
        Type listType = new TypeToken<List<Cart>>() {}.getType();
        Gson gson = new Gson();
        return gson.toJson(list,listType);
    }

    public static boolean update(Cart u)
    {
        delete(u);
        insert(u);
        return true;
    }

    public static boolean delete(Cart u) {
        String query = "DELETE FROM `cart` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }

    public static boolean insert(Cart u) {
        String query = "INSERT INTO `adress` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `cart` VALUES (%user%,%id%,'%item%',%sl%,'%check%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%user%",String.valueOf(u.getUser()));
        query = query.replace("%item%",String.valueOf(u.getItem()));
        query = query.replace("%sl%",String.valueOf(u.getSl()));
        query = query.replace("%check%",String.valueOf(u.getCheck()));
        DBExecute.execute_non_query(query);
        return true;
    }
}
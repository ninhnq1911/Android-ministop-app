package hcmute.edu.vn.mssv18110332.DAO;

import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Address;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Useraccount;
import hcmute.edu.vn.mssv18110332.model.Wards;

public class AddressDAO {
    public static List<Address> get_all(){
        List<Address> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `address`;");
        Type listType = new TypeToken<List<Address>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static Address get_by_id(int id) {
        List<Address> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `address` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Address>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst == null) return null;
        return lst.get(0);
    }

    public static List<Address> get_by_user(int id) {
        List<Address> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `address` WHERE `user` = '" + String.valueOf(id) + "';");
        Type listType = new TypeToken<List<Address>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static boolean update(Address u)
    {
        delete(u);
        insert(u);
        return true;
    }

    public static boolean delete(Address u) {
        String query = "DELETE FROM `address` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }

    public static String getFull(int id)
    {
        Address u = get_by_id(id);
        if (u==null) return ".";
        String home, dis, war, pro, cou;
        cou = "Việt Nam";
        pro = ProvincesDAO.get_by_id(u.getPro()).getName();
        dis = DistrictsDAO.get_by_id(u.getDis()).getName();
        war = WardsDAO.get_by_id(u.getWar()).getName();
        home = u.getHome();
        return home + ", " + war + ", " + dis + ", " + pro + ", " + cou;
    }

    public static String getFull(Address u)
    {
        if (u==null) return ".";
        String home, dis, war, pro, cou;
        cou = "Việt Nam";
        pro = ProvincesDAO.get_by_id(u.getPro()).getName();
        dis = DistrictsDAO.get_by_id(u.getDis()).getName();
        war = WardsDAO.get_by_id(u.getWar()).getName();
        home = u.getHome();
        return home + ", " + war + ", " + dis + ", " + pro + ", " + cou;
    }

    public static boolean isDefault(Address u)
    {
        return (UserAccountDAO.get_by_id(u.getUser()).getAddress()==u.getId());
    }

    public static boolean isDefault(Address u, Useraccount a)
    {
        return u.getId() == a.getAddress();
    }

    public static boolean insert(Address u) {
        String query = "INSERT INTO `adress` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `address` VALUES (%id%,%user%,'%name%',%pro%,%dis%,%war%,'%home%');";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%user%",String.valueOf(u.getUser()));
        query = query.replace("%name%",u.getName());
        query = query.replace("%pro%",String.valueOf(u.getPro()));
        query = query.replace("%dis%",String.valueOf(u.getDis()));
        query = query.replace("%war%",String.valueOf(u.getWar()));
        query = query.replace("%home%",u.getHome());
        DBExecute.execute_non_query(query);
        return true;
    }
}

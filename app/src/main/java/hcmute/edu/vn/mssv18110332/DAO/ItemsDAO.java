package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.model.Favorite;
import hcmute.edu.vn.mssv18110332.model.Items;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class ItemsDAO {

    /* in progress ... */
    public static boolean isFavorite(Useraccount u, Items i)
    {
        List<Favorite> lst = null;
        String query = "SELECT * FROM `favorite` WHERE `uid` = %uid% AND `item` = %item%;";
        query = query.replace("%uid%",String.valueOf(u.getId()));
        query = query.replace("%item%",String.valueOf(i.getId()));
        JsonArray ja = DBExecute.execute_query(query);
        Type listType = new TypeToken<List<Favorite>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst == null || lst.size()==0)
            return false;
        return true;
    }

    public static int getCost(int id)
    {
        Items items = get_by_id(id);
        return items.getDiscounted();
    }

    public static void setFavorite(Useraccount u, Items i)
    {
        String query = "INSERT INTO `favorite` VALUES (%uid%,%item%);";
        query = query.replace("%uid%",String.valueOf(u.getId()));
        query = query.replace("%item%",String.valueOf(i.getId()));
        DBExecute.execute_non_query(query);
    }

    public static void unFavorite(Useraccount u, Items i)
    {
        String query = "DELETE FROM `favorite` WHERE `uid` = %uid% AND `item` = %item%;";
        query = query.replace("%uid%",String.valueOf(u.getId()));
        query = query.replace("%item%",String.valueOf(i.getId()));
        DBExecute.execute_non_query(query);
    }

    public static List<Items> get_all(){
        List<Items> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `items` WHERE `active` = 1;");
        Type listType = new TypeToken<List<Items>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static Items get_by_id(int id) {
        List<Items> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `items` WHERE `id` = '" + String.valueOf(id) + "';");
        Type listType = new TypeToken<List<Items>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static List<Items> get_by_category(int id) {
        List<Items> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `items` WHERE `category` = '" + String.valueOf(id) + "' AND `active` = 1;");
        Type listType = new TypeToken<List<Items>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static boolean update(Items u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(Items u) {
        String query = "DELETE FROM `items` WHERE `id` = %id%;";
        query = query.replace("%id%", String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);
        ;
        return true;
    }

    public static boolean insert_user(Items u) {
        String query = "(19,'Nướcc Aquafina','http://blueseawater.com/pictures_products/get1407854178.jpg',5000,1,100,'Sai Gon Coop','11-12-2020','11-12-2022');";
        query = "INSERT INTO `items` VALUES (%id%,'%name%',%image%,%price%,%category%,%stock%,%provider%,%Mfg%,%Exp%,%dis%,%promo%,%deli%,%active%);";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",String.valueOf(u.getName()));
        query = query.replace("%image%",String.valueOf(u.getImage()));
        query = query.replace("%price%",String.valueOf(u.getPrice()));
        query = query.replace("%category%",String.valueOf(u.getProvider()));
        query = query.replace("%stock%",String.valueOf(u.getMfg()));
        query = query.replace("%provider%",String.valueOf(u.getExp()));
        query = query.replace("%Mfg%",String.valueOf(u.getExp()));
        query = query.replace("%Exp%",String.valueOf(u.getExp()));
        query = query.replace("%dis%",String.valueOf(u.getDiscounted()));
        query = query.replace("%promo%",u.getPromotion());
        query = query.replace("%deli%",u.getDelivery());
        query = query.replace("%active%",String.valueOf(u.getActive()));
        DBExecute.execute_non_query(query);
        return true;
    }
}


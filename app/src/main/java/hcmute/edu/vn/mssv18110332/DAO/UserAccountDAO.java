package hcmute.edu.vn.mssv18110332.DAO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import hcmute.edu.vn.mssv18110332.dbconect.DBExecute;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class UserAccountDAO {

    public static List<Useraccount> get_all(){
        List<Useraccount> lst = null;
        JsonArray ja = DBExecute.execute_query("SELECT * FROM `useraccount`;");
        Type listType = new TypeToken<List<Useraccount>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        return lst;
    }

    public static boolean createNewAccount(String name, String pass)
    {
        Useraccount u = new Useraccount();
        u.setEmail(name);
        u.setPass(pass);
        u.setName("");
        u.setFullname("");
        u.setBirth(AppUtils.getCurrentDateTime("yyyy-MM-dd"));
        u.setGender(1);
        u.setId(0);
        u.setPhonenumber("");
        u.setAddress(0);
        u.setActive(1);
        return insert_user(u);
    }

    public static Useraccount get_by_email(String email) {
        List<Useraccount> lst = null;
        JsonArray ja;
        ja = DBExecute.execute_query("SELECT * FROM `useraccount` WHERE `email` = '" + email + "';");
        Type listType = new TypeToken<List<Useraccount>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static Useraccount get_by_phone(String phone) {
        List<Useraccount> lst = null;
        JsonArray ja;
        ja = DBExecute.execute_query("SELECT * FROM `useraccount` WHERE `phonenumber` = '" + phone + "';");
        Type listType = new TypeToken<List<Useraccount>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static Useraccount get_by_id(int id) {
        List<Useraccount> lst = null;
        JsonArray ja;
        ja = DBExecute.execute_query("SELECT * FROM `useraccount` WHERE `id` = " + String.valueOf(id) + ";");
        Type listType = new TypeToken<List<Useraccount>>() {}.getType();
        lst = new Gson().fromJson(ja, listType);
        if (lst.isEmpty()) return null;
        return lst.get(0);
    }

    public static boolean update(Useraccount u)
    {
        delete(u);
        insert_user(u);
        return true;
    }

    public static boolean delete(Useraccount u) {
        String query = "DELETE FROM `useraccount` WHERE `id` = %id%;";
        query = query.replace("%id%",String.valueOf(u.getId()));
        DBExecute.execute_non_query(query);;
        return true;
    }


    public static boolean insert_user(Useraccount u) {
        String query = "INSERT INTO `useraccount` VALUES (122337929,'Ninh','19/11/2000',1,'ninh@gmail.com','a','Nguyen Quoc Ninh','0983839657','79',12389);";
        query = "INSERT INTO `useraccount` VALUES (%id%,'%name%','%birth%',%gender%,'%email%','%pass%','%fullname%','%phone%',%address%,%active%);";
        query = query.replace("%id%",String.valueOf(u.getId()));
        query = query.replace("%name%",String.valueOf(u.getName()));
        query = query.replace("%birth%",String.valueOf(u.getBirth()));
        query = query.replace("%gender%",String.valueOf(u.getGender()));
        query = query.replace("%email%",String.valueOf(u.getEmail()));
        query = query.replace("%pass%",String.valueOf(u.getPass()));
        query = query.replace("%fullname%",String.valueOf(u.getFullname()));
        query = query.replace("%phone%",String.valueOf(u.getPhonenumber()));
        query = query.replace("%address%",String.valueOf(u.getAddress()));
        query = query.replace("%active%",String.valueOf(u.getActive()));
        DBExecute.execute_non_query(query);
        return true;
    }

    /*public static List<Useraccount> get_all(){
        ResultSet rs;
        List<Useraccount> lst = null;
        rs = DBExecute.execute_query("SELECT * FROM `useraccount`;");
        if (rs != null)
        {
            lst = new ArrayList<>();
            try
            {
                do {
                    Useraccount u= new Useraccount();
                    u.setName(rs.getNString("name"));
                    u.setBirth(rs.getString("birth"));
                    u.setGender(rs.getInt("gender"));
                    u.setEmail(rs.getString("email"));
                    u.setFullname(rs.getNString("fullname"));
                    u.setId(rs.getInt("id"));
                    u.setPass(rs.getNString("pass"));
                    u.setPhonenumber(rs.getNString("phonenumber"));
                    lst.add(u);
                } while (rs.next());
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                return null;
            }
        }
        return lst;
    }

    public static Useraccount get_by_id(int id) {
        ResultSet rs;
        rs = DBExecute.execute_query("SELECT * FROM `useraccount` WHERE id = '" + String.valueOf(id) + "';");
        try
        {
            if (rs != null && !rs.isClosed())
            {
                Useraccount u= new Useraccount();
                u.setName(rs.getNString("name"));
                u.setBirth(rs.getString("birth"));
                u.setGender(rs.getInt("gender"));
                u.setEmail(rs.getString("email"));
                u.setFullname(rs.getNString("fullname"));
                u.setId(rs.getInt("id"));
                u.setPass(rs.getNString("pass"));
                u.setPhonenumber(rs.getNString("phonenumber"));
                return u;
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    public static Useraccount get_by_email(String email) {
        ResultSet rs;
        rs = DBExecute.execute_query("SELECT * FROM `useraccount` WHERE email = '" + email + "';");
        try
        {
            if (rs != null)
            {
                Useraccount u= new Useraccount();
                u.setName(rs.getNString("name"));
                u.setBirth(rs.getString("birth"));
                u.setGender(rs.getInt("gender"));
                u.setEmail(rs.getString("email"));
                u.setFullname(rs.getNString("fullname"));
                u.setId(rs.getInt("id"));
                u.setPass(rs.getNString("pass"));
                u.setPhonenumber(rs.getNString("phonenumber"));
                return u;
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return null;
    }*/

}

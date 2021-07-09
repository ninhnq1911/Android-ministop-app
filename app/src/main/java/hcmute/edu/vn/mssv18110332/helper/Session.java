package hcmute.edu.vn.mssv18110332.helper;

import android.content.Context;
import android.content.SharedPreferences;

import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class Session {
    public static void store(Context context, String key, String value)
    {
        SharedPreferences sharedPreferences =  context.getSharedPreferences("App", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String read(Context context, String key)
    {
        SharedPreferences sharedPreferences =  context.getSharedPreferences("App", 0);
        return sharedPreferences.getString(key,"");
    }

    public static void remove(Context context, String key)
    {
        SharedPreferences sharedPreferences =  context.getSharedPreferences("App", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}

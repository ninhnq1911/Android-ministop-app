package hcmute.edu.vn.mssv18110332.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class AppUtils {
    public static String getNextDate(int days){
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(getCurrentDateTime("yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) return "1997-01-01";
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return format.format(calendar.getTime());
    }

    public static String getCurrentDateTime()
    {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static String getCurrentDateTime(String format)
    {
        SimpleDateFormat formatter= new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static String getVietNamDongFormat(int i)
    {
        //String s = String.valueOf(i);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("###,###,###.### ₫");
        String output = df.format(i);
        return output;
    }

    public static void showMessage(Context mContext, String message)
    {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(mContext,message,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public static void sendEmail(Context context, String message, String receivers)
    {
        Gmail mail = new Gmail( receivers,
                "[no-reply] Send to you from MiniStop services",
                message);
        final ProgressDialog pd = new ProgressDialog();
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //pd.show_progress_dialog(context);
            }
        });
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    Log.i("SendMailTask", "About to instantiate GMail...");
                    mail.createEmailMessage();
                    mail.sendEmail();
                    Log.i("SendMailTask", "Mail Sent.");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e("SendMailTask", e.getMessage(), e);
                    return "failed";
                }
                return "succeded";
            }
        };

        TaskRunner.Callback<Object> callback = new TaskRunner.Callback<Object>() {
            @Override
            public void onComplete(Object result) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pd.hide_progress_dialog();
                    }
                });
            }
        };
        TaskRunner asyncTask = new TaskRunner();
        asyncTask.executeAsync(callable,callback);
    }

    public static int getCurrentUserID()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser u = auth.getCurrentUser();
        if (u == null)
        {
            Log.d("USER","Firebase is nulll");
            return 0;
        }
        Useraccount u_ = UserAccountDAO.get_by_email(u.getEmail());
        if (u == null || u_ == null) return 0;
        return u_.getId();
    }

    public static Useraccount getCurrentUser()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser u = auth.getCurrentUser();
        if (u == null)
        {
            Log.d("USER","Firebase is nulll");
            return null;
        }
        Useraccount u_ = UserAccountDAO.get_by_email(u.getEmail());
        if (u_ == null)
            u_ = UserAccountDAO.get_by_phone(u.getPhoneNumber());
        return u_;
    }

    public static String convertToStatus(String  s)
    {
        s = s.toLowerCase();
        switch (s)
        {
            case "created": return "(Đã nhận đơn hàng)";
            case "process": return "(Đang xử lí đơn hàng)";
            case "delivery": return "(Đang giao hàng)";
            case "finished": return "(Đã giao hàng)";
            case "canceled": return "(Đã hủy)";
            default:
                return "(Không có thông tin)";
        }
    }
}

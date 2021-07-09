package hcmute.edu.vn.mssv18110332.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import hcmute.edu.vn.mssv18110332.R;

public class ProgressDialog {

    AlertDialog dialog;
    AlertDialog.Builder builder;

    public void show_progress_dialog(Context context)
    {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false); // if you want user to wait for some process to finish
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.layout_loading_dialog, null);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show(); // to show this dialog
    }

    public void hide_progress_dialog()
    {
        dialog.dismiss();
    }
}

package hcmute.edu.vn.mssv18110332.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

import hcmute.edu.vn.mssv18110332.R;

public class ConfirmDialog {
    private Dialog dialog;
    public ConfirmDialog(Context context) {
        this.context = context;
    }

    private Context context;
    private onCancelCLickListener cancelCLickListener;
    private onConfirmCLickListener confirmCLickListener;

    public interface onCancelCLickListener{
        void onClick(boolean result);
    }

    public interface onConfirmCLickListener{
        void onClick(boolean result);
    }


    public void setConfirmCLickListener(onConfirmCLickListener confirmCLickListener) {
        this.confirmCLickListener = confirmCLickListener;
    }

    public void setCancelCLickListener(onCancelCLickListener cancelCLickListener) {
        this.cancelCLickListener = cancelCLickListener;
    }

    public void showDialog(int gravity, String name, String message)
    {
        if (context==null) return;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog_layout);

        Window window = dialog.getWindow();
        if (window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowsAttribute = window.getAttributes();
        windowsAttribute.gravity = gravity;
        window.setAttributes(windowsAttribute);

        dialog.setCancelable(false);

        TextView txtName =  dialog.findViewById(R.id.txt_dialog_name_confirm_dialog);
        TextView txtMessage = dialog.findViewById(R.id.txt_dialog_message_confirm_dialog);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_confirm_dialog);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_confirm_dialog);

        txtName.setText(name);
        txtMessage.setText(message);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelCLickListener != null)
                    cancelCLickListener.onClick(false);
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmCLickListener!=null)
                    confirmCLickListener.onClick(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void hideDialog()
    {
        dialog.dismiss();
    }
}

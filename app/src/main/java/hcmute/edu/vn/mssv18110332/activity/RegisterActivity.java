package hcmute.edu.vn.mssv18110332.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ActivityRegisterBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.DataValidate;
import hcmute.edu.vn.mssv18110332.helper.FireBaseUtils;
import hcmute.edu.vn.mssv18110332.helper.Gmail;
import hcmute.edu.vn.mssv18110332.helper.Session;
import hcmute.edu.vn.mssv18110332.model.Useraccount;
import hcmute.edu.vn.mssv18110332.DAO.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int CAMERA_PIC_REQUEST = 251;
    ActivityRegisterBinding bin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bin = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(bin.getRoot());

        bin.regLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        bin.regRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = bin.regUsername.getText().toString();
                String pass = bin.regPassword.getText().toString();
                String conf = bin.regConfirmPass.getText().toString();

                if (pass.equals(conf))
                {
                    Useraccount u = UserAccountDAO.get_by_email(name);
                    if (u != null)
                    {
                        AppUtils.showMessage(RegisterActivity.this,String.valueOf(u.getId()) + "Email đã tồn tại!");
                        return;
                    }
                    if (!DataValidate.validateEmail(name).equals("OK"))
                    {
                        AppUtils.showMessage(RegisterActivity.this,"Email không hợp lệ");
                        return;
                    }
                    if (!DataValidate.validatePassword(pass).equals("OK"))
                    {
                        AppUtils.showMessage(RegisterActivity.this,"Password không hợp lệ");
                        return;
                    }
                    FireBaseUtils.register_email(RegisterActivity.this,name,pass);
                }
                else
                {
                    AppUtils.showMessage(RegisterActivity.this,"Confirm Password không giống");
                    return;
                }
            }
        });

        bin.regTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                someActivityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                bin.regImageAvatar.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bitmap bp = (Bitmap) data.getExtras().get("data");
                        bin.regImageAvatar.setImageBitmap(bp);
                    }
                    else if (result.getResultCode() == RESULT_CANCELED)
                        AppUtils.showMessage(RegisterActivity.this, "Action canceled");
                    else
                        AppUtils.showMessage(RegisterActivity.this, "Action Failed");
                }
            });

}
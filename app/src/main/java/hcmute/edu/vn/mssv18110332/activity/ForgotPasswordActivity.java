package hcmute.edu.vn.mssv18110332.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import hcmute.edu.vn.mssv18110332.DAO.*;

import hcmute.edu.vn.mssv18110332.databinding.ActivityForgotPasswordBinding;
import hcmute.edu.vn.mssv18110332.databinding.ActivityLoginBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.FireBaseUtils;
import hcmute.edu.vn.mssv18110332.helper.Session;
import hcmute.edu.vn.mssv18110332.model.Useraccount;


public class ForgotPasswordActivity extends AppCompatActivity {

    @NonNull ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnChangePasswordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmailForgot.getText().toString();
                Useraccount u = UserAccountDAO.get_by_email(email);
                if (u!=null)
                {
                    FireBaseUtils.change_password_email(getContext(),email);
                    finish();
                    return;
                }
                Toast.makeText(ForgotPasswordActivity.this, "Không tìm thấy tài khoản. Bạn chưa có tài khoản? Đăng kí ngay!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Context getContext() {return ForgotPasswordActivity.this;}
}
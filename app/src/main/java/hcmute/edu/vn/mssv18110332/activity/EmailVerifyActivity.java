package hcmute.edu.vn.mssv18110332.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hcmute.edu.vn.mssv18110332.databinding.ActivityEmailVerifyBinding;
import hcmute.edu.vn.mssv18110332.helper.FireBaseUtils;
import hcmute.edu.vn.mssv18110332.helper.ProgressDialog;

public class EmailVerifyActivity extends AppCompatActivity {

    ActivityEmailVerifyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_verify_email);
        binding = ActivityEmailVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.vefVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.vefEmail.getText().toString().trim();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser u = auth.getCurrentUser();
                if ((u==null) )
                {
                    Toast.makeText(EmailVerifyActivity.this,"Bạn chưa đăng kí",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmailVerifyActivity.this,RegisterActivity.class);
                    EmailVerifyActivity.this.startActivity(intent);
                    finish();
                    return;
                }
                if (!u.getEmail().equals(email))
                {
                    Toast.makeText(EmailVerifyActivity.this,"Không tìm thấy email",Toast.LENGTH_SHORT).show();
                    return;
                }
                ProgressDialog pd = new ProgressDialog();
                pd.show_progress_dialog(EmailVerifyActivity.this);
                FireBaseUtils.email_verify(EmailVerifyActivity.this,u);
                pd.hide_progress_dialog();
                Intent intent = new Intent(EmailVerifyActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
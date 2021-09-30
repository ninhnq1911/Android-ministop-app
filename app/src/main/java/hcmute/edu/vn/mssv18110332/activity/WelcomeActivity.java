package hcmute.edu.vn.mssv18110332.activity;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.mssv18110332.DAO.ItemsDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ActivityWelcomeBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.gson.Gson;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Intent i = new Intent(WelcomeActivity.this,ItemDetailActivity.class);
        Gson gson = new Gson();
        String json = gson.toJson(ItemsDAO.get_all().get(0));
        i.putExtra("item", json);
        startActivity(i);*/

        binding.welBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.welBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.welBtnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Quên mật khẩu",Toast.LENGTH_LONG).show();
                Intent i = new Intent(WelcomeActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

    }
}
package hcmute.edu.vn.mssv18110332.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import hcmute.edu.vn.mssv18110332.DAO.*;

import hcmute.edu.vn.mssv18110332.databinding.ActivityLoginBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.DataValidate;
import hcmute.edu.vn.mssv18110332.helper.FireBaseUtils;
import hcmute.edu.vn.mssv18110332.helper.Session;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        auth.signOut();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authenticate(binding.txtUsername.getText().toString(),binding.txtUserpass.getText().toString());
            }
        });

        binding.btnForgotPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    boolean Authenticate(String email, String pass){
        if (!DataValidate.validateEmail(email).equals("OK") ||
            !DataValidate.validatePassword(pass).equals("OK"))
        {
            Toast.makeText(LoginActivity.this, "Email / password không đúng!", Toast.LENGTH_SHORT).show();
            return false;
        }
        Useraccount user = UserAccountDAO.get_by_email(email);
        FireBaseUtils.login_email(LoginActivity.this,email,pass);
        if (user != null && user.getPass().equals(pass))
        {
            Session.store(getApplicationContext(),"Authenticated","true");
            Session.store(getApplicationContext(),"UserID",String.valueOf(user.getId()));
            Session.store(getApplicationContext(),"UserName",user.getName());
            //Session.store(getApplicationContext(),"UserCart",String.valueOf(user.getCart()));
            return true;
        }
        return false;
    }

    @Override
    public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null ) {
            Useraccount u = AppUtils.getCurrentUser();
            if (!u.getPass().equals(binding.txtUserpass.getText().toString()))
            {
                u.setPass(binding.txtUserpass.getText().toString());
                UserAccountDAO.update(u);
            }
            if (user.isEmailVerified())
            {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(getApplicationContext(), EmailVerifyActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }
}
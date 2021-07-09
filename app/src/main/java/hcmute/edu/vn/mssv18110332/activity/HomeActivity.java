package hcmute.edu.vn.mssv18110332.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ActivityHomeBinding;
import hcmute.edu.vn.mssv18110332.interface_define.IOnBackPressed;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_main);
        //NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null ) {
            Toast.makeText(getApplicationContext(),"Bạn chưa đăng nhập",Toast.LENGTH_SHORT);
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
        if (!user.isEmailVerified())
        {
            Toast.makeText(getApplicationContext(),"Bạn chưa xác nhận email tài khoản",Toast.LENGTH_SHORT);
            startActivity(new Intent(getApplicationContext(), EmailVerifyActivity.class));
            finish();
        }
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null ) {
                    Toast.makeText(getApplicationContext(),"Bạn chưa đăng nhập",Toast.LENGTH_SHORT);
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }
                if (!user.isEmailVerified())
                {
                    Toast.makeText(getApplicationContext(),"Bạn chưa xác nhận email tài khoản",Toast.LENGTH_SHORT);
                    startActivity(new Intent(getApplicationContext(), EmailVerifyActivity.class));
                    finish();
                }
            }
        };
        //Intent i = new Intent(HomeActivity.this,PurchaseActivity.class);
        //startActivity(i);
    }
}
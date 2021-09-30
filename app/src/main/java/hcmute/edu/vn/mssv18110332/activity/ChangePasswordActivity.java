package hcmute.edu.vn.mssv18110332.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hcmute.edu.vn.mssv18110332.DAO.AddressDAO;
import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.adapter.address.AddressAdapter;
import hcmute.edu.vn.mssv18110332.databinding.ActivityAddressBookBinding;
import hcmute.edu.vn.mssv18110332.databinding.ActivityChangePasswordBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.DataValidate;
import hcmute.edu.vn.mssv18110332.helper.FireBaseUtils;
import hcmute.edu.vn.mssv18110332.helper.ProgressDialog;
import hcmute.edu.vn.mssv18110332.model.Address;
import hcmute.edu.vn.mssv18110332.model.Useraccount;


public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnChangePasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CHANG PASSWORD","button click");

                String old_pass = binding.txtPasswordChange.getText().toString();
                String new_pass = binding.txtNewPasswordChange.getText().toString();
                String con_pass = binding.txtNewPasswordConfirmChange.getText().toString();

                if (old_pass.isEmpty() || new_pass.isEmpty() || con_pass.isEmpty())
                {
                    Toast.makeText(getContext(),"Password không được để trống",Toast.LENGTH_SHORT).show();
                    Log.d("CHANG PASSWORD","Password không được để trống");
                    return;
                }

                Useraccount user = AppUtils.getCurrentUser();

                if (!old_pass.equals(user.getPass()))
                {
                    Toast.makeText(getContext(),"Password của bạn nhập không đúng",Toast.LENGTH_SHORT).show();
                    Log.d("CHANG PASSWORD","Password của bạn nhập không đúng");
                    return;
                }

                if (!new_pass.equals(con_pass))
                {
                    Toast.makeText(getContext(),"Password mới bạn nhập không khớp nhau",Toast.LENGTH_SHORT).show();
                    Log.d("CHANG PASSWORD","Password mới bạn nhập không khớp nhau");
                    return;
                }
                if (!DataValidate.validatePassword(new_pass).equals("OK"))
                {
                    Toast.makeText(getContext(),"Password phải có ít nhất 6 kí tự gồm SỐ và CHỮ CÁI",Toast.LENGTH_SHORT).show();
                    Log.d("CHANG PASSWORD","Password phải có ít nhất 6 kí tự gồm SỐ và CHỮ CÁI");
                    return;
                }
                ProgressDialog pd = new ProgressDialog();
                pd.show_progress_dialog(getContext());
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FireBaseUtils.change_password(getContext(),auth.getCurrentUser(),new_pass);
                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu của bạn đã được thay đổi, đăng nhập lại nhé!", Toast.LENGTH_SHORT).show();
                auth.signOut();
                Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(i);
                pd.hide_progress_dialog();
                finish();
            }
        });
    }

    Context getContext()
    {return ChangePasswordActivity.this;}
}
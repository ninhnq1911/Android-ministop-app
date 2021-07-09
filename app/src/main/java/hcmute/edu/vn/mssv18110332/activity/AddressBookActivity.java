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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hcmute.edu.vn.mssv18110332.DAO.AddressDAO;
import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.adapter.address.AddressAdapter;
import hcmute.edu.vn.mssv18110332.databinding.ActivityAddressBookBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Address;

public class AddressBookActivity extends AppCompatActivity {

    ActivityAddressBookBinding binding;
    RecyclerView recyclerView;
    AddressAdapter adapter;
    AddressAdapter.IEditAddressOnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBookBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_address_book);

        recyclerView = findViewById(R.id.recycler_address_book);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddressBookActivity.this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        int uid = AppUtils.getCurrentUser().getId();
        List<Address> lst = AddressDAO.get_by_user(uid);
        Log.d("USER",uid+"");
        Log.d("LIST ADDRESS",lst.size() + "");
        for (Address a: lst)
        {
            //Log.d("ADDRESS", a.getId() + "-" + UserAccountDAO.get_by_id(a.getUser()).getAddress());
            //Log.d("ADDRESS", AddressDAO.isDefault(a) + "");
            Log.d("ADDRESS", a.getName() + a.getWar()  + a.getDis() + a.getPro() + "" + a.getHome());
        }

        CircleImageView btn = findViewById(R.id.btn_add_new_address_book);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddressBookActivity.this,AddNewAdressActivity.class);
                someActivityResultLauncher.launch(i);
            }
        });

        adapter = new AddressAdapter(getLayoutInflater());
        listener = new AddressAdapter.IEditAddressOnClickListener() {
            @Override
            public void EditAddressOnClickListener(ImageView view, Address ad) {
                Intent i = new Intent(AddressBookActivity.this,AddNewAdressActivity.class);
                i.putExtra("Address",ad.getId());
                someActivityResultLauncher.launch(i);
            }
        };

        adapter.setData(lst, listener);
        recyclerView.setAdapter(adapter);
    }

    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int CAMERA_PIC_REQUEST = 251;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        String r = (String) data.getExtras().get("result");
                        if (r.equals("OK")) {
                            adapter.setData(AddressDAO.get_by_user(AppUtils.getCurrentUser().getId()),listener);
                        }
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED)
                        AppUtils.showMessage(getContext(), "Action canceled");
                    else
                        AppUtils.showMessage(getContext(), "Action Failed");
                }
            });

    Context getContext()
    {
        return AddressBookActivity.this;
    }
}
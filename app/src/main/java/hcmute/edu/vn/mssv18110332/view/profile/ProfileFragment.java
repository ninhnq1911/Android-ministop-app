package hcmute.edu.vn.mssv18110332.view.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.activity.AddressBookActivity;
import hcmute.edu.vn.mssv18110332.activity.ChangePasswordActivity;
import hcmute.edu.vn.mssv18110332.activity.RegisterActivity;
import hcmute.edu.vn.mssv18110332.activity.UpdateInfoActivity;
import hcmute.edu.vn.mssv18110332.helper.ConfirmDialog;
import hcmute.edu.vn.mssv18110332.model.Useraccount;
import hcmute.edu.vn.mssv18110332.databinding.FragmentProfileBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    StorageReference storageReference;
    OnBackPressedCallback callback;
    long pressBackTime;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback =
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (System.currentTimeMillis() - pressBackTime < 2000)
                            getActivity().finish();
                        else
                            Toast.makeText(getContext(), "Nhấn back một lần nữa để thoát!", Toast.LENGTH_SHORT).show();
                        pressBackTime = System.currentTimeMillis();
                    }
                };
        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        Useraccount u  = AppUtils.getCurrentUser();
        binding.setUser(u);
        Log.d("USER",u.getEmail() + "-" + u.getPass() + "-" + u.getPhonenumber() + "-" + u.getAddress());
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+u.getId()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Picasso.get().load(uri).into(binding.imgAvatarProfile);
                Glide.with(getContext()).load(uri.toString()).into(binding.imgAvatarProfile);
            }
        });
        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(getActivity(),UpdateInfoActivity.class);
                 someActivityResultLauncher.launch(i);
             }
        });
        binding.btnAddressProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddressBookActivity.class);
                someActivityResultLauncher.launch(i);
            }
        });
        binding.btnPasswordProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
                someActivityResultLauncher.launch(i);
            }
        });
        binding.btnLogoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog confirmDialog = new ConfirmDialog(getContext());
                confirmDialog.setConfirmCLickListener(new ConfirmDialog.onConfirmCLickListener() {
                    @Override
                    public void onClick(boolean result) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
                confirmDialog.showDialog(Gravity.CENTER,
                        "xác nhận đăng xuất",
                        "Bạn có thật sự muốn đăng xuất khỏi ứng dụng hay không?");
            }
        });
        View root = binding.getRoot();
        return root;
    }

    public void updateProfile(View v)
    {
        Intent i = new Intent(getActivity(), UpdateInfoActivity.class);
        startActivity(i);
    }

    public void updateUI(Useraccount u)
    {
        binding.setUser(u);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
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
                            binding.setUser(AppUtils.getCurrentUser());
                            binding.invalidateAll();
                        }
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED)
                    {
                        AppUtils.showMessage(getContext(), "Action canceled");
                        binding.setUser(AppUtils.getCurrentUser());
                        binding.invalidateAll();
                    }
                    else
                        AppUtils.showMessage(getContext(), "Action Failed");
                }
            });


}

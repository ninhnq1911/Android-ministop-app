package hcmute.edu.vn.mssv18110332.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ActivityRegisterBinding;
import hcmute.edu.vn.mssv18110332.databinding.ActivityUpdateInfoBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.Gmail;
import hcmute.edu.vn.mssv18110332.helper.ProgressDialog;
import hcmute.edu.vn.mssv18110332.helper.Session;
import hcmute.edu.vn.mssv18110332.model.Address;
import hcmute.edu.vn.mssv18110332.model.Useraccount;
import hcmute.edu.vn.mssv18110332.DAO.*;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UpdateInfoActivity extends AppCompatActivity {

    ActivityUpdateInfoBinding binding;
    Uri filePath;
    FirebaseStorage storage;
    boolean selectOK;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    final ProgressDialog pd = new ProgressDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+AppUtils.getCurrentUser().getId()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Picasso.get().load(uri).into(binding.imgAvatarProfile);
                Glide.with(UpdateInfoActivity.this).load(uri.toString()).into(binding.imgAvatarProfile);
            }
        });

        binding.setUser(AppUtils.getCurrentUser());
        binding.btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Useraccount u = binding.getUser();
                u.setFullname(binding.prfTxtFullName.getText().toString());
                String[] s = u.getFullname().split(" ");
                u.setName(s[s.length-1]);
                u.setBirth(binding.txtBirthProfile.getText().toString());
                UserAccountDAO.update(u);
                if (binding.radioMaleInfo.isChecked())
                    u.setGender(0);
                else
                    u.setGender(1);
                UserAccountDAO.update(u);

                String phone = u.getPhonenumber();
                if (!phone.equals(binding.txtPhoneProfile.getText().toString()))
                {
                    Intent i = new Intent(UpdateInfoActivity.this, PhoneVerifyActivity.class);
                    i.putExtra("phone",binding.txtPhoneProfile.getText().toString());
                    someActivityResultLauncher.launch(i);
                }

                Intent i = new Intent();
                i.putExtra("result","OK");
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });

        binding.btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
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
                        AppUtils.showMessage(getContext(), "Action canceled");
                    else
                        AppUtils.showMessage(getContext(), "Action Failed");
                }
            });

    ActivityResultLauncher<Intent> SelectImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data == null)
                            return;
                        filePath = data.getData();
                        try {
                            // Setting image on image view using Bitmap
                            Bitmap bitmap = MediaStore
                                    .Images
                                    .Media
                                    .getBitmap(
                                            getContentResolver(),
                                            filePath);
                            //binding.imgAvatarProfile.setImageBitmap(bitmap);
                            selectOK = true;
                            uploadImage(filePath);
                        }
                        catch (IOException e) {
                            // Log the exception
                            e.printStackTrace();
                            pd.hide_progress_dialog();
                        }
                    }
                    else {
                        pd.hide_progress_dialog();
                        if (result.getResultCode() == Activity.RESULT_CANCELED)
                            AppUtils.showMessage(getContext(), "Action canceled");
                        else
                            AppUtils.showMessage(getContext(), "Action Failed");
                    }
                }
            });

    Context getContext()
    {
        return UpdateInfoActivity.this;
    }

    private final int PICK_IMAGE_REQUEST = 22;
    private void SelectImage()
    {
        pd.show_progress_dialog(getContext());
        filePath = null;
        selectOK = false;
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent = Intent.createChooser(intent, "Select Image from here...");
        SelectImageActivityResultLauncher.launch(intent);
    }

    // UploadImage method
    private void uploadImage(Uri imageUri)
    {
        // adding listeners on upload
        // or failure of image
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+AppUtils.getCurrentUser().getId()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Picasso.get().load(uri).into(binding.imgAvatarProfile);
                        Glide.with(UpdateInfoActivity.this).load(uri.toString()).into(binding.imgAvatarProfile);
                        pd.hide_progress_dialog();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.hide_progress_dialog();
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
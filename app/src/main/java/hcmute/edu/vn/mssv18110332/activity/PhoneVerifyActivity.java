package hcmute.edu.vn.mssv18110332.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.FireBaseUtils;
import hcmute.edu.vn.mssv18110332.helper.ProgressDialog;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class PhoneVerifyActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuth";

    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendButton;
    private Button resendButton;
    String number;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;
    private FirebaseUser fu;
    Useraccount ua;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        String phone = (String) getIntent().getExtras().get("phone");
        phoneText = (EditText) findViewById(R.id.phoneText);
        phoneText.setFocusable(false);
        codeText = (EditText) findViewById(R.id.codeText);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        resendButton = (Button) findViewById(R.id.resendButton);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);
        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);
        fbAuth = FirebaseAuth.getInstance();
        fu = fbAuth.getCurrentUser();
        ua = AppUtils.getCurrentUser();
        phoneText.setText(phone);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(v);
            }
        });
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode(v);
            }
        });
    }

    public void sendCode(View view) {
        ProgressDialog pd = new ProgressDialog();
        pd.show_progress_dialog(PhoneVerifyActivity.this);
        number = ccp.getFullNumberWithPlus();
        if (UserAccountDAO.get_by_phone(number)!=null)
        {
            Toast.makeText(PhoneVerifyActivity.this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
            pd.hide_progress_dialog();
            return;
        }
        Log.d(TAG,"Send code to Phone: " + number);
        setUpVerificatonCallbacks();
        Log.d(TAG,"Setup call back complete");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fbAuth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(PhoneVerifyActivity.this) // Activity (for callback binding)
                        .setCallbacks(verificationCallbacks)
                        .setForceResendingToken(resendToken)
                        .build();
        Log.d(TAG,"Setup authentication option completed");
        PhoneAuthProvider.verifyPhoneNumber(options);
        Log.d(TAG,"Verify completely");
        pd.hide_progress_dialog();
    }

    private void setUpVerificatonCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                resendButton.setEnabled(false);
                verifyButton.setEnabled(false);
                codeText.setText("");
                signInWithPhoneAuthCredential(credential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.w(TAG, "Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.w(TAG,  "Quota exceeded.");
                }
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                phoneVerificationId = verificationId;
                resendToken = token;
                verifyButton.setEnabled(true);
                sendButton.setEnabled(false);
                resendButton.setEnabled(true);
            }
        };
    }

    public void verifyCode(View view) {
        ProgressDialog pd = new ProgressDialog();
        pd.show_progress_dialog(PhoneVerifyActivity.this);
        String code = codeText.getText().toString();
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        pd.hide_progress_dialog();
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            codeText.setText("");
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();
                            String phoneNumber = user.getPhoneNumber();
                            ua.setPhonenumber(phoneNumber);
                            UserAccountDAO.update(ua);
                            Intent i = new Intent();
                            i.putExtra("result","OK");
                            setResult(Activity.RESULT_OK,i);
                            finish();
                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(PhoneVerifyActivity.this,"Your verify code is wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void resendCode(View view) {
        ProgressDialog pd = new ProgressDialog();
        pd.show_progress_dialog(PhoneVerifyActivity.this);
        number = ccp.getFullNumberWithPlus();
        setUpVerificatonCallbacks();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity)this) // Activity (for callback binding)
                        .setCallbacks(verificationCallbacks)
                        .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        pd.hide_progress_dialog();
    }

    public void signOut(View view) {
        fbAuth.signOut();
        sendButton.setEnabled(true);
    }

    Context getContext()
    {
        return PhoneVerifyActivity.this;
    }
}
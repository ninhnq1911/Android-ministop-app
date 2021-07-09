package hcmute.edu.vn.mssv18110332.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.activity.HomeActivity;
import hcmute.edu.vn.mssv18110332.activity.LoginActivity;
import hcmute.edu.vn.mssv18110332.activity.RegisterActivity;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

import static hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO.*;

public class FireBaseUtils {

     private static FirebaseAuth auth;

    public static void register_email(Context context, String e, String p)
    {
        final ProgressDialog pd = new ProgressDialog();
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.show_progress_dialog(context);
            }
        });
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        Toast.makeText(context, "createUserWithEmail:onComplete:" + task.isSuccessful(),
                                Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            pd.hide_progress_dialog();
                            Toast.makeText(context, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            createNewAccount(e, p);
                            AppUtils.sendEmail(context,"Your account has been registered with us", e);
                            Useraccount u = UserAccountDAO.get_by_email(e);
                            if (u!=null)
                            {
                                AppUtils.showMessage(context,"Tạo tài khoản thành công");
                                //Session.store(context.getApplicationContext(),"Authenticated","true");
                                //Session.store(context.getApplicationContext(),"UserID",String.valueOf(u.getId()));
                                //Session.store(context.getApplicationContext(),"UserName",u.getName());
                            }
                            context.startActivity(new Intent(context, LoginActivity.class));
                            pd.hide_progress_dialog();
                            ((Activity)context).finish();
                        }
                    }
                });
    }

    public static void reset_password(Context context, String e, String p)
    {
        auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(e)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "We have sent you instructions to reset your password!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to send reset email!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void login_email(Context context, String e, String p)
    {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (p.length() < 6) {
                                Toast.makeText(context, "Password must be at least 6 characters",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Authentication Failed", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Authentication succeeded", Toast.LENGTH_LONG).show();
                           // Intent intent = new Intent(context, HomeActivity.class);
                            //context.startActivity(intent);
                        }
                    }
                });
    }

    public static void change_email(Context context, FirebaseUser u, String e)
    {
        auth = FirebaseAuth.getInstance();
        if (u != null && !e.isEmpty()) {
            u.updateEmail(e.trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                signOut();
                            } else {
                                Toast.makeText(context, "Failed to update email!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else if (e.isEmpty()) {
            Toast.makeText(context, "New email can't be empty", Toast.LENGTH_LONG).show();
        }
    }

    public static void change_password(Context context, FirebaseUser u, String p)
    {
        if (u != null && !p.trim().isEmpty()) {
            if (p.trim().length() < 6) {
                Toast.makeText(context,"Password too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                u.updatePassword(p.trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                    change_password_email(context,u);
                                    Useraccount us = UserAccountDAO.get_by_email(u.getEmail());
                                    us.setPass(p);
                                    UserAccountDAO.update(us);
                                    //signOut();
                                } else {
                                    Toast.makeText(context, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else if (p.trim().isEmpty()) {
            Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show();
        }
    }

    public static void change_password_email(Context context, FirebaseUser u)
    {
        auth = FirebaseAuth.getInstance();
        String e = u.getEmail();
        if (!e.isEmpty()) {
            auth.sendPasswordResetEmail(e.trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(context,"Failed to send reset email!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void change_password_email(Context context, String e)
    {
        auth = FirebaseAuth.getInstance();
        if (!e.isEmpty()) {
            auth.sendPasswordResetEmail(e.trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(context,"Failed to send reset email!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void remove_user(Context context, FirebaseUser u)
    {
        if (u != null) {
            u.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, RegisterActivity.class));
                                ((Activity)context).finish();
                            } else {
                                Toast.makeText(context, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public static void phone_verify(Context context, FirebaseUser u)
    {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        String TAG = "Phone verify";

        // Initialize phone auth callbacks
        // This callback will be invoked in two situations:
        // 1 - Instant verification. In some cases the phone number can be instantly
        //     verified without needing to send or enter a verification code.
        // 2 - Auto-retrieval. On some devices Google Play services can automatically
        //     detect the incoming verification SMS and perform verification without
        //     user action.
        // Update the UI and attempt sign in with the phone credential
        // This callback is invoked in an invalid request for verification is made,
        // for instance if the the phone number format is not valid.
        // Invalid request
        // The SMS quota for the project has been exceeded
        // Show a message and update the UI
        // The SMS verification code has been sent to the provided phone number, we
        // now need to ask the user to enter the code and then construct a credential
        // by combining the code with a verification ID.
        // Save verification ID and resending token so we can use them later
        // Update UI
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            public String mVerificationId;
            public PhoneAuthProvider.ForceResendingToken mResendToken;
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                //mVerificationInProgress = false;

                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                //signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                //mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.w(TAG, "Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.w(TAG,  "Quota exceeded.");
                }

                // Show a message and update the UI
                //updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // Update UI
                //updateUI(STATE_CODE_SENT);
            }
        };
    }

    /*public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }*/

    /*@Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
        }
    }*/

    private void startPhoneNumberVerification(Context context, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks, String phoneNumber) {
        auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity)context) // Activity (for callback binding)
                        .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        //signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(Context context, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks, String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity)context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /*private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mBinding.fieldVerificationCode.setError("Invalid code.");
                            }
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                        }
                    }
                });
    }*/

    public static void email_verify(Context context, FirebaseUser u)
    {
        String TAG = "Email verify";
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re-enable btn_new_address
                        //mBinding.verifyEmailButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            //pd.hide_progress_dialog();
                            Toast.makeText(context,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //pd.hide_progress_dialog();
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(context,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void signOut()
    {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
    }
}

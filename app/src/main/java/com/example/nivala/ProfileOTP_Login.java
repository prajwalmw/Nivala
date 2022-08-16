package com.example.nivala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nivala.databinding.ActivityProfileOtpLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ProfileOTP_Login extends AppCompatActivity {
    private ActivityProfileOtpLoginBinding binding;
    private FirebaseAuth mauth;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileOtpLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // OTP Login support is added.
        mauth = FirebaseAuth.getInstance();

        binding.sendOtpBtn.setOnClickListener(v -> {
            String mobileString = binding.mobileNoBox.getText().toString().trim();
            if(mobileString.isEmpty() || mobileString.length() < 10) {
                binding.mobileNoBox.setError("Enter a valid mobile number");
                binding.mobileNoBox.requestFocus();
                return;
            }
            else {
                //getting mobile number from user entered flow and passing it to verification
                phone_verification(mobileString);
            }
        });

        binding.continueBtn.setOnClickListener(v -> {
            String edit_otp = binding.otpBox.getText().toString();
            if(edit_otp.isEmpty() || edit_otp.length() < 6)
            {
                binding.otpBox.setError("Enter valid code");
                binding.otpBox.requestFocus();
            }
            verify_VerificationCode(edit_otp); // use the otp to verify and login.
        });
    }

    /**This function is used to verify the mobile number
     * of the user entered.
     * @param mobile
     * @return void
     */
    private void phone_verification(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                ProfileOTP_Login.this,
                callbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                binding.otpBox.setText(code);
                //verifying the code
                verify_VerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ProfileOTP_Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            mVerificationId = verificationId; // type: String
        }

    };

    /**This function takes the otp code as parameter and verifies it.
     * @param code
     */
    private void verify_VerificationCode(String code) {
        //credenials created
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //user signing in
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential mcredential) {
        mauth.signInWithCredential(mcredential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // get id here and send that to main activity.
                    Intent intent = new Intent(ProfileOTP_Login.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ProfileOTP_Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }});
    }


}
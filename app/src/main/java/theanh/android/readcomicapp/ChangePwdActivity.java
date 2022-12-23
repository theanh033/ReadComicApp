package theanh.android.readcomicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePwdActivity extends AppCompatActivity {

    private EditText edtCurrentPwd, edtNewPwd, edtConfirmNewPwd;
    private Button btnVerified, btnChangePwd;
    private TextView txvVerifiedNotice;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private String userCurrPwd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        getSupportActionBar().setTitle("Change Password");

        edtCurrentPwd = findViewById(R.id.cPwdCurrentPwd);
        edtNewPwd = findViewById(R.id.cPwdNewPwd);
        edtConfirmNewPwd = findViewById(R.id.cPwdConfirmPwd);
        edtNewPwd.setEnabled(false);
        edtConfirmNewPwd.setEnabled(false);

        btnVerified = findViewById(R.id.verified);

        txvVerifiedNotice = findViewById(R.id.verifiedNotice);
        progressBar = findViewById(R.id.progessBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser.equals("")) {
            Toast.makeText(ChangePwdActivity.this, "Somwthing went wrong! User's details are not available", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChangePwdActivity.this, MainActivity.class));
            finish();
        } else {
            verified(firebaseUser);
        }

        btnChangePwd = findViewById(R.id.changePwd);
        btnChangePwd.setEnabled(false);
    }

    private void verified(FirebaseUser firebaseUser) {
        btnVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCurrPwd = edtCurrentPwd.getText().toString().trim();

                if (TextUtils.isEmpty(userCurrPwd)) {
                    Toast.makeText(ChangePwdActivity.this, "Password is needed!", Toast.LENGTH_SHORT).show();
                    edtCurrentPwd.setError("Please enter your current password");
                    edtCurrentPwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userCurrPwd);
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                edtCurrentPwd.setEnabled(false);
                                btnVerified.setEnabled(false);
                                btnVerified.setBackgroundTintList(ContextCompat.getColorStateList(ChangePwdActivity.this, R.color.lightblack));

                                edtNewPwd.setEnabled(true);
                                edtConfirmNewPwd.setEnabled(true);
                                btnChangePwd.setEnabled(true);

                                txvVerifiedNotice.setText("You are verified");
                                txvVerifiedNotice.setTextColor(Color.rgb(124, 252, 0));
                                Toast.makeText(ChangePwdActivity.this, "Password has been verified." + "Change password now", Toast.LENGTH_SHORT).show();

                                btnChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(ChangePwdActivity.this, R.color.black));
                                btnChangePwd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(ChangePwdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userNewPwd = edtNewPwd.getText().toString().trim();
        String userConfirmNewPwd = edtConfirmNewPwd.getText().toString().trim();

        if (TextUtils.isEmpty(userNewPwd)) {
            Toast.makeText(ChangePwdActivity.this, "New password is needed", Toast.LENGTH_SHORT).show();
            edtNewPwd.setError("Please enter your new password");
            edtNewPwd.requestFocus();
        } else if (TextUtils.isEmpty(userConfirmNewPwd)) {
            Toast.makeText(ChangePwdActivity.this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
            edtConfirmNewPwd.setError("Please re-enter your new password");
            edtConfirmNewPwd.requestFocus();
        } else if (!userNewPwd.matches(userConfirmNewPwd)) {
            Toast.makeText(ChangePwdActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
            edtConfirmNewPwd.setError("Please re-enter same password");
            edtConfirmNewPwd.requestFocus();
        } else if (userCurrPwd.equals(userNewPwd)) {
            Toast.makeText(ChangePwdActivity.this, "New password cannot be same ass old password", Toast.LENGTH_SHORT).show();
            edtNewPwd.setError("Please enter a new password");
            edtNewPwd.requestFocus();
        } else {
            firebaseUser.updatePassword(userNewPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePwdActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(ChangePwdActivity.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(ChangePwdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
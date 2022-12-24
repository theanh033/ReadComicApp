package theanh.android.readcomicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPwdActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnResetPwd, btnCancel;
    private FirebaseAuth authProfile;
    private final static String TAG = "ResetPwdActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        getSupportActionBar().setTitle("Forgot Password");

        edtEmail = findViewById(R.id.edtFPEmail);
        btnResetPwd = findViewById(R.id.btnFPResetPwd);

        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPwdActivity.this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Email is required");
                    edtEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPwdActivity.this, "Please enter valid email!", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Valid email is required");
                    edtEmail.requestFocus();
                } else {
                    resetPassword(email);
                }
            }

            private void resetPassword(String email) {
                authProfile = FirebaseAuth.getInstance();
                authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPwdActivity.this, "Please check for password reset link", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ForgotPwdActivity.this, SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                edtEmail.setError("User does not exist or no longer valid. Please sign up again!");
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(ForgotPwdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        btnCancel = findViewById(R.id.backToLogin);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPwdActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });
    }
}
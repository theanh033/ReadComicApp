package theanh.android.readcomicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

import theanh.android.readcomicapp.Object.Reader;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnSignup;
    private TextView txvLogin;
    private ImageView imgShowHidePwd;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private static final String TAG = "SignUpActivity";
    String userID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Signup");

        edtUsername = findViewById(R.id.signUpUsername);
        edtEmail = findViewById(R.id.signUpEmail);
        edtPassword = findViewById(R.id.signUpPassword);
        btnSignup = findViewById(R.id.btnSignup);
        txvLogin = findViewById(R.id.login);
        imgShowHidePwd = findViewById(R.id.sign_up_show_hide_pwd);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        showHidePwd();
        inItUi();
    }

    private void showHidePwd() {
        imgShowHidePwd.setImageResource(R.drawable.ic_baseline_lock_24);
        imgShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowHidePwd.setImageResource(R.drawable.ic_baseline_lock_24);
                } else {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowHidePwd.setImageResource(R.drawable.ic_baseline_lock_open_24);
                }
            }
        });
    }

    private void inItUi() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your username.", Toast.LENGTH_SHORT).show();
                    edtUsername.setError("Username is required");
                    edtUsername.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Email is required");
                    edtEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Valid email is required");
                    edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                    edtPassword.setError("Password is required");
                    edtPassword.requestFocus();
                } else if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    edtPassword.setError("Password too weak");
                    edtPassword.requestFocus();
                } else if (username.contains("admin")) {
                    Toast.makeText(SignUpActivity.this, "You can't use this type of username. Please choose another username!", Toast.LENGTH_SHORT).show();
                    edtUsername.setError("Invalid username");
                    edtUsername.requestFocus();
                } else if (email.contains("admin")) {
                    Toast.makeText(SignUpActivity.this, "You can't use this type of email. Please choose another email!", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Invalid email");
                    edtEmail.requestFocus();
                } else {
                    signUp(username, email, password);
                }
            }
        });

        txvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }

    private void signUp(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    finish();

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    Reader reader = new Reader(username, email);
                    DatabaseReference refProfile = FirebaseDatabase.getInstance().getReference("User");
                    refProfile.child(firebaseUser.getUid()).setValue(reader).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                userID = mAuth.getCurrentUser().getUid();
//                                DocumentReference documentReference = fStore.collection("users").document(userID);
//                                documentReference.set(reader.map()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Log.d(TAG, "onSuccess: user Profile is created for" + userID);
//                                    }
//                                });
                                Toast.makeText(SignUpActivity.this, "You can log in now.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign up fail! Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        edtPassword.setError("Your password is too weak. Please choose a stronger password!");
                        edtPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        edtEmail.setError("Your email is invalid or already in use. Please re-enter!");
                        edtEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        edtEmail.setError("User is already registered with this email. Use another email!");
                        edtEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
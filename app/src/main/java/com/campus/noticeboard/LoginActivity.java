/*
 * Student ID: 2024EB01570
 * Course: Programming in Mobile Devices - Staff Graded Assignment 2
 * Java LoginActivity implementation
 */
package com.campus.noticeboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.campus.noticeboard.utils.NetworkHelper;
import com.campus.noticeboard.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SharedPreferencesHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sessionHelper = new SharedPreferencesHelper(this);

        // Auto-login if session exists
        if (sessionHelper.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Internet Connectivity Check
        if (!NetworkHelper.checkNetworkAndShowToast(this)) {
            return; // Toast: "Please connect to the internet."
        }

        // Firebase Auth: signInWithEmailAndPassword()
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            fetchRoleAndProceed(user.getUid(), email);
                        } else {
                            sessionHelper.saveUserSession(email, SharedPreferencesHelper.ROLE_STUDENT);
                            navigateToWelcomeScreen();
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                        Toast.makeText(LoginActivity.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void fetchRoleAndProceed(String uid, String email) {
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String role = SharedPreferencesHelper.ROLE_STUDENT;
                if (snapshot.exists() && snapshot.child("role").getValue(String.class) != null) {
                    role = snapshot.child("role").getValue(String.class);
                }

                // Save Email and Role in Shared Preferences
                sessionHelper.saveUserSession(email, role);

                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                navigateToWelcomeScreen();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                sessionHelper.saveUserSession(email, SharedPreferencesHelper.ROLE_STUDENT);
                navigateToWelcomeScreen();
            }
        });
    }

    private void navigateToWelcomeScreen() {
        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        finish();
    }
}

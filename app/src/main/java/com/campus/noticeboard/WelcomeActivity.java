/*
 * Student ID: 2024EB01570
 * Course: Programming in Mobile Devices - Staff Graded Assignment 2
 * Java WelcomeActivity implementation
 */
package com.campus.noticeboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.campus.noticeboard.services.NotificationService;
import com.campus.noticeboard.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvWelcomeEmail, tvUserRole, tvStudentIdHeader;
    private Button btnGoToNotices, btnLogout;

    private SharedPreferencesHelper sessionHelper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sessionHelper = new SharedPreferencesHelper(this);
        mAuth = FirebaseAuth.getInstance();

        // Retrieve saved user details from Shared Preferences
        String email = sessionHelper.getUserEmail();
        String role = sessionHelper.getUserRole();

        tvWelcomeEmail = findViewById(R.id.tvWelcomeEmail);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvStudentIdHeader = findViewById(R.id.tvStudentIdHeader);
        btnGoToNotices = findViewById(R.id.btnGoToNotices);
        btnLogout = findViewById(R.id.btnLogout);

        // Display user email, role, and Student ID
        tvWelcomeEmail.setText("Welcome, " + email);
        tvUserRole.setText("Role: " + role);
        tvStudentIdHeader.setText("Student ID: 2024EB01570");

        // Navigate to NoticeActivity
        btnGoToNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, NoticeActivity.class));
            }
        });

        // Logout Button Handler
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

        // Start Background Android Service for Periodic Notifications
        startService(new Intent(this, NotificationService.class));
    }

    private void handleLogout() {
        // 1. Clear Shared Preferences
        sessionHelper.clearSession();

        // 2. Sign out from Firebase
        mAuth.signOut();

        // 3. Stop background notification service
        stopService(new Intent(this, NotificationService.class));

        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();

        // 4. Return to RegisterActivity
        Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

/*
 * Student ID: 2024EB01570
 * Course: Programming in Mobile Devices - Staff Graded Assignment 2
 * Java NoticeActivity implementation (Publish & Retrieve Notices via Firebase)
 */
package com.campus.noticeboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.campus.noticeboard.adapters.NoticeAdapter;
import com.campus.noticeboard.models.Notice;
import com.campus.noticeboard.utils.NetworkHelper;
import com.campus.noticeboard.utils.SharedPreferencesHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    private CardView cardPublishSection, cardStudentInfo;
    private EditText etNoticeTitle, etNoticeDescription;
    private Button btnPublishNotice;
    private TextView tvEmptyNoticeState, tvStudentIdHeader;
    private RecyclerView rvNoticeList;

    private NoticeAdapter noticeAdapter;
    private List<Notice> noticeList;

    private DatabaseReference mNoticeDatabase;
    private SharedPreferencesHelper sessionHelper;
    private String userEmail;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        sessionHelper = new SharedPreferencesHelper(this);
        userEmail = sessionHelper.getUserEmail();
        userRole = sessionHelper.getUserRole();

        mNoticeDatabase = FirebaseDatabase.getInstance().getReference("notices");

        // Initialize Views
        tvStudentIdHeader = findViewById(R.id.tvStudentIdHeader);
        cardPublishSection = findViewById(R.id.cardPublishSection);
        cardStudentInfo = findViewById(R.id.cardStudentInfo);
        etNoticeTitle = findViewById(R.id.etNoticeTitle);
        etNoticeDescription = findViewById(R.id.etNoticeDescription);
        btnPublishNotice = findViewById(R.id.btnPublishNotice);
        tvEmptyNoticeState = findViewById(R.id.tvEmptyNoticeState);
        rvNoticeList = findViewById(R.id.rvNoticeList);

        tvStudentIdHeader.setText("Student ID: 2024EB01570");

        // Setup RecyclerView
        noticeList = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(noticeList);
        rvNoticeList.setLayoutManager(new LinearLayoutManager(this));
        rvNoticeList.setAdapter(noticeAdapter);

        // Apply Role-Based Access Control
        applyRoleAccessControl();

        // Publish Notice Click Handler
        btnPublishNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishNotice();
            }
        });

        // Retrieve Notices from Firebase Realtime Database
        fetchNoticesFromFirebase();
    }

    private void applyRoleAccessControl() {
        if (SharedPreferencesHelper.ROLE_STAFF.equalsIgnoreCase(userRole)) {
            // Staff User: Form visible & enabled
            cardPublishSection.setVisibility(View.VISIBLE);
            cardStudentInfo.setVisibility(View.GONE);
        } else {
            // Student User: Publish section hidden / disabled
            cardPublishSection.setVisibility(View.GONE);
            cardStudentInfo.setVisibility(View.VISIBLE);
        }
    }

    private void publishNotice() {
        String title = etNoticeTitle.getText().toString().trim();
        String description = etNoticeDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etNoticeTitle.setError("Title is required");
            etNoticeTitle.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            etNoticeDescription.setError("Description is required");
            etNoticeDescription.requestFocus();
            return;
        }

        // 1. Internet Connectivity Check before Firebase operation
        if (!NetworkHelper.checkNetworkAndShowToast(this)) {
            return; // Toast: "Please connect to the internet."
        }

        // 2. Store notice in Firebase Realtime Database
        String key = mNoticeDatabase.push().getKey();
        if (key == null) {
            key = String.valueOf(System.currentTimeMillis());
        }

        Notice notice = new Notice(
                key,
                title,
                description,
                userEmail,
                System.currentTimeMillis()
        );

        mNoticeDatabase.child(key).setValue(notice)
                .addOnSuccessListener(aVoid -> {
                    // Show Toast: "Notice Published Successfully."
                    Toast.makeText(NoticeActivity.this, "Notice Published Successfully.", Toast.LENGTH_SHORT).show();
                    etNoticeTitle.setText("");
                    etNoticeDescription.setText("");
                })
                .addOnFailureListener(e -> Toast.makeText(NoticeActivity.this, "Failed to publish notice: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void fetchNoticesFromFirebase() {
        // Check network connection before Firebase read
        if (!NetworkHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }

        mNoticeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notice notice = dataSnapshot.getValue(Notice.class);
                    if (notice != null) {
                        noticeList.add(notice);
                    }
                }

                // Show most recent notices at top
                Collections.reverse(noticeList);

                if (noticeList.isEmpty()) {
                    tvEmptyNoticeState.setVisibility(View.VISIBLE);
                    rvNoticeList.setVisibility(View.GONE);
                } else {
                    tvEmptyNoticeState.setVisibility(View.GONE);
                    rvNoticeList.setVisibility(View.VISIBLE);
                    noticeAdapter.updateNoticeList(noticeList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NoticeActivity.this, "Failed to load notices: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

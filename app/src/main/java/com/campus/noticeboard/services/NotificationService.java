/*
 * Student ID: 2024EB01570
 * Course: Programming in Mobile Devices - Staff Graded Assignment 2
 * Java Android Service for periodic background notifications
 */
package com.campus.noticeboard.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.campus.noticeboard.NoticeActivity;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "campus_notice_channel";
    private static final String CHANNEL_NAME = "Campus Notice Board Updates";
    private static final int NOTIFICATION_ID = 2024;
    private static final long PERIODIC_INTERVAL_MS = 15000L; // 15 seconds periodic trigger

    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startPeriodicNotification();
        return START_STICKY;
    }

    private void startPeriodicNotification() {
        runnable = new Runnable() {
            @Override
            public void run() {
                sendNotification();
                handler.postDelayed(this, PERIODIC_INTERVAL_MS);
            }
        };
        handler.post(runnable);
    }

    private void sendNotification() {
        Intent notificationIntent = new Intent(this, NoticeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Campus Notice Board")
                .setContentText("Check the Campus Notice Board for new updates!")
                .setSubText("Student ID: 2024EB01570")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for Campus Notice Board periodic updates");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

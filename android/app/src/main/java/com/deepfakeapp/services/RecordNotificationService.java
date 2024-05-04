package com.deepfakeapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.deepfakeapp.R;

public class RecordNotificationService extends Service {
    private static final int NOTIFICATION_ID = 2;
    private static final String CHANNEL_ID = "ScreenRecordServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Recording Screen",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Create a notification channel for Android Oreo and above
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // Create a notification for the foreground service
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Recording Service")
                    .setContentText("Recording in progress...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            startForeground(NOTIFICATION_ID, notification);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
package com.deepfakeapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;

import com.deepfakeapp.R;
import com.deepfakeapp.ScreenRecordActivity;

public class NotificationToOpenAppService extends Service {

    private int NOTIFICATION_ID;
    private String CHANNEL_ID;
    private CharSequence CHANNEL_NAME;
    private CharSequence TITLE;
    private CharSequence CONTENT_TEXT;

    // Create a notification channel for Android Oreo and above
    @Override
    public void onCreate() {
        super.onCreate();

        Resources res = getApplicationContext().getResources();
        NOTIFICATION_ID = res.getInteger(R.integer.NOTIFICATION_1);
        CHANNEL_ID = res.getString(R.string.CHANNEL_1);
        CHANNEL_NAME = res.getString(R.string.CHANNEL_NAME_1);
        TITLE = res.getString(R.string.OPEN_APP_RECOMMENDATION_TITLE);
        CONTENT_TEXT = res.getString(R.string.ASK_TO_OPEN_APP_MESSAGE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Intent activityIntent = new Intent(getApplicationContext(), ScreenRecordActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE);

        // Create a notification for the foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(TITLE)
                    .setContentText(CONTENT_TEXT)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
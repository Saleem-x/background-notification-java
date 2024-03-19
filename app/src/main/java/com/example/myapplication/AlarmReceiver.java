package com.example.myapplication;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//import androidx.core.app.NotificationCompat;



public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "NotificationChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm triggered. Checking for notifications...");

        // Check for notifications
        boolean hasNotification = checkForNotifications();

        if (hasNotification) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showNotification(context);
            }
        }
    }

    private boolean checkForNotifications() {
        // Perform your logic to check for notifications
        // Return true if there are notifications, false otherwise
        return true; // For demonstration purposes, always return true
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("notification_action", "navigate_to_second_fragment");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("hellooooo")
                .setContentText("notification varndooo "+ currentTime)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .build();


        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}


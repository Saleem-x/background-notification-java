package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmScheduler {
    public static void scheduleAlarm(Context context) {
        // Set up alarm to trigger AlarmReceiver every 5 minutes
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to start from current time and repeat every 5 minutes
        long triggerTime = System.currentTimeMillis();
        long interval = 1 * 60 * 1000;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, pendingIntent);
    }
}

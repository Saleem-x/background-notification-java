package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
                new ApiCallTask(context).execute();
            }
        }
    }

    private boolean checkForNotifications() {
        // Perform your logic to check for notifications
        // Return true if there are notifications, false otherwise
        return true; // For demonstration purposes, always return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void showNotification(Context context, String response) {
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

        // Intent to open MainActivity
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("notification_action", "navigate_to_second_fragment");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create PendingIntent to handle notification tap
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Cart Count Alert")
                .setContentText("Your Cart Have " + response+ " Items " +"at "+ currentTime )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        // Show notification
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private static class ApiCallTask extends AsyncTask<Void, Void, String> {
        private final Context mContext;

        public ApiCallTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return sendGetRequest();
            } catch (IOException e) {
                Log.e(TAG, "Error fetching data from API", e);
                return null;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showNotification(mContext, response);
                }
            } else {
                Log.e(TAG, "API response is null");
            }
        }
    }

    private static String sendGetRequest() throws IOException, JSONException {
        URL url = new URL("https://b2bproduct.dev-ts.online/App/CartCount?cusID=7&userID=3155");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray resultArray = jsonObject.getJSONArray("result");
            JSONObject countObject = resultArray.getJSONObject(0);
            String count = countObject.getString("count");
            return count;
        } else {
            throw new IOException("GET request failed with response code: " + responseCode);
        }
    }
}

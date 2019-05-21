package com.example.rikkeisoft.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;

import com.example.rikkeisoft.R;
import com.example.rikkeisoft.ui.main.MainActivity;
import com.example.rikkeisoft.util.Define;

public class SchedulingService extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";


    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, intent);
    }


    private void createNotification(Context context, Intent intent) {
        /**Creates an explicit intent for an Activity in your app**/
        String index = intent.getStringExtra(Define.KEY_TYPE);
        int id = intent.getIntExtra(Define.KEY_ID, 0);
        Intent resultIntent = new Intent(context
                , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                    id, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_note);
        mBuilder.setContentTitle(context.getString(R.string.app_name))
                .setContentText(index)
                .setAutoCancel(true)
                .setOnlyAlertOnce(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(id, mBuilder.build());
    }



}


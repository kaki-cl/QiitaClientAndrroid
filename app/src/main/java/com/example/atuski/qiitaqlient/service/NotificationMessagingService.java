package com.example.atuski.qiitaqlient.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.atuski.qiitaqlient.MainActivity;
import com.example.atuski.qiitaqlient.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // プッシュメッセージのdataに含めた値を取得
        Map<String, String> data = remoteMessage.getData();

        String message;
        if (data.get("default") != null) {
            message = data.get("default");
        } else {
            message = remoteMessage.getNotification().getBody();
        }

        try {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, "notify_001");
            Intent ii = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//            bigText.bigText("sssss");
            bigText.setBigContentTitle(message);
            bigText.setSummaryText("Text in detail");
            mBuilder.setContentIntent(pendingIntent);
//            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setSmallIcon(R.drawable.ic_stat_name);
            mBuilder.setContentTitle(message);
//            mBuilder.setContentText("setContentText");
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);

            NotificationManager mNotificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("notify_001",
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);

                mNotificationManager.createNotificationChannel(channel);
            }

//            startForeground(0, mBuilder.build());
            mNotificationManager.notify(0, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

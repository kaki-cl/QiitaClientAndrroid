package com.example.atuski.qiitaqlient.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.atuski.qiitaqlient.MainActivity;
import com.example.atuski.qiitaqlient.PushReceiveActivity;
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
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.v(TAG, String.valueOf(remoteMessage.getData().size()));
        if (remoteMessage.getNotification() != null) {
            Log.v("getBody", remoteMessage.getNotification().getBody());
        }

//        String message = remoteMessage.getNotification().getBody();


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // プッシュメッセージのdataに含めた値を取得
        Map<String, String> data = remoteMessage.getData();
        Log.v("default", data.get("default"));

        String message;
        if (data.get("default") != null) {
            message = data.get("default");
        } else {
            message = remoteMessage.getNotification().getBody();
        }


//=====================================================

//        Log.v("contentId", data.get("id"));
//        Log.v("contentType", data.get("type"));
//
//        String contentId = data.get("id");
//        String contentType = data.get("type");

//        // Notificationを生成
//        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext());
//        notificationCompatBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        notificationCompatBuilder.setContentTitle(getString(R.string.app_name));
//        notificationCompatBuilder.setContentText(data.get("default"));
//        notificationCompatBuilder.setDefaults(Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS);
//        notificationCompatBuilder.setAutoCancel(true);
////
////        // タップ時に呼ばれるIntentを生成
//        Intent intent = new Intent(this, PushReceiveActivity.class);
//        intent.putExtra(PushReceiveActivity.ARG_ID, "testId");
//        intent.putExtra(PushReceiveActivity.ARG_TYPE, "testType");
//        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationCompatBuilder.setContentIntent(contentIntent);
//
//        // 通知表示
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(346, notificationCompatBuilder.build());

//        案2
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 , intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Push通知のタイトル")
//                .setSubText("Push通知のサブタイトル")
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("default")))
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 , notificationBuilder.build());


//=====================================================

        try {
            // 案3
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
//
//
//
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

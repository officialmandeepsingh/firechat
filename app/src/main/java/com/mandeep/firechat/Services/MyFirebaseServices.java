package com.mandeep.firechat.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mandeep.firechat.R;

public class MyFirebaseServices extends FirebaseMessagingService {
    public MyFirebaseServices() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Notify(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }

    private void Notify(String title, String body) {

        /*int notification_id = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Simple Notification")
                .setContentText("This is a example of Simple Notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(false);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelid = "abc";
            NotificationChannel notificationChannel = new NotificationChannel(channelid, " welcome", 	NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channelid);

        }
        notificationManager.notify(notification_id, builder.build());*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelid = "abc";
            NotificationChannel notificationChannel = new NotificationChannel(channelid, " welcome", 	NotificationManager.IMPORTANCE_DEFAULT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_message_black_24dp)
                    .setContentText(body);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(2,builder.build());
        }else{

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_message_black_24dp)
                    .setContentText(body);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(3,builder.build());

        }


    }
}

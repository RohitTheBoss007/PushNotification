package com.example.android.firebasepushnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();
        String click_action=remoteMessage.getNotification().getClickAction();

        String messageData=remoteMessage.getData().get("message");
        String fromData=remoteMessage.getData().get("from_name");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("MyNotifications","MyNotifications",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);



        }
        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("message",messageData);
        resultIntent.putExtra("from_name",fromData);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logotiger)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setContentText(message);



        int mNotificationId=(int)System.currentTimeMillis();

        NotificationManagerCompat manager=NotificationManagerCompat.from(this);
        manager.notify(mNotificationId,builder.build());
    }
}

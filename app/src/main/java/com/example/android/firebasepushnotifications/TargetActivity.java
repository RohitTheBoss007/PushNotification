package com.example.android.firebasepushnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TargetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        String messageData=getIntent().getStringExtra("message");
        String fromData=getIntent().getStringExtra("from_name");
        String destination=getIntent().getStringExtra("destination");



        try {
            Class<?> c = Class.forName(destination);
            Intent resultIntent = new Intent(this, c);
            resultIntent.putExtra("message",messageData);
            resultIntent.putExtra("from_name",fromData);
            startActivity(resultIntent);

        } catch (ClassNotFoundException ignored) {

        }
    }
}

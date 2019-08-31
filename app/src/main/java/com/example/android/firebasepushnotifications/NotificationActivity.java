package com.example.android.firebasepushnotifications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
    TextView notif_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notif_data=findViewById(R.id.notif_data);
        String messageData=getIntent().getStringExtra("message");
        String fromData=getIntent().getStringExtra("from_name");
        notif_data.setText("FROM : "+fromData+"\n\nMESSAGE : "+messageData);
    }
}

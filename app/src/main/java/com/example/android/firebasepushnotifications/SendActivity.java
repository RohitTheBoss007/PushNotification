package com.example.android.firebasepushnotifications;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    TextView sendTo;
    EditText etMessage;
    Button sendbtn;

    FirebaseFirestore firestore;

    String user_name,user_id,cur_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_name=getIntent().getStringExtra("user_name");
        user_id=getIntent().getStringExtra("user_id");
        cur_id=FirebaseAuth.getInstance().getUid();
        firestore=FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_send);
        sendTo=findViewById(R.id.sendto);
        etMessage=findViewById(R.id.etmessage);
        sendbtn=findViewById(R.id.sendbtn);

        sendTo.setText(user_name);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=etMessage.getText().toString();
                if(!TextUtils.isEmpty(message))
                {
                    final ProgressDialog progressDialog = new ProgressDialog(SendActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Sending Notification Message...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Map<String,Object> notificationMessage=new HashMap<>();
                    notificationMessage.put("message",message);
                    notificationMessage.put("from",cur_id);
                    firestore.collection("Users").document(user_id).collection("Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(SendActivity.this,"Notification sent",Toast.LENGTH_SHORT).show();
                            etMessage.setText("");
                            progressDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SendActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });



                }
            }
        });
    }
}

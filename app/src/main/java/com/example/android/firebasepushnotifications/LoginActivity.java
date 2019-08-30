package com.example.android.firebasepushnotifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _emailText=(EditText)findViewById(R.id.input_email);
        _loginButton=(Button) findViewById(R.id.btn_login);
        _signupLink=(TextView) findViewById(R.id.link_signup);
        _passwordText=(EditText)findViewById(R.id.input_password);

        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=_emailText.getText().toString();
                String password=_passwordText.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
                {
                    _loginButton.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                        String tokenID=FirebaseInstanceId.getInstance().getToken();
                                        String currentID=mAuth.getCurrentUser().getUid();

                                        Map<String,Object> tokenMap=new HashMap<>();
                                        tokenMap.put("token_id",tokenID);
                                        firestore.collection("Users").document(currentID).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                _loginButton.setEnabled(true);
                                                progressDialog.dismiss();
                                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                finish();

                                            }
                                        });






                            }
                            else
                            {
                                _loginButton.setEnabled(true);
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }


            }
        });



        _signupLink.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View v) {
                // Start the SignUp activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();

        }
    }
}

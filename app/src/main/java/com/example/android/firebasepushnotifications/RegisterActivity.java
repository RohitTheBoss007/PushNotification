package com.example.android.firebasepushnotifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    EditText _nameText;

    EditText _emailText;

    EditText _passwordText;
    EditText _reEnterPasswordText;
    Button _signupButton;
    TextView _loginLink;
    FloatingActionButton fab;
    CircleImageView pic;
    private Uri mImageUri;

    StorageReference mStorage;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStorage=FirebaseStorage.getInstance().getReference().child("images");
        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        _nameText = (EditText) findViewById(R.id.input_name);

        _emailText = (EditText) findViewById(R.id.input_email);

        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        fab=findViewById(R.id.fab);
        pic=findViewById(R.id.pic);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _signupButton.setEnabled(false);
                if(mImageUri!=null)
                {



                    final String name=_nameText.getText().toString();
                    String email=_emailText.getText().toString();
                    String password=_passwordText.getText().toString();
                    String rePassword=_reEnterPasswordText.getText().toString();

                    if(!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && password.equals(rePassword))
                    {final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Creating Account...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();




                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {




                                if(task.isSuccessful()){
                                    final String user_id=mAuth.getCurrentUser().getUid();
                                    StorageReference userProfile=mStorage.child(user_id+".jpg");
                                    userProfile.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                final String downloadUrl=taskSnapshot.getDownloadUrl().toString();


                                                        String tokenID=FirebaseInstanceId.getInstance().getToken();
                                                        Map<String,Object> userMap=new HashMap<>();
                                                        userMap.put("name",name);
                                                        userMap.put("image",downloadUrl);
                                                        userMap.put("uid",user_id);
                                                        userMap.put("token_id",tokenID);
                                                        mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                _signupButton.setEnabled(true);
                                                                progressDialog.dismiss();

                                                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                                                finish();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                                _signupButton.setEnabled(true);
                                                                progressDialog.dismiss();

                                                            }
                                                        });

                                                    }
                                                });





                                }
                                else {
                                    Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    _signupButton.setEnabled(true);
                                    return;
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                _signupButton.setEnabled(true);
                                progressDialog.dismiss();

                            }
                        });

                    }
                    else
                        Toast.makeText(RegisterActivity.this,"Fill the fields correctly!",Toast.LENGTH_SHORT).show();
                        _signupButton.setEnabled(true);
                        return;
                }
                else
                    Toast.makeText(RegisterActivity.this,"Upload profile pic!",Toast.LENGTH_SHORT).show();
                    _signupButton.setEnabled(true);
                    return;
            }
        });


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
    private void openFileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            mImageUri=data.getData();
            Glide.with(this).load(mImageUri).into(pic);
        }
    }
}

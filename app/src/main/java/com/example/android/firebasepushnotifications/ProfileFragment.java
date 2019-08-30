package com.example.android.firebasepushnotifications;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Button mLogoutButton;
    FirebaseAuth mAuth;
    FirebaseFirestore mFireStore;

    CircleImageView circleImageView;
    TextView userName;
    String mUserId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth=FirebaseAuth.getInstance();
        mUserId=mAuth.getCurrentUser().getUid();
        mFireStore=FirebaseFirestore.getInstance();
        mLogoutButton=v.findViewById(R.id.logout);
        circleImageView=v.findViewById(R.id.dp);
        userName=v.findViewById(R.id.user_name);

        mFireStore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("name");
                String image=documentSnapshot.getString("image");

                userName.setText(name);
                Glide.with(container.getContext()).load(image).placeholder(R.drawable.pic).dontAnimate().into(circleImageView);

            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String,Object>tokenMapRemove =new HashMap<>();
                tokenMapRemove.put("token_id",FieldValue.delete());
                mFireStore.collection("Users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        startActivity(new Intent(container.getContext(),LoginActivity.class));
                    }
                });



            }
        });

        return v;
    }

}

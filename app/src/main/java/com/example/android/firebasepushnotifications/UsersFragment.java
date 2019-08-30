package com.example.android.firebasepushnotifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    RecyclerView mUserListrv;
    List<Users> usersList;
    UsersAdapter usersAdapter;

    FirebaseFirestore firestore;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_users, container, false);
        mUserListrv=v.findViewById(R.id.userList);
        usersList=new ArrayList<>();
        usersAdapter=new UsersAdapter(usersList,container.getContext());
        mUserListrv.setHasFixedSize(true);
        mUserListrv.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mUserListrv.setAdapter(usersAdapter);

        firestore=FirebaseFirestore.getInstance();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        usersList.clear();
        firestore.collection("Users").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("Error:",e.getMessage());
                }
                else {

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String user_id = doc.getDocument().getId();


                            Users users = doc.getDocument().toObject(Users.class);
                            usersList.add(users);
                            usersAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });
    }
}

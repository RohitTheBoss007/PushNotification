package com.example.android.firebasepushnotifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
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
public class NotificationFragment extends Fragment {

    RecyclerView notif_list;
    NotificationAdapter notificationAdapter;

    List<Notifications> mNotifList;
    FirebaseFirestore firestore;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_notification, container, false);
        mNotifList=new ArrayList<>();
        notif_list=v.findViewById(R.id.notif_list);
        notificationAdapter=new NotificationAdapter(container.getContext(),mNotifList);

        notif_list.setHasFixedSize(true);
        notif_list.setLayoutManager(new LinearLayoutManager(container.getContext()));
        notif_list.setAdapter(notificationAdapter);


        firestore=FirebaseFirestore.getInstance();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mNotifList.clear();

        String cur_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("Users").document(cur_id).collection("Notifications").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc:documentSnapshots.getDocumentChanges())
                {
                    Notifications notifications=doc.getDocument().toObject(Notifications.class);
                    mNotifList.add(notifications);
                    notificationAdapter.notifyDataSetChanged();
                }

            }
        });

    }
}

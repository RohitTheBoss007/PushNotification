package com.example.android.firebasepushnotifications;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder> {
    List<Notifications>mNotifList;
    FirebaseFirestore firestore;
    Context context;

    public NotificationAdapter(Context context,List<Notifications> mNotifList) {
        this.mNotifList = mNotifList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new NotificationAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapterViewHolder holder, int position) {
        firestore=FirebaseFirestore.getInstance();
        String from_id=mNotifList.get(position).getFrom();
        holder.mNotifMessage.setText(mNotifList.get(position).getMessage());
        firestore.collection("Users").document(from_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name=documentSnapshot.getString("name");
                String image=documentSnapshot.getString("image");

                holder.from_name.setText(name);
                Glide.with(context).load(image).placeholder(R.drawable.pic).dontAnimate().into(holder.circleImageView);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifList.size();
    }

    public class NotificationAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mNotifMessage,from_name;
        CircleImageView circleImageView;

        public NotificationAdapterViewHolder(View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.user_single_image);
            from_name=itemView.findViewById(R.id.user_single_name);
            mNotifMessage=itemView.findViewById(R.id.user_single_status);
        }
    }
}

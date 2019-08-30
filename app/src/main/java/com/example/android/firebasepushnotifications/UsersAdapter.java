package com.example.android.firebasepushnotifications;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder> {

    List<Users>usersList;
    Context context;


    public UsersAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UsersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapterViewHolder holder, int position) {
        final String user_name=usersList.get(position).getName();
        final String user_id=usersList.get(position).getUid();
        holder.userName.setText(user_name);
        Glide.with(context).load(usersList.get(position).getImage()).placeholder(R.drawable.pic).dontAnimate().into(holder.circleImageView);
        holder.lluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent=new Intent(context,SendActivity.class);
                sendIntent.putExtra("user_name",user_name);
                sendIntent.putExtra("user_id",user_id);
                context.startActivity(sendIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UsersAdapterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView userName;
        LinearLayout lluser;
        public UsersAdapterViewHolder(View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.dpuser);
            userName=itemView.findViewById(R.id.tvUser);
            lluser=itemView.findViewById(R.id.lluser);
        }
    }
}

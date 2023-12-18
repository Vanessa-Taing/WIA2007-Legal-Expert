package com.example.mad_login.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.MessageActivity;
import com.example.mad_login.Model.User;
import com.example.mad_login.R;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;


    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @androidx.annotation.NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mUsers.get(position);

        holder.username.setText(user.getName());

        //ImageViewer setImageURI() should not be ued with regular URIs. So we are using Picasso
        Picasso.get()
                .load(user.getImageURL())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.mad_cat)
                .into(holder.profile_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getUid();
                String userName = user.getName();
                String imageUrl = user.getImageURL();
                Log.d("UserAdapter", "Clicked on user with UID: " + userId);

                if (userId != null) {
                    Intent intent = new Intent(mContext, MessageActivity.class);
                    intent.putExtra("userid", userId);
                    intent.putExtra("username", userName);
                    intent.putExtra("imageUrl", imageUrl);
                    mContext.startActivity(intent);
                } else {
                    Log.e("UserAdapter", "User UID is null");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;

        public UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
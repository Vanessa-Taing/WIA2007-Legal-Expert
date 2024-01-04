package com.example.mad_login.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.MessageActivity;
import com.example.mad_login.Model.Chat;
import com.example.mad_login.Model.User;
import com.example.mad_login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;
    String theLastMessage;


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

        // Call lastMessage to set the last message for this user
        lastMessage(user.getUid(), holder.last_message, holder.time_stamp, holder.unread_message_bubble, holder.unread_message_count);

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
        private TextView last_message;
        private TextView time_stamp;
        private FrameLayout unread_message_bubble;
        private TextView unread_message_count;

        public UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            last_message = itemView.findViewById(R.id.last_message);
            time_stamp = itemView.findViewById(R.id.time_stamp);
            unread_message_bubble = itemView.findViewById(R.id.unread_message_bubble);
            unread_message_count = itemView.findViewById(R.id.unread_message_count);

        }
    }

    private void lastMessage(String userid, TextView last_message, TextView time_stamp, FrameLayout unread_message_bubble, TextView unread_message_count) {
        theLastMessage = "";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats");

        // Use AtomicReference to make the variable effectively final
        final AtomicReference<Chat> lastChat = new AtomicReference<>(null);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                int unreadCount = 0; // Initialize unread count

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null) {
                        if (!chat.isIsseen() && chat.getReceiver() != null && chat.getSender() != null
                                && chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)) {
                            // Check if the message is unread
                            unreadCount++;
                        }
                        if (chat.getReceiver() != null && chat.getSender() != null &&
                                (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                        chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid()))) {
                            // Set the lastChat when a matching message is found
                            lastChat.set(chat);
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (theLastMessage) {
                    case "":
                        last_message.setText(null);
                        time_stamp.setText(null);
                        break;

                    default:
                        last_message.setText(theLastMessage);
                        Chat chat = lastChat.get();
                        if (chat != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            String time = sdf.format(new Date(chat.getTimestamp()));
                            time_stamp.setText(time);
                        }
                        break;
                }

                // Update unread message count and visibility of the bubble
                if (unreadCount > 0) {
                    unread_message_bubble.setVisibility(View.VISIBLE);
                    unread_message_count.setText(String.valueOf(unreadCount));
                } else {
                    unread_message_bubble.setVisibility(View.GONE);
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

}
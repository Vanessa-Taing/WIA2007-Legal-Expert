package com.example.mad_login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.Model.Chat;
import com.example.mad_login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_LEFT =0;
    public static final int MSG_TYPE_RIGHT =1;
    private Context mContext;
    private List<Chat> mChat;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;
    }

    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
        } else{
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
        }
            return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MessageAdapter.@NonNull MessageViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        if (chat != null) {
            holder.show_message.setText(chat.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public MessageViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else{
            return MSG_TYPE_LEFT;
        }
    }
}


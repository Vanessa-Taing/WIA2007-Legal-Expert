package com.example.mad_login.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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
            // Check if the message contains a link
            if (mChat.get(position).getMessage().contains("click this link to see my profile:")) {
                // If it's a link, set HTML text and make the entire message clickable
                String messageWithLink = mChat.get(position).getMessage();
                Spanned spannedText = Html.fromHtml(messageWithLink);

                // Make the link clickable
                SpannableStringBuilder ssb = addClickablePart(spannedText, holder.show_message, messageWithLink);
                holder.show_message.setText(ssb);

                // Set movement method to make the link clickable
                holder.show_message.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                // If it's a regular message without a link, set plain text
                holder.show_message.setText(mChat.get(position).getMessage());
            }
        }

        if (position == mChat.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    private SpannableStringBuilder addClickablePart(Spanned strSpanned, TextView textView, String fullMessage) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        // Find the position of the link
        String linkPrefix = "click this link to see my profile: ";
        int startIdx = str.indexOf(linkPrefix) + linkPrefix.length();
        int endIdx = fullMessage.length();

        // Set a ClickableSpan to handle the click event
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Handle the click event, e.g., open the link
                String url = extractUrlFromMessage(fullMessage);
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    textView.getContext().startActivity(intent);
                }
            }
        }, startIdx, endIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssb;
    }

    private String extractUrlFromMessage(String message) {
        // Extract the URL from the message
        String prefix = "click this link to see my profile: ";
        int startIdx = message.indexOf(prefix) + prefix.length();
        return message.substring(startIdx);
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    private String getFormattedTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    private boolean isDifferentDay(long timestamp1, long timestamp2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(timestamp1);
        cal2.setTimeInMillis(timestamp2);

        return cal1.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR)
                || cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR);
    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message, txt_seen, tvTime, tvDate;
        public MessageViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
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


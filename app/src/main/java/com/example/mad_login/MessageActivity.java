package com.example.mad_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.Adapter.MessageAdapter;
import com.example.mad_login.Model.Chat;
import com.example.mad_login.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    RecyclerView recyclerView;
    EditText messageInput;
    ImageButton btn_send;

    FirebaseUser currentUser;
    DatabaseReference reference;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    Intent intent;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        messageInput = findViewById(R.id.messageInput);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Registered Users");

        intent = getIntent();
        String receiverId = intent.getStringExtra("userid");
        String receiverName = intent.getStringExtra("username");
        String receiverImageUrl = intent.getStringExtra("imageUrl");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageInput.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(currentUser.getUid(), receiverId, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                messageInput.setText("");
            }
        });


        reference.child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(receiverName);
                Picasso.get().load(receiverImageUrl).into(profile_image);

                // Read messages after setting up the user details
                readMessages(receiverId, currentUser.getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MessageActivity", "Error reading user details", error.toException());
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // Create a unique key for each message
        String messageId = reference.child("Chats").push().getKey();

        // Create a Chat object with the message details
        Chat chat = new Chat(sender, receiver, message);

        // Save the message to the "Chats" node using the unique key
        reference.child("Chats").child(messageId).setValue(chat)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MessageActivity", "Message sent successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MessageActivity", "Error sending message", e);
                    }
                });
    }


    private void readMessages(String currentUserId, String receiverId) {
        mchat = new ArrayList<>();
        messageAdapter = new MessageAdapter(MessageActivity.this, mchat);
        recyclerView.setAdapter(messageAdapter);

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(currentUserId) && chat.getSender().equals(receiverId) ||
                            chat.getReceiver().equals(receiverId) && chat.getSender().equals(currentUserId)) {
                        mchat.add(chat);
                    }
                }

                // Notify the adapter of the data change after the loop
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MessageActivity", "Error reading messages", error.toException());
            }
        });
    }
}
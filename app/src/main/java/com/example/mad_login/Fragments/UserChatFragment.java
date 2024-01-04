package com.example.mad_login.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.Adapter.UserAdapter;
import com.example.mad_login.Model.User;
import com.example.mad_login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserChatFragment extends Fragment {

    private RecyclerView UserRecyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private EditText search_users;
    private ImageView cancelSearch;


    public UserChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_chat, container, false);

        UserRecyclerView = view.findViewById(R.id.userRecyclerView);
        UserRecyclerView.setHasFixedSize(true);
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);
        UserRecyclerView.setAdapter(userAdapter);

        readUsers("");

        search_users = view.findViewById(R.id.search_users);
        cancelSearch = view.findViewById(R.id.cancel_search);

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the search field and hide the cancel icon
                search_users.setText("");
                cancelSearch.setVisibility(View.GONE);

                // Switch back to normal readUsers
                readUsers("");
            }
        });

        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Show/hide cancel icon based on the search text
                cancelSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);

                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }


    private void searchUsers(String searchText) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");

        mUsers.clear(); // Clear the list before adding users

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    String name = userSnapshot.child("name").getValue(String.class);
                    String imageURL = userSnapshot.child("imageUrl").getValue(String.class);

                    if (userId != null && !userId.equals(firebaseUser.getUid()) && name != null && !name.isEmpty()) {
                        // Convert both search text and user name to lowercase for case-insensitive comparison
                        String searchLowerCase = searchText.toLowerCase();
                        String nameLowerCase = name.toLowerCase();

                        // Check if the user name contains the search text
                        if (nameLowerCase.contains(searchLowerCase)) {
                            // Check if there are messages between the current user and the displayed user
                            chatRef.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                    if (chatSnapshot.exists()) {
                                        boolean hasMessages = false;
                                        for (DataSnapshot chatDataSnapshot : chatSnapshot.getChildren()) {
                                            String sender = chatDataSnapshot.child("sender").getValue(String.class);
                                            String receiver = chatDataSnapshot.child("receiver").getValue(String.class);

                                            if ((sender != null && sender.equals(firebaseUser.getUid()) && receiver != null && receiver.equals(userId)) ||
                                                    (sender != null && sender.equals(userId) && receiver != null && receiver.equals(firebaseUser.getUid()))) {
                                                // User has messages with the current user
                                                hasMessages = true;
                                                break; // Break out of the loop since we found a matching user
                                            }
                                        }

                                        if (hasMessages) {
                                            User user = new User();
                                            user.setUid(userId);
                                            user.setName(name);
                                            user.setImageUrl(imageURL != null && !imageURL.isEmpty() ? imageURL : getDefaultUserPhotoUrl());
                                            mUsers.add(user);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("UserChatFragment", "Error reading user messages", databaseError.toException());
                                }
                            });
                        }
                    }
                }

                // Notify the adapter of the data change
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserChatFragment", "Error reading users", databaseError.toException());
            }
        });
    }


    private void readUsers(String searchText) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");

        String currentUserId = firebaseUser != null ? firebaseUser.getUid() : "";

        mUsers.clear(); // Clear the list before adding users

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    if (!userId.equals(currentUserId)) {
                        // Check if there are messages between the current user and the displayed user
                        chatRef.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                if (chatSnapshot.exists()) {
                                    for (DataSnapshot chatDataSnapshot : chatSnapshot.getChildren()) {
                                        String sender = chatDataSnapshot.child("sender").getValue(String.class);
                                        String receiver = chatDataSnapshot.child("receiver").getValue(String.class);

                                        if ((sender != null && sender.equals(currentUserId) && receiver != null && receiver.equals(userId)) ||
                                                (sender != null && sender.equals(userId) && receiver != null && receiver.equals(currentUserId))) {
                                            // User has messages with the current user
                                            String name = userSnapshot.child("name").getValue(String.class);
                                            String imageUrl = userSnapshot.child("imageUrl").getValue(String.class);

                                            System.out.println("UserChatFragment: User ID: " + userId);
                                            System.out.println("UserChatFragment: Name: " + name);
                                            System.out.println("UserChatFragment: ImageUrl: " + imageUrl);

                                            if (name != null && !name.isEmpty()) {
                                                User user = new User();
                                                user.setUid(userId);
                                                user.setName(name);
                                                user.setImageUrl(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : getDefaultUserPhotoUrl());
                                                mUsers.add(user);

                                                // Notify the adapter of the data change
                                                userAdapter.notifyDataSetChanged();
                                                break; // Break out of the loop since we found a matching user
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("UserChatFragment", "Error reading user messages", databaseError.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserChatFragment", "Error reading users", databaseError.toException());
            }
        });
    }


    // Method to get the default user photo URL
    private String getDefaultUserPhotoUrl() {
        // Replace this with the URL of your default user photo
        return "https://example.com/default_user_photo.jpg";
    }
}
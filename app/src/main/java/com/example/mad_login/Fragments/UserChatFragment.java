package com.example.mad_login.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public UserChatFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_allchats, container, false);

        UserRecyclerView = view.findViewById(R.id.userRecyclerView);
        UserRecyclerView.setHasFixedSize(true);
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);
        UserRecyclerView.setAdapter(userAdapter);

        readUsers();

        return view;
    }

    private void readUsers() {

        authProfile =FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        // Assuming you have stored the user data under a node called "Registered Users"
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");
        String currentUserId = firebaseUser != null ? firebaseUser.getUid() : "";

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the user ID for the current snapshot
                    String userId = snapshot.getKey();

                    // Skip the current user
                    if (userId.equals(currentUserId)) {
                        continue;
                    }

                    // Assuming "name" is a direct child of the "Registered Users" node
                    String name = snapshot.child("name").getValue(String.class);
                    String imageURL = snapshot.child("imageURL").getValue(String.class);

                    if (name != null && !name.isEmpty()) {
                        User user = new User();
                        user.setUid(userId);
                        user.setName(name);

                        // If the user has a profile image, use it; otherwise, use the default user photo URL
                        user.setImageURL(imageURL != null && !imageURL.isEmpty() ? imageURL : getDefaultUserPhotoUrl());

                        // Add the user to the list
                        mUsers.add(user);
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

    // Method to get the default user photo URL
    private String getDefaultUserPhotoUrl() {
        // Replace this with the URL of your default user photo
        return "https://example.com/default_user_photo.jpg";
    }
}
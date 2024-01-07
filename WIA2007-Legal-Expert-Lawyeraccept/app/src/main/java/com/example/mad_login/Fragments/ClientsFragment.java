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

public class ClientsFragment extends Fragment {

    private RecyclerView UserRecyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private EditText search_users;
    private ImageView cancelSearch;

    public ClientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        UserRecyclerView = view.findViewById(R.id.userRecyclerView);
        UserRecyclerView.setHasFixedSize(true);
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);
        UserRecyclerView.setAdapter(userAdapter);

        // Assuming that firebaseUser is initialized before calling readUsers
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");

        mUsers.clear(); // Clear the list before adding users

        lawyerRef.child(firebaseUser.getUid()).child("ReceivedRequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    String userId = requestSnapshot.getKey();

                    userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            String name = userSnapshot.child("name").getValue(String.class);
                            String imageURL = userSnapshot.child("imageURL").getValue(String.class);

                            if (name != null && !name.isEmpty()) {
                                User user = new User();
                                user.setUid(userId);
                                user.setName(name);
                                user.setImageUrl(imageURL != null && !imageURL.isEmpty() ? imageURL : getDefaultUserPhotoUrl());
                                mUsers.add(user);

                                // Notify the adapter of the data change
                                userAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("ClientsFragment", "Error reading user data", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ClientsFragment", "Error reading received requests", databaseError.toException());
            }
        });
    }

    private void readUsers(String searchText) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");

        String currentUserId = firebaseUser != null ? firebaseUser.getUid() : "";

        mUsers.clear(); // Clear the list before adding users

        lawyerRef.child(currentUserId).child("ReceivedRequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    String userId = requestSnapshot.getKey();

                    userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            String name = userSnapshot.child("name").getValue(String.class);
                            String imageURL = userSnapshot.child("imageURL").getValue(String.class);

                            if (name != null && !name.isEmpty()) {
                                User user = new User();
                                user.setUid(userId);
                                user.setName(name);
                                user.setImageUrl(imageURL != null && !imageURL.isEmpty() ? imageURL : getDefaultUserPhotoUrl());
                                mUsers.add(user);

                                // Notify the adapter of the data change
                                userAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("ClientsFragment", "Error reading user data", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ClientsFragment", "Error reading received requests", databaseError.toException());
            }
        });
    }

    // Method to get the default user photo URL
    private String getDefaultUserPhotoUrl() {
        // Replace this with the URL of your default user photo
        return "https://example.com/default_user_photo.jpg";
    }
}
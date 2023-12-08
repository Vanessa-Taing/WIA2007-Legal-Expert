package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class shareUserProfile extends AppCompatActivity {

    private TextView TVName,TVEmail,TVDoB,TVGender,TVMobile,TVCase;
    private ProgressBar progressBar;
    private String name,email,doB,gender,mobile;
    private ImageView IVProfile;
    private FirebaseUser firebaseUser;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_user_profile);

        TVName = findViewById(R.id.TVName);
        TVEmail = findViewById(R.id.TVEmail);
        TVDoB = findViewById(R.id.TVDoB);
        TVGender = findViewById(R.id.TVGender);
        TVMobile = findViewById(R.id.TVMobile);
        TVCase = findViewById(R.id.TVCase);
        progressBar = findViewById(R.id.progressBar);
        IVProfile =findViewById(R.id.IVProfile);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser ==null){
            Toast.makeText(shareUserProfile.this,"Something went wrong! User's details are not available at the moment",
                    Toast.LENGTH_LONG).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User Reference from the Database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails!=null){
                    name = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;

                    TVName.setText(name);
                    TVEmail.setText(email);
                    TVDoB.setText(doB);
                    TVGender.setText(gender);
                    TVMobile.setText(mobile);

                    //Set User DP(After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //Set User's current DP in ImageView(if uploaded ady). We will Picasso since imageViewer setImage
                    //Regular URIs
                    Picasso.get().load(uri).into(IVProfile);
                }else{
                    Toast.makeText(shareUserProfile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(shareUserProfile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
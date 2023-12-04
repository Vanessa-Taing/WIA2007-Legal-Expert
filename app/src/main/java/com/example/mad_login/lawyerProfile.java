package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class lawyerProfile extends AppCompatActivity {
    private Button btnYourRating, btnSetting, btnSchedule,btnCaseHistory,btnLogOut;
    private TextView TVProfileName,TVSpecialise;
    private ImageView IVProfilePic;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile);
        btnLogOut =findViewById(R.id.btnLogOut);
        btnYourRating =findViewById(R.id.btnYourRating);
        btnSetting =findViewById(R.id.btnSetting);
        btnSchedule =findViewById(R.id.btnSchedule);
        btnCaseHistory =findViewById(R.id.btnCaseHistory);

        TVProfileName= findViewById(R.id.TVLawyerName);
        TVSpecialise=findViewById(R.id.TVSpecialise);

        //set onclick when lawyer click profile pic
        IVProfilePic = findViewById(R.id.imageView_profile_dp);

        authProfile =FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        // Assuming you have stored the user data under a node called "Registered Lawyers"
        String userId = authProfile.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadWriteUserDetails readUserDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails!=null) {
                    // 'name' and 'specialization' fields in "Registered Lawyers"
                    String name = readUserDetails.name;
                    String specialization = readUserDetails.specialization;

                    // Update the UI with the retrieved data
                    TVProfileName.setText(name);
                    TVSpecialise.setText("Specialise in "+specialization);

                    //set user DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewer setImageURI() should not be ued with regular URIs. So we are using Picasso
                    Picasso.get().load(uri).into(IVProfilePic);
                } else{
                Toast.makeText(lawyerProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(lawyerProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        //logOut
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(lawyerProfile.this,  MainActivity.class));
            }
        });

        //setting
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lawyerProfile.this, lawyerSetting.class));
            }
        });
//
//        //go to case history
//        btnCaseHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(lawyerProfile.this, caseHistory.class));
//            }
//        });
//
//        //go to Schedule
//        btnSchedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(lawyerProfile.this, schedule.class));
//            }
//        });
//
//        //go to support
//        btnYourRating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(lawyerProfile.this, support.class));
//            }
//        });

    }
}
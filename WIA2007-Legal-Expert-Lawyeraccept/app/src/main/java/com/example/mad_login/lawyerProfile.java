package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem; //sf

import com.example.mad_login.lawyerAppointment.lawyerAppointment;
import com.example.mad_login.lawyerAppointment.lawyerSchedule;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        btnLogOut = findViewById(R.id.btnLogOut);
        btnSetting = findViewById(R.id.btnSetting);
        btnSchedule = findViewById(R.id.btnSchedule);
        RatingBar RBRating = findViewById(R.id.RBRating);


        TVProfileName = findViewById(R.id.TVLawyerName);
        TVSpecialise = findViewById(R.id.TVSpecialise);

        //set onclick when lawyer click profile pic
        IVProfilePic = findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        // Assuming you have stored the user data under a node called "Registered Lawyers"
        String userId = authProfile.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(userId);


        DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(userId).child("Rating");

        ValueEventListener ratingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalRating = 0;
                int ratingCount = 0;

                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
                    LawyerRating lawyerRating = caseSnapshot.getValue(LawyerRating.class);
                    if (lawyerRating != null) {
                        totalRating += lawyerRating.getRating();
                        ratingCount++;
                    }
                }

                if (ratingCount > 0) {
                    float averageRating = (float) totalRating / ratingCount;
                    // Set the averageRating value to the RatingBar
                    RBRating.setRating(averageRating);
                } else {
                    // No ratings available
                    RBRating.setRating(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the retrieval process
            }
        };

// Attach the listener to the ratingsRef
        ratingsRef.addValueEventListener(ratingsListener);


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadWriteUserDetails readUserDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    // 'name' and 'specialization' fields in "Registered Lawyers"
                    String name = readUserDetails.name;
                    String specialization = readUserDetails.specialization;

                    // Update the UI with the retrieved data
                    TVProfileName.setText(name);
                    TVSpecialise.setText("Specialise in " + specialization);

                    //set user DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewer setImageURI() should not be ued with regular URIs. So we are using Picasso
                    Picasso.get().load(uri).into(IVProfilePic);
                } else {
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
                startActivity(new Intent(lawyerProfile.this, MainActivity.class));
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
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lawyerProfile.this, lawyerSchedule.class));
            }
        });
//
//        //go to support
//        btnYourRating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(lawyerProfile.this, support.class));
//            }
//        });

             BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.lawyermenu_request) {
                        // Navigate to lawyerRequestStatus activity
                        startActivity(new Intent(lawyerProfile.this, lawyerRequestStatus.class));
                        return true;
                    }
                    else if (item.getItemId()==R.id.lawyermenu_profile) {
                        startActivity(new Intent(lawyerProfile.this, lawyerProfile.class));
                        return true ;
                    }
                    else if (item.getItemId()==R.id.lawyermenu_chat) {
                        startActivity(new Intent(lawyerProfile.this, lawyer_ChatActivity.class));
                        return true ;
                    }
                    else if (item.getItemId()==R.id.lawyermenu_appointment) {
                        startActivity(new Intent(lawyerProfile.this, lawyerAppointment.class));
                        return true ;
                    }
                    else if (item.getItemId()==R.id.lawyermenu_home){
                        startActivity(new Intent(lawyerProfile.this, lawyerProfile.class));
                        return true ;
                    }
                    else {
                        // Handle other menu items here if needed
                        return false;
                    }
                }
        });


    }
}
package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class userProfile extends AppCompatActivity {
    private TextView TVProfileName,TVProfileState;
    private ImageView ProfilePictureView;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private Button btnFeedback, btnSetting, btnSchedule,btnMyCase,btnLogOut;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        btnLogOut =findViewById(R.id.btnLogOut);
        btnFeedback =findViewById(R.id.btnFeedback);
        btnSetting =findViewById(R.id.btnSetting);
        btnSchedule =findViewById(R.id.btnSchedule);
        btnMyCase =findViewById(R.id.btnMyCase);
        TVProfileName =findViewById(R.id.TVProfileName);
        TVProfileState = findViewById(R.id.TVState);
        ProfilePictureView = findViewById(R.id.imageView_profile_dp);

        // Set default profile picture
        ProfilePictureView.setImageResource(R.drawable.mad_cat);


        authProfile =FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        // Assuming you have stored the user data under a node called "Registered Users"
        String userId = authProfile.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadWriteUserDetails readUserDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails!=null) {
                    // Assuming you have 'name' and 'state' fields in your user data
                    String name = readUserDetails.name;
                    String userState = readUserDetails.state;

                    // Update the UI with the retrieved data
                    TVProfileName.setText(name);
                    TVProfileState.setText("Based in "+userState);

                    //set user DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewer setImageURI() should not be ued with regular URIs. So we are using Picasso
                    Picasso.get().load(uri).into(ProfilePictureView);
                }else{
                    Toast.makeText(userProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(userProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        //logOut
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                finish();
                startActivity(new Intent(userProfile.this,MainActivity.class));
                Toast.makeText(userProfile.this,"Logged Out",Toast.LENGTH_LONG).show();
            }
        });

        //setting
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userProfile.this, userSetting.class));
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    startActivity(new Intent(getApplicationContext(), CaseActivity.class));
                    return true;
                } else if (itemId == R.id.menu_cases) {
                    startActivity(new Intent(getApplicationContext(), MainActivity4.class));
                    return true;
                }  else if (itemId == R.id.menu_lawyer) {
                    startActivity(new Intent(getApplicationContext(), LawyerListActivity.class));
                    return true;
                } else if (itemId == R.id.menu_profile) {
                    startActivity(new Intent(getApplicationContext(), userProfile.class));
                    return true;
                }else if (itemId == R.id.menu_chat) {
                    startActivity(new Intent(getApplicationContext(), User_ChatActivity.class));
                    return true;
                }
                return false;
            }
        });

//        //go to my case
//        btnMyCase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(generalUserProfile.this, myCase.class));
//            }
//        });
//
//        //go to Schedule
//        btnSchedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(generalUserProfile.this, schedule.class));
//            }
//        });
//
        //go to support
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userProfile.this, FeedbackOnApp.class));
            }
        });
        }


//    Creating ActionBar Menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //Inflate menu items
//        getMenuInflater().inflate(R.menu.common_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    //When any menu item(refresh menu) is selected
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if(id==R.id.menu_refresh){
//            startActivity(getIntent());
//            finish();
//            overridePendingTransition(0,0); //no animation when click refresh
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
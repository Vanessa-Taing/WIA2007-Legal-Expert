package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_login.lawyerAppointment.lawyerAppointment;
import com.example.mad_login.userSchedule.userSchedule;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class userProfile extends AppCompatActivity {
    private TextView TVProfileName,TVProfileState,TVShareProfile,TVCaseDescription;
    private ImageView ProfilePictureView;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private Button btnSupport, btnSetting, btnSchedule,btnMyCase,btnLogOut;

    //temporary for testing terminate cas function only
    private Dialog terminateDialog;
    private RadioGroup rgReason;
    private Button btnConfirm;
    //temporary for testing terminate cas function only
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        btnLogOut =findViewById(R.id.btnLogOut);
        btnSupport =findViewById(R.id.btnSupport);
        btnSetting =findViewById(R.id.btnSetting);
        btnSchedule =findViewById(R.id.btnSchedule);
        btnMyCase =findViewById(R.id.btnMyCase);
        TVProfileName =findViewById(R.id.TVProfileName);
        TVProfileState = findViewById(R.id.TVState);
        ProfilePictureView = findViewById(R.id.imageView_profile_dp);
        TVShareProfile=findViewById(R.id.tvShareProfile);
        TVCaseDescription = findViewById(R.id.TVCaseDescription);

        TVShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userProfile.this,shareUserProfile.class));
            }
        });
        TVCaseDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userProfile.this,userCaseDescription.class));
            }
        });

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

        //temporary for testing terminate cases only
        DatabaseReference reasonsRef = FirebaseDatabase.getInstance().getReference("termination_reasons");
        // Initialize dialog and UI elements for termination
        terminateDialog = new Dialog(this);
        terminateDialog.setContentView(R.layout.activity_terminate_session);

        rgReason = terminateDialog.findViewById(R.id.rgReason);
        btnConfirm = terminateDialog.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleConfirmButtonClick();
            }
        });
        //go to my case
        btnMyCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminateDialog.show(); //temporary for testing terminate function only
//                startActivity(new Intent(userProfile.this, terminateSession.class));
            }
        });

//
        //go to Schedule
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userProfile.this, userSchedule.class));
            }
        });
//
//        //go to support
//        btnSupport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(generalUserProfile.this, support.class));
//            }
//        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_profile) {
                    // Navigate to lawyerRequestStatus activity
                    startActivity(new Intent(userProfile.this, userProfile.class));
                    return true;
                }
//                else if (item.getItemId()==R.id.lawyermenu_profile) {
//                    startActivity(new Intent(userProfile.this, lawyerSetting.class));
//                    return true ;
//                }
//                else if (item.getItemId()==R.id.lawyermenu_appointment) {
//                    startActivity(new Intent(userProfile.this, lawyerAppointment.class));
//                    return true ;
//                }
                else {
                    // Handle other menu items here if needed
                    return false;
                }
            }
        });
        }

        //temporary for testing terminate function only
    private void handleConfirmButtonClick() {
        // Get selected radio button(s)
        int selectedId = rgReason.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton radioButton = terminateDialog.findViewById(selectedId);

            // Get the selected reason
            String selectedReason = radioButton.getText().toString();

            // Save the data to Firebase
            saveTerminationReason(selectedReason);

            // Dismiss the dialog
            terminateDialog.dismiss();

        } else {
            // Handle the case where no radio button is selected
            Toast.makeText(this, "Please select a reason", Toast.LENGTH_SHORT).show();
        }
    }

    //temporary for testing terminate function only
    private void saveTerminationReason(String reason) {
        // Save the data to Firebase
        // Replace "user_lawyer_relationships" with your actual Firebase node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_lawyer_relationships");

        // Replace "user_id" and "lawyer_id" with the actual user and lawyer IDs
        // This is a simplified example, and you should replace it with your actual data structure
        String userId = authProfile.getCurrentUser().getUid();
        String lawyerId = "lawyer_id";

        // Save the termination reason
        databaseReference.child(userId).child(lawyerId).child("termination_reason").setValue(reason);
        Toast.makeText(this, "Your terminate reason has been saved", Toast.LENGTH_SHORT).show();
    }




}

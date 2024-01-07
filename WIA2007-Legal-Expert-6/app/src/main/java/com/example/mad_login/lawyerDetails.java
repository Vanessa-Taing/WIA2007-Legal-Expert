package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_login.Model.LawyerInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class lawyerDetails extends AppCompatActivity {
    private TextView TVname, TVlawfirm , TVexpYear, TVspecialization, TVlanguage, TVstate, TVmobile, TVemail;
    private ImageView IVimage;
    private Button btnAppointment,btnContactLawyer;
    private FirebaseUser firebaseUser;
    private RatingBar RBRating;
    private Button btnSentRequest;

    //*private Button btnAccept , btnReject ;
    private String currentState = "not_friend";
    private DatabaseReference LawyerRequestRef, UserRef;
    private FirebaseAuth mAuth;
    private String senderUserID, receiverUserID,caseID;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);

        TVname = findViewById(R.id.lawyerName);
        TVlawfirm = findViewById(R.id.lawFirm);
        TVexpYear = findViewById(R.id.EXPYear);
        TVspecialization = findViewById(R.id.specialization);
        TVlanguage = findViewById(R.id.language);
        TVstate = findViewById(R.id.state);
        TVmobile = findViewById(R.id.mobile);
        TVemail = findViewById(R.id.email);
        IVimage = findViewById(R.id.lawyerImage);
        btnContactLawyer =findViewById(R.id.btnContactLawyer);
        btnSentRequest = findViewById(R.id.btnSentRequest);
        RBRating = findViewById(R.id.rating);


        auth =FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        senderUserID= firebaseUser.getUid();
        caseID = getIntent().getStringExtra("caseID");
        receiverUserID = getIntent().getExtras().get("lawyer_id").toString();
        ;
        //  stored the lawyer data under a node called "Registered Lawyers"
        String lawyerId = auth.getCurrentUser().getUid();
        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(lawyerId);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        LawyerRequestRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");


        DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(receiverUserID).child("Rating");

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



        String uid = receiverUserID;
        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        Intent intent = getIntent();
        LawyerInfo model = (LawyerInfo) intent.getSerializableExtra("lawyerInfo");

        btnContactLawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model != null) {
                    Intent chatIntent = new Intent(lawyerDetails.this, MessageActivity.class);
                    chatIntent.putExtra("userid", uid);
                    chatIntent.putExtra("username", name);
                    chatIntent.putExtra("imageUrl", imageUrl);

                    Log.d("LawyerDetailsActivity", "Intent extras: " + chatIntent.getExtras());

                    startActivity(chatIntent);
                }else {
                    Log.e("LawyerDetailsActivity", "LawyerInfo model is null");
                }
            }
        });

        lawyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadWriteUserDetails readUserDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails!=null) {
                    String name = readUserDetails.name;
                    String lawfirm = readUserDetails.lawFirm;
                    String dob = readUserDetails.doB;
                    String expyear =readUserDetails.expYear;
                    String spec = readUserDetails.specialization;
                    String gender = readUserDetails.gender;
                    String language = readUserDetails.language;
                    String state =readUserDetails.state;
                    String mobile = readUserDetails.mobile;
                    String email = readUserDetails.email;
                    //set rating

                    // Update the UI with the retrieved data
                    TVname.setText(name);
                    TVemail.setText("Email: " + email);
                    TVstate.setText("State: " + state);
                    TVmobile.setText("Mobile: " + mobile);
                    TVspecialization.setText("Specialization: " + spec);
                    TVlanguage.setText("Language: " + language);
                    TVlawfirm.setText("Law Firm: "+lawfirm);
                    TVexpYear.setText("Experience year: " + expyear);
                    //set rating

                    //code to set profile picture
                    // Set default profile picture
                    IVimage.setImageResource(R.drawable.ic_baseline_account_box_24);
                    // Check if the user has a profile picture
                    if (firebaseUser.getPhotoUrl() != null) {
                        // Load the uploaded profile picture using Picasso
                        Uri uri = firebaseUser.getPhotoUrl();
                        Picasso.get().load(uri).into(IVimage);
                    }
                }else{
                    String name = getIntent().getStringExtra("name");
                    String lawfirm = getIntent().getStringExtra("lawfirm");
                    String dob = getIntent().getStringExtra("DOB");
                    String expyear = getIntent().getStringExtra("expYear");
                    String spec = getIntent().getStringExtra("specialization");
                    String gender = getIntent().getStringExtra("gender");
                    String language = getIntent().getStringExtra("language");
                    String state = getIntent().getStringExtra("state");
                    String mobile = getIntent().getStringExtra("mobile");
                    String email = getIntent().getStringExtra("email");

                    TVname.setText(name);
                    TVemail.setText("Email: " + email);
                    TVstate.setText("State: " + state);
                    TVmobile.setText("Mobile: " + mobile);
                    TVspecialization.setText("Specialization: " + spec);
                    TVlanguage.setText("Language: " + language);
                    TVlawfirm.setText(lawfirm);
                    TVexpYear.setText("Experience year: " + expyear);
                }
                btnSentRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(lawyerDetails.this);
                        builder.setTitle("Confirmation");

                        if (currentState.equals("not_friend")) {
                            builder.setMessage("Are you certain you want to send a request to this lawyer to handle your case?");
                        } else if (currentState.equals("request_sent")) {
                            builder.setMessage("Are you sure you want to cancel the request sent to this lawyer?");
                        }

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (currentState.equals("not_friend") && caseID != null) {
                                    // Check if the user has already sent a request for this case
                                    UserRef.child(senderUserID).child("Cases").child(caseID) //*
                                            .child("SentRequest").child(receiverUserID)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        String previousStatus = dataSnapshot.child("status").getValue(String.class);
                                                        if (previousStatus.equals("rejected")) {
                                                            // Previous request was rejected, allow sending a new request
                                                            SendRequestToaLawyer(caseID, receiverUserID);
                                                        }
                                                        else{
                                                            Toast.makeText(lawyerDetails.this, "You have already sent a request for this case.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        SendRequestToaLawyer(caseID, receiverUserID);
                                                    }

                                                }


                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Handle errors
                                                }
                                            });
                                } else if (currentState.equals("request_sent") && caseID != null) {
                                    // User wants to cancel the request
                                    CancelRequest(caseID, receiverUserID);
                                } else {
                                    Toast.makeText(lawyerDetails.this, "No case selected", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User canceled, do nothing
                            }
                        });

                        // Show the dialog
                        AlertDialog dialog = builder.show();


                        // Customize text color for buttons (Yes and No)
                        int buttonTextColor = getResources().getColor(R.color.mainPurple); // Replace with your desired color resource
                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        if (positiveButton != null && negativeButton != null) {
                            positiveButton.setTextColor(buttonTextColor);
                            positiveButton.setTypeface(positiveButton.getTypeface(), Typeface.BOLD);

                            negativeButton.setTextColor(getResources().getColor(R.color.buttonGray));
                            negativeButton.setTypeface(negativeButton.getTypeface(), Typeface.BOLD);
                        }
                    }
                });

                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setTitle("Lawyer Details");

            }

            private void SendRequestToaLawyer(final String caseID, final String receiverUserID) {
                Log.d("SendRequestToaLawyer", "CaseID: " + caseID + ", ReceiverUserID: " + receiverUserID);
                UserRef.child(senderUserID).child("Cases").child(caseID)
                        .child("SentRequest").child(receiverUserID)
                        .child("status").setValue("sent")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    LawyerRequestRef.child(receiverUserID).child("ReceivedRequests")
                                            .child(senderUserID).child(caseID).child("status").setValue("pending")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> requesTask) {
                                                    if (requesTask.isSuccessful()) {
                                                        currentState = "request_sent";
                                                        btnSentRequest.setText("Cancel Lawyer Request");
                                                        Toast.makeText(lawyerDetails.this, "Request sent successfully!", Toast.LENGTH_SHORT).show();

                                                    }else {
                                                        Log.e("SendRequestToaLawyer", "Failed to update received requests", requesTask.getException());
                                                    }
                                                }
                                            });
                                }else {
                                    Log.e("SendRequestToaLawyer", "Failed to update sent requests", task.getException());
                                }
                            }
                        });
            }

            private void CancelRequest(final String caseID, final String receiverUserID) {
                UserRef.child(senderUserID).child("Cases").child(caseID)
                        .child("SentRequest").child(receiverUserID)
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    LawyerRequestRef.child(receiverUserID).child("ReceivedRequests")
                                            .child(senderUserID).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> requestTask) {
                                                    if (requestTask.isSuccessful()) {
                                                        currentState = "not_friend";
                                                        btnSentRequest.setText("Send Lawyer Request");
                                                        Toast.makeText(lawyerDetails.this, "Request canceled successfully!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(lawyerDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });




    }
}
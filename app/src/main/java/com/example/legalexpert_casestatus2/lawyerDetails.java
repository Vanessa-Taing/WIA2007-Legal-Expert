package com.example.legalexpert_casestatus2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class lawyerDetails extends AppCompatActivity {
    private TextView TVname, TVlawfirm, TVDOB, TVexpYear, TVspecialization, TVgender, TVlanguage, TVstate, TVmobile, TVemail;
    private Button btnSentRequest;

    //*private Button btnAccept , btnReject ;
    private String currentState = "not_friend";
    private DatabaseReference LawyerRequestRef, UserRef;
    private FirebaseAuth mAuth;
    private String senderUserID, receiverUserID,caseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);

      //  mAuth = FirebaseAuth.getInstance();
       // senderUserID = mAuth.getCurrentUser().getUid();
        senderUserID= "qkXa0CDhGHd3bE1qomtstJaHfB72";


        caseID = getIntent().getStringExtra("caseID");
        receiverUserID = getIntent().getExtras().get("lawyer_id").toString();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        LawyerRequestRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");

        TVname = findViewById(R.id.lawyerName);
        TVlawfirm = findViewById(R.id.lawFirm);
        TVDOB = findViewById(R.id.DOB);
        TVexpYear = findViewById(R.id.EXPYear);
        TVspecialization = findViewById(R.id.specialization);
        TVgender = findViewById(R.id.gender);
        TVlanguage = findViewById(R.id.language);
        TVstate = findViewById(R.id.state);
        TVmobile = findViewById(R.id.mobile);
        TVemail = findViewById(R.id.email);
        btnSentRequest = findViewById(R.id.btnRequest);


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
        TVDOB.setText("Date of birth: " + dob);
        TVemail.setText("Email: " + email);
        TVgender.setText("Gender: " + gender);
        TVstate.setText("State: " + state);
        TVmobile.setText("Mobile: " + mobile);
        TVspecialization.setText("Specialization: " + spec);
        TVlanguage.setText("Language: " + language);
        TVlawfirm.setText(lawfirm);
        TVexpYear.setText("Experience year: " + expyear);

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
                        if (currentState.equals("not_friend")) {
                            // Check if the user has already sent a request for this case
                            UserRef.child(senderUserID).child("Cases").child(caseID)
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
                        } else if (currentState.equals("request_sent")) {
                            // User wants to cancel the request
                            CancelRequest(caseID, receiverUserID);
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

public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
        case android.R.id.home:
            Intent parentIntent = NavUtils.getParentActivityIntent(this);
            parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(parentIntent);
            finish();
            return true;
    }
    return super.onOptionsItemSelected(item);
}


}

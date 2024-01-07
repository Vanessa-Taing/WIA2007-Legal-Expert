package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_login.userAppointment.userAppointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptedLawyerDetails extends AppCompatActivity {

    private TextView TVname, TVlawfirm, TVDOB, TVexpYear, TVspecialization, TVgender, TVlanguage, TVstate, TVmobile, TVemail;
    private Button btnAppointment, btnCancelRequest;


    private DatabaseReference UserRef, LawyerRequestRef;
    private FirebaseAuth mAuth;
    private String senderUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_lawyer_details);

        // mAuth = FirebaseAuth.getInstance();
        // senderUserID = mAuth.getCurrentUser().getUid();
        senderUserID = "qkXa0CDhGHd3bE1qomtstJaHfB72";
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
        btnAppointment = findViewById(R.id.btnAppointment);
        btnCancelRequest = findViewById(R.id.btnCancelRequest);


        String dob = getIntent().getStringExtra("doB");
        String email = getIntent().getStringExtra("email");
        String expyear = getIntent().getStringExtra("expYear");
        String gender = getIntent().getStringExtra("gender");
        String language = getIntent().getStringExtra("language");
        String lawfirm = getIntent().getStringExtra("lawFirm");
        String mobile = getIntent().getStringExtra("mobile");
        String spec = getIntent().getStringExtra("specialization");
        String name = getIntent().getStringExtra("name");
        String state = getIntent().getStringExtra("state");
        String status = getIntent().getStringExtra("status");

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

        if ("sent".equals(status)) {
            btnCancelRequest.setVisibility(View.VISIBLE);
            btnAppointment.setVisibility(View.GONE);
        } else if ("accepted".equals(status)||"Initiate".equals(status)||"OnGoing".equals(status)||"OnHold".equals(status)||"Complete".equals(status)) {
            btnCancelRequest.setVisibility(View.GONE);
            btnAppointment.setVisibility(View.VISIBLE);
        }

        //handle cancelRequestButton
        btnCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverId = getIntent().getStringExtra("receiverId");
                String caseId = getIntent().getStringExtra("caseId");
                CancelRequest(caseId, receiverId);
            }
        });

        //AppointmentButton
        btnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceptedLawyerDetails.this, userAppointment.class);

                String lawyerId = getIntent().getStringExtra("receiverId");
                String caseId = getIntent().getStringExtra("caseId");
                String caseName = getIntent().getStringExtra("caseName");


                intent.putExtra("name", name);
                intent.putExtra("lawyerId",lawyerId );
                intent.putExtra("caseId",caseId );
                intent.putExtra("senderUserId",senderUserID);
                intent.putExtra("caseName",caseName);

                startActivity(intent);
            }
        });

        //actionBar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Selected Lawyer Details");


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

    private void CancelRequest(final String caseID, final String receiverUserID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AcceptedLawyerDetails.this);
        builder.setTitle("Confirmation");

        builder.setMessage("Are you sure you want to cancel the request sent to this lawyer?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
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
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(AcceptedLawyerDetails.this, "Request canceled", Toast.LENGTH_SHORT).show();
                                                        finish(); // Close the activity after canceling the request
                                                    } else {
                                                        Toast.makeText(AcceptedLawyerDetails.this, "Error canceling request", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(AcceptedLawyerDetails.this, "Error canceling request", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
}
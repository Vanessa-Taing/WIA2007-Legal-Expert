package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class LawyerFeedback extends AppCompatActivity {

    private float LawyerRating;
    private String lawyerName, userID, caseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_feedback);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        userID = currentUser.getUid();
        caseID = getIntent().getStringExtra("caseID");


        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextView TVBack = findViewById(R.id.TVSkipUpload);
        TextView TVReport = findViewById(R.id.TVReport);
        TextView TVCaseName = findViewById(R.id.TVCaseName);
        TextView TVCaseType = findViewById(R.id.TVCaseType3);
        TextView TVLawyerAssign = findViewById(R.id.TVLawyerAssign);
        EditText ETLawyerRating = findViewById(R.id.ETLawyerRating);
        EditText ETOverallExp = findViewById(R.id.ETOverallExp);
        RatingBar LawyerRatingBar = findViewById(R.id.LawyerRatingBar);

        String caseName = getIntent().getStringExtra("caseName");
        String caseType = getIntent().getStringExtra("caseType");
        String lawyerID = getIntent().getStringExtra("caseLawyerID");
        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(lawyerID);

        TVCaseName.setText("Case Name : " + caseName);
        TVCaseType.setText("Case Type : " + caseType);
        TVLawyerAssign.setText(" ");


        LawyerRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {LawyerRating = rating;}

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LawyerFeedback.this, "Lawyer Rating: " + LawyerRating, Toast.LENGTH_SHORT).show();
                uploadRating(ETOverallExp.getText().toString(), ETLawyerRating.getText().toString(), LawyerRating, lawyerID);

            }
        });

        TVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TVReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LawyerFeedback.this, LawyerReport.class);
                intent.putExtra("lawyerID", lawyerID);
                intent.putExtra("caseID", caseID);
                startActivity(intent);
            }
        });




    }

    private void uploadRating(String OverallEXP, String LawyerEXP, float Rating, String LawyerID) {
        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Lawyers").child(LawyerID);


        // Create a Case object with the entered data, including the document information
        LawyerRating LRating = new LawyerRating(OverallEXP, LawyerEXP, Rating, userID);

        databaseReference.child("Rating").child(caseID).setValue(LRating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(LawyerFeedback.this, "Thank you for your rating", Toast.LENGTH_SHORT).show();
                            // switch to login page when success

                            Intent intent = new Intent (LawyerFeedback.this, MainActivity4.class);
                            startActivity(intent);
                        } else {
                            // Handle the error
                            Toast.makeText(LawyerFeedback.this, "Failed to rate case", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
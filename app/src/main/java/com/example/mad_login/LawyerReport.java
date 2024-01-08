package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LawyerReport extends AppCompatActivity {

    private String caseID, userID, LawyerID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_report);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        userID = currentUser.getUid();
        caseID = getIntent().getStringExtra("caseID");
        LawyerID = getIntent().getStringExtra("LawyerID");


        TextView tvBack = findViewById(R.id.TVSkipUpload);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        EditText ETReport = findViewById(R.id.ETReport);


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadReport(ETReport.getText().toString(), LawyerID, userID);
            }
        });
    }

    private void uploadReport(String ReportString, String LawyerID, String userID) {
        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report").child(LawyerID);


        // Create a Case object with the entered data, including the document information
        Report Report = new Report(ReportString, LawyerID, userID);

        databaseReference.child(caseID).setValue(Report)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(LawyerReport.this, "Thank you for your report", Toast.LENGTH_SHORT).show();
                            // switch to login page when success

                            Intent intent = new Intent (LawyerReport.this, MainActivity4.class);
                            startActivity(intent);
                        } else {
                            // Handle the error
                            Toast.makeText(LawyerReport.this, "Failed to report", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
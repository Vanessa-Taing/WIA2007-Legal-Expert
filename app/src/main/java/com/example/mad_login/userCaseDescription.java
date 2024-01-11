package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_login.Model.Case;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userCaseDescription extends AppCompatActivity {

    private static final int UPLOAD_DOC_REQUEST_CODE = 1;
    private String[] caseType = {"civil","consumer" , "contract", "criminal" , "family" ,"islamic"};
    private AutoCompleteTextView ACTVCaseType;
    private ArrayAdapter<String> adapterCaseType;
    private EditText ETCaseName, ETCaseDescription;
    private Button btnSubmit,btnUplaodDoc;
    private ProgressBar progressBar;
    private TextView TVSkipUpload;
    private static final String TAG = "userCaseDescription";

    private String userId,txtCaseName,txtCaseDescription,txtCaseType,documentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_case_description);

        // Initialize UI components
        ETCaseName = findViewById(R.id.ETCaseName);
        ETCaseDescription = findViewById(R.id.ETCaseDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        ACTVCaseType = findViewById(R.id.ACTVCaseType);
        TVSkipUpload = findViewById(R.id.TVSkipUpload);
        btnUplaodDoc = findViewById(R.id.btnUplaodDoc);

        btnUplaodDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(userCaseDescription.this, uploadDoc.class), UPLOAD_DOC_REQUEST_CODE);
            }
        });

        //set when user click skip upload, switch to login page
        TVSkipUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the profile page
                startActivity(new Intent(userCaseDescription.this, login1_generalUser.class));
                finish(); // Optional: Finish the current activity if you don't want to return to it
            }
        });


        // Set up the AutoCompleteTextView for caseType
        adapterCaseType = new ArrayAdapter<>(this, R.layout.dropdown_item, caseType);
        ACTVCaseType.setAdapter(adapterCaseType);

        // Set up item click listener for caseType
        ACTVCaseType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(userCaseDescription.this, "Selected item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up click listener for the Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain the entered data
                txtCaseName = ETCaseName.getText().toString();
                txtCaseDescription = ETCaseDescription.getText().toString();
                txtCaseType = ACTVCaseType.getText().toString();

                // Check if documentUrl is not null
                if (TextUtils.isEmpty(txtCaseName) || TextUtils.isEmpty(txtCaseDescription) || TextUtils.isEmpty(txtCaseType)) {
                    Toast.makeText(userCaseDescription.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    // Get the current user
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        uploadCase(userId, txtCaseName, txtCaseDescription, txtCaseType, documentUrl);
                    } else {
                        // Handle the case where the user is not authenticated
                        Toast.makeText(userCaseDescription.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    // Inside your userCaseDescription activity

// ...

    private void uploadCase(String userId, String txtCaseName, String txtCaseDescription, String txtCaseType, String documentUrl) {
        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("Cases");

        // Create a Case object with the entered data, including the document information
        Case userCase = new Case(txtCaseName, txtCaseDescription, txtCaseType, documentUrl);

        // Use the reference to the database to push a new child node with a unique ID for the case
        String caseId = databaseReference.push().getKey();
        databaseReference.child(caseId).setValue(userCase)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(userCaseDescription.this, "Your case is submitted", Toast.LENGTH_SHORT).show();
                            // switch to login page when success
                            Intent intent= new Intent(userCaseDescription.this, login1_generalUser.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Handle the error
                            Toast.makeText(userCaseDescription.this, "Failed to submit your case", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error: " + task.getException());
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPLOAD_DOC_REQUEST_CODE && resultCode == RESULT_OK) {
            // Obtain the document URL from the data intent
            documentUrl = data.getStringExtra("documentUrl");
            // Call the uploadCase method with the obtained document URL
            uploadCase(userId, txtCaseName, txtCaseDescription, txtCaseType, documentUrl);
        }
    }
}

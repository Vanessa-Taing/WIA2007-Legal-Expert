package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminAddCase extends AppCompatActivity {

    private static final int UPLOAD_DOC_REQUEST_CODE = 1;
    private String[] caseType = {"Civil", "Consumer", "Contract", "Criminal", "Family", "Islamic"};
    private AutoCompleteTextView ACTVCaseType;
    private ArrayAdapter<String> adapterCaseType;
    private EditText ETCaseName, ETCaseDescription, ETCaseLink, ETCaseSummary;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private TextView TVSkipUpload;
    private static final String TAG = "userCaseDescription";

    private String userId,txtCaseName,txtCaseSummary, txtCaseDescription,txtCaseType,txtCaseLink, item, caseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_case);



        // Initialize UI components
        item = "";
        ETCaseName = findViewById(R.id.ETReport);
        ETCaseSummary = findViewById(R.id.ETLawyerRating);
        ETCaseDescription = findViewById(R.id.ETCaseSummary);
        ETCaseLink = findViewById(R.id.ETCaseLink);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        ACTVCaseType = findViewById(R.id.ACTVCaseType);
        TVSkipUpload = findViewById(R.id.TVSkipUpload);

        //set when user click skip upload, switch to login page
        TVSkipUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the profile page
                Intent intent = new Intent (adminAddCase.this, adminCases.class);
                startActivity(intent);
                // Optional: Finish the current activity if you don't want to return to it
            }
        });


        // Set up the AutoCompleteTextView for caseType
        adapterCaseType = new ArrayAdapter<>(this, R.layout.dropdown_item, caseType);
        ACTVCaseType.setAdapter(adapterCaseType);

        // Set up item click listener for caseType
        ACTVCaseType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                item = adapterView.getItemAtPosition(position).toString();

                Toast.makeText(adminAddCase.this, "Selected item: " + item, Toast.LENGTH_SHORT).show();

                // Create a Firebase Database instance
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference casesRef = database.getReference("Cases Code");

                // Retrieve the current amount of cases of the given case type
                casesRef.orderByChild("casetype").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Check if there are any cases
                        if (dataSnapshot.exists()) {
                            String lastKey = "";
                            // Get the key of the last case
                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                 lastKey = childSnapshot.getKey();
                            }
                            // Extract the case number from the last key
                            int lastCaseNumber = Integer.parseInt(lastKey.substring(lastKey.length() - 3));
                            // Generate the case code based on the case type and the last case number
                            switch (item) {
                                case "Civil":
                                    caseCode = "A" + String.format("%03d", lastCaseNumber + 1);
                                    break;
                                case "Consumer":
                                    caseCode = "B" + String.format("%03d", lastCaseNumber + 1);
                                    break;
                                case "Contract":
                                    caseCode = "C" + String.format("%03d", lastCaseNumber + 1);
                                    break;
                                case "Criminal":
                                    caseCode = "D" + String.format("%03d", lastCaseNumber + 1);
                                    break;
                                case "Family":
                                    caseCode = "E" + String.format("%03d", lastCaseNumber + 1);
                                    break;
                                case "Islamic":
                                    caseCode = "F" + String.format("%03d", lastCaseNumber + 1);
                                    break;
                            }
                        } else {
                            // If there are no cases, start from A001
                            switch (item) {
                                case "Civil":
                                    caseCode = "A001";
                                    break;
                                case "Consumer":
                                    caseCode = "B001";
                                    break;
                                case "Contract":
                                    caseCode = "C001";
                                    break;
                                case "Criminal":
                                    caseCode = "D001";
                                    break;
                                case "Family":
                                    caseCode = "E001";
                                    break;
                                case "Islamic":
                                    caseCode = "F001";
                                    break;
                            }
                        }
                        Toast.makeText(adminAddCase.this, "Case Code: " + caseCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
            });



        // Set up click listener for the Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(adminAddCase.this);
                builder.setCancelable(true);
                builder.setTitle("Double Confirmation");
                builder.setMessage("Are you absolutely sure you want to submit this case?");
                builder.setPositiveButton("Yes, Submit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadCase(txtCaseName, txtCaseSummary, txtCaseDescription, txtCaseType, txtCaseLink, caseCode);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();

                // Obtain the entered data
                txtCaseName = ETCaseName.getText().toString();
                txtCaseSummary = ETCaseSummary.getText().toString();
                txtCaseDescription = ETCaseDescription.getText().toString();
                txtCaseLink = ETCaseLink.getText().toString();
                txtCaseType = ACTVCaseType.getText().toString();

                if (TextUtils.isEmpty(txtCaseName) || TextUtils.isEmpty(txtCaseDescription) || TextUtils.isEmpty(txtCaseType) || TextUtils.isEmpty(txtCaseLink) || TextUtils.isEmpty(txtCaseSummary)) {
                    Toast.makeText(adminAddCase.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    dialog.show();
                }
            }
        });
    }

    // Inside your userCaseDescription activity

// ...

    private void uploadCase(String txtCaseName,String txtCaseSummary, String txtCaseDescription, String txtCaseType, String txtCaseLink, String caseCode) {
        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cases Code");

        // Create a Case object with the entered data, including the document information
        AppCase NewCase = new AppCase(txtCaseName, txtCaseSummary, txtCaseDescription, txtCaseType, txtCaseLink);

        databaseReference.child(caseCode).setValue(NewCase)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(adminAddCase.this, "Your case is submitted", Toast.LENGTH_SHORT).show();
                            // switch to login page when success

                            Intent intent = new Intent (adminAddCase.this, adminCases.class);
                            startActivity(intent);
                        } else {
                            // Handle the error
                            Toast.makeText(adminAddCase.this, "Failed to submit your case", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error: " + task.getException());
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPLOAD_DOC_REQUEST_CODE && resultCode == RESULT_OK) {

        }
    }

}
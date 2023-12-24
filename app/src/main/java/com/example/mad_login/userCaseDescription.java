package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class userCaseDescription extends AppCompatActivity {

    private static final int UPLOAD_DOC_REQUEST_CODE = 1;
    private String[] caseType = {"Criminal", "Sexual Assault", "Domestic Violence", "Divorce", "Contract", "Bankruptcy"};
    private AutoCompleteTextView ACTVCaseType;
    private ArrayAdapter<String> adapterCaseType;
    private EditText ETCaseName, ETCaseDescription, etFileName;
    private Button btnSubmit, btnUploadDoc, btnSelect;
    private ProgressBar progressBar;
    private TextView TVSkipUpload;
    private static final String TAG = "userCaseDescription";
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Uri selectedFileUri,url ; // Store the selected file URI
    private String txtCaseName, txtCaseDescription, txtCaseType, documentUrl, userId;
    private FirebaseUser currentUser;

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
        btnUploadDoc = findViewById(R.id.btnUploadDoc);
        etFileName = findViewById(R.id.etFileName);
        btnSelect = findViewById(R.id.btnSelect);

        // Set up Firebase
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("Cases");
        }

        //set when user clicks skip upload, switch to login page
        TVSkipUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the profile page
                startActivity(new Intent(userCaseDescription.this, login1_generalUser.class));
                finish(); // Optional: Finish the current activity if you don't want to return to it
            }
        });

        // Select files button click listener
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser!= null) {
                    selectFiles();
                } else {
                    // User is not authenticated, show a message or redirect to login
                    Toast.makeText(userCaseDescription.this, "Please log in to upload files", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Upload button click listener
        btnUploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser!= null) {
                    // Start the upload process only if a file is selected
                    if (selectedFileUri != null) {
                        uploadFiles(selectedFileUri);
                    } else {
                        // No file selected, you can handle this case as needed
                        Toast.makeText(userCaseDescription.this, "No file selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User is not authenticated, show a message
                    Toast.makeText(userCaseDescription.this, "Please log in to upload files", Toast.LENGTH_SHORT).show();
                }
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

                // Check if caseName,caseType,caseDescription is not null
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

    private void uploadCase(String userId, String txtCaseName, String txtCaseDescription, String txtCaseType, String documentUrl) {
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
                            Intent intent = new Intent(userCaseDescription.this, login1_generalUser.class);
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

        if (requestCode == UPLOAD_DOC_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Store the selected file URI
            selectedFileUri = data.getData();
            // Display the selected file name or other details
            etFileName.setText(getFileName(selectedFileUri));
        }
    }

    private void selectFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Files..."), UPLOAD_DOC_REQUEST_CODE);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = "selected_file.pdf"; // Default name if unable to retrieve
        }
        return result;
    }

    private void uploadFiles(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        // Define the storage reference for the file
        StorageReference reference = storageReference.child("Upload Docs/" + currentUser.getUid() + "/" + System.currentTimeMillis() + ".pdf");

            // Upload the file to Firebase Storage
            reference.putFile(Objects.requireNonNull(data))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded file
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            url = uriTask.getResult();
                            documentUrl = url.toString();
                            Toast.makeText(userCaseDescription.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                                }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            // Update the progress of the upload
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded:" + (int) progress + "%");
                        }
                    });
        }
    }

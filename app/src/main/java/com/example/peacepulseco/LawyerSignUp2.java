package com.example.peacepulseco;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LawyerSignUp2 extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private Button btnUpload;
    private Button btnSubmit;

    private StorageReference storageReference;
    private Uri fileUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_sign_up2);

        FirebaseApp.initializeApp(this);
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://peacepulsecotest.appspot.com/");

        btnUpload = findViewById(R.id.btnUpload);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri != null) {
                    uploadFileToFirebase();
                } else {
                    Toast.makeText(LawyerSignUp2.this, "Please select a file to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST_CODE);
    }

    private void uploadFileToFirebase() {
        // Create a reference to the file in Firebase storage
        StorageReference fileRef = storageReference.child("files/" + fileUri.getLastPathSegment());

        // Upload file to Firebase storage
        UploadTask uploadTask = fileRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(LawyerSignUp2.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
            // File uploaded successfully, you can perform further actions here if needed
        }).addOnFailureListener(e -> {
            Toast.makeText(LawyerSignUp2.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            // Handle file upload failure here
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.getData();

            }
        }
    }
}
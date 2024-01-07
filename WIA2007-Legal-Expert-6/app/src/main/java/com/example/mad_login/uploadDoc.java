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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class uploadDoc extends AppCompatActivity {

    private Button btnUploadDoc, btnSelect;
    private EditText etFileName;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Uri selectedFileUri; // Store the selected file URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_doc);

        btnUploadDoc = findViewById(R.id.btnUploadDoc);
        etFileName = findViewById(R.id.etFileName);
        btnSelect = findViewById(R.id.btnSelect);

        // Database
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Upload Docs");

        // Select files button click listener
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null) {
                    selectFiles();
                } else {
                    // User is not authenticated, show a message or redirect to login
                    Toast.makeText(uploadDoc.this, "Please log in to upload files", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Upload button click listener
        btnUploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null && selectedFileUri != null) {
                    // Start the upload process
                    uploadFiles(selectedFileUri);
                } else {
                    // User is not authenticated or no file selected, show a message
                    Toast.makeText(uploadDoc.this, "Please select a file and log in to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Files..."), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Store the selected file URI
            selectedFileUri = data.getData();
            // Display the selected file name or other details
            etFileName.setText(getFileName(selectedFileUri));
        }
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

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            // Define the storage reference for the file
            StorageReference reference = storageReference.child("Upload Docs/" + firebaseUser.getUid() + "/" + System.currentTimeMillis() + ".pdf");
            // Upload the file to Firebase Storage
            reference.putFile(Objects.requireNonNull(data))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded file
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) ;
                            Uri url = uriTask.getResult();

                            // Create a fileClass object with file details
                            fileClass file = new fileClass(etFileName.getText().toString(), url.toString());

                            // Save the file details to the Firebase Realtime Database
                            databaseReference.child(firebaseUser.getUid()).child(databaseReference.push().getKey()).setValue(file);

                            // Finish the activity
                            finish();
                            Toast.makeText(uploadDoc.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            // Pass the download URL back to the calling activity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("documentUrl", url.toString());
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            // Update the progress of the upload
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded:" + (int) progress + "%");
                        }
                    });
        }
    }
}

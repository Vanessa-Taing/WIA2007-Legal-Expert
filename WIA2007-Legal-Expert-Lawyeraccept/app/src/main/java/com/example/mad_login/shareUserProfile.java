package com.example.mad_login;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.squareup.picasso.Picasso;

import com.itextpdf.layout.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class shareUserProfile extends AppCompatActivity {

    private TextView TVName, TVEmail, TVDoB, TVGender, TVMobile, TVCase;
    private ProgressBar progressBar;
    private String name, email, doB, gender, mobile, profilePictureUrl;
    private ImageView IVProfile;
    private FirebaseUser firebaseUser;
    private FirebaseAuth authProfile;
    private AutoCompleteTextView ACTVCase;
    private ArrayAdapter<String> adapterCase;
    private Button btnShareProfile;
    private Uri profilePictureUri; // Assume you have the URI for the profile picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_user_profile);

        TVName = findViewById(R.id.TVName);
        TVEmail = findViewById(R.id.TVEmail);
        TVDoB = findViewById(R.id.TVDoB);
        TVGender = findViewById(R.id.TVGender);
        TVMobile = findViewById(R.id.TVMobile);
        TVCase = findViewById(R.id.TVCase);
        progressBar = findViewById(R.id.progressBar);
        IVProfile = findViewById(R.id.IVProfile);
        btnShareProfile = findViewById(R.id.btnShareProfile);
        btnShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserProfile();
            }
        });

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        //dropdown for case
        ACTVCase = findViewById(R.id.ACTVCase);

        //dropdown for case
        adapterCase = new ArrayAdapter<>(this, R.layout.dropdown_item);
        ACTVCase.setAdapter(adapterCase);

        // Call fetchCaseNames before setting the listener
        fetchCaseNames();
        ACTVCase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedCaseName = adapterView.getItemAtPosition(position).toString().trim();
                retrieveAndDisplayCaseDetails(firebaseUser, selectedCaseName);
            }
        });


        if (firebaseUser == null) {
            Toast.makeText(shareUserProfile.this, "Something went wrong! User's details are not available at the moment",
                    Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
            retrieveCaseInformation(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Extracting User Reference from the Database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    name = readUserDetails.name;
                    email = firebaseUser.getEmail();
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;

                    TVName.setText(name);
                    TVEmail.setText(email);
                    TVDoB.setText(doB);
                    TVGender.setText(gender);
                    TVMobile.setText(mobile);

                    // Retrieve profile picture URL from Firebase Storage
                    retrieveProfilePicture(userID);

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(shareUserProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void retrieveProfilePicture(String userID) {
        // Reference to the profile picture in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("DisplayPics/" + userID + ".jpg");

        // Download the profile picture and load it into ImageView
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            profilePictureUrl = uri.toString();
            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                Picasso.get().load(profilePictureUrl).into(IVProfile);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors that may occur while downloading the profile picture
            Toast.makeText(shareUserProfile.this, "Failed to retrieve profile picture", Toast.LENGTH_LONG).show();
        });
    }


    private void retrieveCaseInformation(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Extracting Case Reference from the Database for "Registered Users"
        DatabaseReference referenceCase = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(userID)
                .child("Cases");

        referenceCase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterate through each case
                for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                    Case caseDetails = caseSnapshot.getValue(Case.class);
                    if (caseDetails != null) {
                        // Assuming you want to display caseName, caseType, and caseDescription
                        String caseInfo = "Case Name: " + caseDetails.caseName +
                                "\nCase Type: " + caseDetails.caseType +
                                "\nCase Description: " + caseDetails.caseDescription +
                                "\nUploaded Document URL: " + caseDetails.documentUrl;

                        // Append case information to the existing text
                        TVCase.append(caseInfo);
                        TVCase.append("\n\n");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(shareUserProfile.this, "Something went wrong while retrieving case information!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void retrieveAndDisplayCaseDetails(FirebaseUser firebaseUser, String selectedCaseName) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceCase = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(userID)
                .child("Cases");

        referenceCase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean caseFound = false;

                // Iterate through each case
                for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                    Case caseDetails = caseSnapshot.getValue(Case.class);
                    if (caseDetails != null && selectedCaseName.equals(caseDetails.caseName)) {
                        // Display details for the selected case
                        String caseInfo = "Case Name: " + caseDetails.caseName +
                                "\nCase Type: " + caseDetails.caseType +
                                "\nCase Description: " + caseDetails.caseDescription +
                                "\nUploaded Document URL: " + caseDetails.documentUrl;

                        // Set case information to the TextView
                        TVCase.setText(caseInfo);

                        // Set the flag to true since the case is found
                        caseFound = true;
                        break;
                    }
                }

                // If the case is not found, display a message
                if (!caseFound) {
                    Toast.makeText(shareUserProfile.this, "Case details not found!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(shareUserProfile.this, "Something went wrong while retrieving case information!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchCaseNames() {
        DatabaseReference caseReference = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(firebaseUser.getUid())
                .child("Cases");

        caseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> caseNames = new ArrayList<>();
                for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                    Case caseDetails = caseSnapshot.getValue(Case.class);
                    if (caseDetails != null) {
                        caseNames.add(caseDetails.caseName);
                    }
                }

                // Update the adapter with the fetched case names
                adapterCase.addAll(caseNames);
                adapterCase.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(shareUserProfile.this, "Failed to fetch case names!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void shareUserProfile() {
        // Retrieve the profile picture directly into the PDF
        retrieveProfilePictureForSharing();
    }

    private void retrieveProfilePictureForSharing() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("DisplayPics/" + firebaseUser.getUid() + ".jpg");

        try {
            File localFile = File.createTempFile("profile_image", "jpg");

            imageRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image downloaded successfully
                        // Now you can use the localFile path and proceed to create and share the profile PDF
                        File pdfFile = createPdfFile(localFile);
                        shareProfile(pdfFile);
                    })
                    .addOnFailureListener(exception -> {
                        // Handle download failure
                        Toast.makeText(this, "Failed to download profile picture", Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create temporary file for profile picture", Toast.LENGTH_SHORT).show();
        }
    }

    private File createPdfFile(File profileImageFile) {
        File pdfFile = null;
        try {
            // Create a directory for PDF files if it doesn't exist
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "UserProfilePDFs");

            // Create a unique PDF file
            String pdfFileName = String.format(Locale.getDefault(), "UserProfile_%s.pdf", System.currentTimeMillis());
            pdfFile = new File(pdfDir, pdfFileName);

            // Initialize PDF writer and document
            PdfWriter pdfWriter = new PdfWriter(pdfFile);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            // Add profile picture to the PDF
            if (profileImageFile != null) {
                try {
                    ImageData imageData = ImageDataFactory.create(profileImageFile.getAbsolutePath());
                    Image image = new Image(imageData);
                    // Set the width of the image as needed
                    image.setWidth(100); // Adjust the width as per your requirement
                    document.add(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Add user profile data to the PDF
            document.add(new Paragraph("Name: " + name));
            document.add(new Paragraph("Email: " + email));
            document.add(new Paragraph("Date of Birth: " + doB));
            document.add(new Paragraph("Gender: " + gender));
            document.add(new Paragraph("Mobile: " + mobile));

            // Add case information to the PDF
            document.add(new Paragraph("\nCase Information"));
            document.add(new Paragraph(TVCase.getText().toString()));

            // Close the document
            document.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return pdfFile;
    }

    private void shareProfile(File pdfFile) {
        // Get the URI for the PDF file using FileProvider
        Uri pdfUri = FileProvider.getUriForFile(this, "com.example.mad_login.fileprovider", pdfFile);

        // Create an intent for sharing
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);

        // Grant read permission
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Start the sharing activity
        startActivity(Intent.createChooser(shareIntent, "Share Profile"));
    }
}
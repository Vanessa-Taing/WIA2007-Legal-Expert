package com.example.mad_login;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class shareUserProfile extends AppCompatActivity {

    private TextView TVName, TVEmail, TVDoB, TVGender, TVMobile, TVCase;
    private ProgressBar progressBar;
    private String name, email, doB, gender, mobile, profilePictureUrl,receiverId;
    private ImageView IVProfile;
    private FirebaseUser firebaseUser;
    private FirebaseAuth authProfile;
    private AutoCompleteTextView ACTVCase;
    private ArrayAdapter<String> adapterCase;
    private Button btnShareProfile;
    private Intent intent;
    private DatabaseReference reference;

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

        // Initialize UI components and retrieve receiver details
        intent = getIntent();
        receiverId = intent.getStringExtra("receiverUid");
        btnShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserProfile();
            }
        });

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Registered Users");

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
            // Profile picture doesn't exist, load default profile picture
            IVProfile.setImageResource(R.drawable.ic_baseline_account_box_24);
            Toast.makeText(shareUserProfile.this, "default profile picture is used", Toast.LENGTH_LONG).show();
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

        // Create a temporary file to store the profile picture
        File localFile;
        try {
            localFile = File.createTempFile("profile_image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create a temporary file for the profile picture", Toast.LENGTH_SHORT).show();
            return;
        }

        // Download the profile picture
        imageRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image downloaded successfully
                    // Now you can use the localFile path and proceed to create and share the profile PDF
                    File pdfFile = createPdfFile(localFile);
                    shareProfile(pdfFile);
                })
                .addOnFailureListener(exception -> {
                    // Handle download failure
                    // If the profile picture doesn't exist, use the default profile picture for sharing
                    localFile.delete(); // Delete the temporary file created for a non-existing profile picture
                    File defaultProfilePic = new File(getCacheDir(), "default_profile_picture.jpg");
                    try {
                        if (!defaultProfilePic.exists()) {
                            // Create the default profile picture file if it doesn't exist
                            Picasso.get().load(R.drawable.ic_baseline_account_box_24).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    try {
                                        FileOutputStream fos = new FileOutputStream(defaultProfilePic);
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                        fos.flush();
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    // Proceed to create and share the profile PDF with the default profile picture
                                    File pdfFile = createPdfFile(defaultProfilePic);
                                    shareProfile(pdfFile);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    // Handle failure to load the default profile picture
                                    Toast.makeText(shareUserProfile.this, "Failed to load the default profile picture", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                            });
                        }
                        // Proceed to create and share the profile PDF with the default profile picture
                        File pdfFile = createPdfFile(defaultProfilePic);
                        shareProfile(pdfFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to download the profile picture", Toast.LENGTH_SHORT).show();
                    }
                });
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
        // Upload the PDF file to Firebase Storage or your preferred storage solution
        // Get the URL or identifier for the stored file
        String fileUrl = uploadPdfToStorage(pdfFile);

        if (fileUrl != null) {
            // Create a message or notification to inform the receiver about the shared file
            sendMessageToReceiver(fileUrl);
        } else {
            // Handle the case where file upload fails
            Toast.makeText(this, "Failed to share the profile", Toast.LENGTH_SHORT).show();
        }
    }

    private String uploadPdfToStorage(File pdfFile) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Generate a unique filename for the PDF file
        String fileName = "Profile.pdf";

        // Create a reference to the file in Firebase Storage
        StorageReference pdfRef = storageRef.child("UserProfilePDFs/" + fileName);

        // Upload the PDF file
        pdfRef.putFile(Uri.fromFile(pdfFile))
                .addOnSuccessListener(taskSnapshot -> {
                    // File uploaded successfully
                    // Retrieve the URL of the uploaded file
                    pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String fileUrl = uri.toString();
                        // Now you can use the 'fileUrl' to reference the uploaded PDF file
                        // You may want to store this URL in your database for future retrieval
                        // (e.g., in the sendMessageToReceiver method)
                        sendMessageToReceiver(fileUrl);
                    }).addOnFailureListener(e -> {
                        // Handle failure to get the download URL
                        Toast.makeText(shareUserProfile.this, "Failed to get the download URL", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Handle failure to upload the PDF file
                    Toast.makeText(shareUserProfile.this, "Failed to upload the PDF file", Toast.LENGTH_SHORT).show();
                });

        return fileName; // Return the unique filename (optional, based on your needs)
    }

    private void sendMessageToReceiver(String fileUrl) {
        // Construct a message with the file URL and other details
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("sender", firebaseUser.getUid());
        messageMap.put("receiver", receiverId);
        messageMap.put("messageType", "pdf"); // Indicate the type of the message
        messageMap.put("fileUrl", fileUrl);
        messageMap.put("timestamp", System.currentTimeMillis());
        messageMap.put("isseen", false);

        // Send the message to the "Chats" node
        String messageId = reference.child("Chats").push().getKey();
        reference.child("Chats").child(messageId).setValue(messageMap);
    }

    //private void shareProfile(File pdfFile) {
    //    // Upload the PDF file to Firebase Storage
    //    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("UserProfilePDFs").child(pdfFile.getName());
    //    Uri pdfUri = Uri.fromFile(pdfFile);
    //
    //    storageReference.putFile(pdfUri)
    //            .addOnSuccessListener(taskSnapshot -> {
    //                // Get the download URL of the uploaded file
    //                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
    //                    String pdfDownloadUrl = uri.toString();
    //
    //                    // Share the download URL directly with the specific user
    //                    sendProfilePdfLink(pdfDownloadUrl);
    //
    //                    // Notify the user that sharing is successful
    //                    Toast.makeText(shareUserProfile.this, "Profile shared successfully", Toast.LENGTH_SHORT).show();
    //                }).addOnFailureListener(e -> {
    //                    // Handle failure to get download URL
    //                    Toast.makeText(shareUserProfile.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
    //                });
    //            })
    //            .addOnFailureListener(e -> {
    //                // Handle failure to upload PDF file
    //                Toast.makeText(shareUserProfile.this, "Failed to upload profile PDF", Toast.LENGTH_SHORT).show();
    //            });
    //}
    //
    //private void sendProfilePdfLink(String pdfDownloadUrl) {
    //    // Create a new message containing the PDF download URL
    //    reference = FirebaseDatabase.getInstance().getReference();
    //    HashMap<String, Object> hashMap = new HashMap<>();
    //    hashMap.put("sender", firebaseUser.getUid());
    //    hashMap.put("receiver", receiverId);
    //    hashMap.put("pdfUrl", pdfDownloadUrl);
    //    hashMap.put("timestamp", System.currentTimeMillis());
    //    hashMap.put("isseen", false);
    //
    //    // Send the message to the "Chats" node
    //    String messageId = reference.child("Chats").push().getKey();
    //    reference.child("Chats").child(messageId).setValue(hashMap);
    //}
}
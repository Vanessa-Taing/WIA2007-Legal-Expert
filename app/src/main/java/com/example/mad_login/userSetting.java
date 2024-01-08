package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class userSetting extends AppCompatActivity {
    private TextView TVChangeProfilePic;
    private ImageView IVback, IVProfile;
    private Button btnUpdateEmail, btnUpdateProfile;
    private static final int UPLOAD_PROFILE_PIC_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        IVProfile = findViewById(R.id.IVProfile);
        TVChangeProfilePic = findViewById(R.id.TVChangeProfile);
        btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
        btnUpdateProfile = findViewById(R.id.btnUpdate);
        IVback = findViewById(R.id.IVback);

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userSetting.this, userProfile.class));
                finish();
            }
        });

        // upload profile pic
        TVChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the UploadProfilePic activity with startActivityForResult
                Intent intent = new Intent(userSetting.this, UploadProfilePic.class);
                startActivityForResult(intent, UPLOAD_PROFILE_PIC_REQUEST_CODE);
            }
        });

        // update email
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userSetting.this, updateEmail.class));
                finish();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userSetting.this, updateUserProfile.class));
                finish();
            }
        });

        // Load the latest profile picture
        loadLatestProfilePicture();
    }

    private void loadLatestProfilePicture() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            // Get the user's UID
            String userId = firebaseUser.getUid();

            // Reference to the user's profile picture in Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");
            StorageReference profilePicRef = storageReference.child(userId + "." + getFileExtension(null));

            // Load the profile picture into IVProfile using Picasso
            profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(IVProfile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors that may occur during the download
                }
            });
        }
    }

    // Obtain File Extension of the image
    private String getFileExtension(@Nullable Uri uri) {
        if (uri != null) {
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String extension = mime.getExtensionFromMimeType(cR.getType(uri));
            return extension != null ? extension : "";
        }
        return ""; // Handle the case where the URI is null
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPLOAD_PROFILE_PIC_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the profilePicUri from the result Intent
            if (data != null) {
                String profilePicUri = data.getStringExtra("profilePicUri");

                // Use the profilePicUri as needed (e.g., load it into IVProfile)
                if (profilePicUri != null) {
                    Picasso.get().load(profilePicUri).into(IVProfile);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLatestProfilePicture();
    }

}

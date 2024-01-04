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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

//upload only, not changing
public class UploadProfilePic extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView IVUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST =1;
    private Uri uriImage;
    private Button btnUploadPic, btnChoose;
    private String profilePicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        btnChoose = findViewById(R.id.btnChoose);
        btnUploadPic = findViewById(R.id.btnUploadPic);
        progressBar = findViewById(R.id.progressBar);
        IVUploadPic = findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            // User is not authenticated, redirect to login screen or take appropriate action
            Intent intent = new Intent(UploadProfilePic.this, signUp_SelectRole.class);
            startActivity(intent);
            finish(); // Finish the current activity to prevent the user from coming back to it
        } else {
            // User is authenticated, proceed with the rest of the code

            storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");
            Uri uri = firebaseUser.getPhotoUrl();   //get the image from firebase

            // Set User's current DP in ImageView (if uploaded already). Use Picasso for image loading.
            Picasso.get().load(uri).into(IVUploadPic);

            // Choosing image to upload
            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFileChooser();
                }
            });

        //Upload Image
        btnUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });
        }
    }

    private void UploadPic() {
        if(uriImage!=null){ //user had selected a pic
            //Save the image with uid of the currently logged user
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid()+"."
            +getFileExtension(uriImage));

            //upload image to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;

                            //set the display image of the user after upload
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            if (firebaseUser != null) {
                                firebaseUser.updateProfile(profileUpdates);
                            }
                            // Set the profilePicUri
                            profilePicUri = downloadUri.toString();

                            // Update the "imageUrl" node in the database
                            updateImageUrlInDatabase(profilePicUri);

                            //Pass the URI to the appropriate activity based on user type
                            backToPreviousActivity();
                        }
                    });

                    //after success uploaded
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfilePic.this,"Upload Successful!",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {       //if failed to upload
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfilePic.this,e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(UploadProfilePic.this,"No File Selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateImageUrlInDatabase(String imageUrl) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(firebaseUser.getUid());

            // Update the "imageUrl" node with the new URL
            databaseReference.child("imageUrl").setValue(imageUrl);
        }
    }


    private void backToPreviousActivity() {
        Intent resultIntent = new Intent();

        // Pass the profilePicUri as a result to the calling activity
        resultIntent.putExtra("profilePicUri", profilePicUri);
        setResult(RESULT_OK, resultIntent);

        // Finish the current activity and return to the calling activity
        finish();
    }

    //Obtain File Extension of the image(can get multiple format of image, not just jpg)
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = mime.getExtensionFromMimeType(cR.getType(uri));
        return extension != null ? extension : "";
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");  //any kind of image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){  //have result
            uriImage = data.getData();
            IVUploadPic.setImageURI(uriImage);
        }
    }

    }

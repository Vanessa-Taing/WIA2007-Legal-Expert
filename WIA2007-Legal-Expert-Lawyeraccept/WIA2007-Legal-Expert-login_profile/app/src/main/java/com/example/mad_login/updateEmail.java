package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updateEmail extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView tvAuthenticated;
    private String oldEmail,newEmail,userPwd;
    private Button btnUpdateEmail;
    private EditText etNewEmail,etPwd;
    private DatabaseReference lawyersRef,usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        progressBar =findViewById(R.id.progressBar);
        etNewEmail =findViewById(R.id.editText_update_email_new);
        etPwd =findViewById(R.id.editText_update_email_verify_password);
        tvAuthenticated =findViewById(R.id.textView_update_email_authenticated);
        btnUpdateEmail =findViewById(R.id.button_update_email);

        btnUpdateEmail.setEnabled(false); //btn disable in the beginning until user is authenticated
        etNewEmail.setEnabled(false);

        authProfile =FirebaseAuth.getInstance();
        firebaseUser =authProfile.getCurrentUser();

        //Set old email ID on TextView
        oldEmail = firebaseUser.getEmail();
        TextView tvOldEmail = findViewById(R.id.textView_update_email_old);
        tvOldEmail.setText(oldEmail);

        if(firebaseUser.equals("")){
            Toast.makeText(updateEmail.this, "Something went wrong! User's details not available", Toast.LENGTH_SHORT).show();
        }else{
            reAuthenticate(firebaseUser);
        }
    }

    //ReAuthenticate/Verify User before updating email
    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button btnVerifyUser = findViewById(R.id.button_authenticate_user);
        btnVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtain pwd for authentication
                userPwd= etPwd.getText().toString();
                if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(updateEmail.this,"Password is needed to continue",Toast.LENGTH_SHORT).show();
                    etPwd.setError("Please enter your password for authentication");
                    etPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    AuthCredential credential = EmailAuthProvider.getCredential(oldEmail,userPwd);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(updateEmail.this,"Password has been verified."+"You can update email now.",Toast.LENGTH_LONG).show();
                                //Set TextView to show that user is authenticated
                                tvAuthenticated.setText("You are authenticated. You can update you email now.");

                                //Disable et for pwd, button to verify user and enable et for new email  update email btn
                                etNewEmail.setEnabled(true);
                                etPwd.setEnabled(false);
                                btnVerifyUser.setEnabled(false);
                                btnUpdateEmail.setEnabled(true);

                                //Change color of Update Email btn
                                btnUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(updateEmail.this,
                                        R.color.my_primary));

                                btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newEmail =etNewEmail.getText().toString();
                                        if(TextUtils.isEmpty(newEmail)){
                                            Toast.makeText(updateEmail.this,"New Email is required",Toast.LENGTH_SHORT).show();
                                            etNewEmail.setError("Please enter new Email");
                                            etNewEmail.requestFocus();
                                        }else if(!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                                            Toast.makeText(updateEmail.this,"Please enter valid Email",Toast.LENGTH_SHORT).show();
                                            etNewEmail.setError("Please provide valid Email");
                                            etNewEmail.requestFocus();
                                        }else if(oldEmail.matches(newEmail)){
                                            Toast.makeText(updateEmail.this,"New Email cannot be same as old Email",Toast.LENGTH_SHORT).show();
                                            etNewEmail.setError("Please enter new Email");
                                            etNewEmail.requestFocus();
                                        }else{
                                            progressBar.setVisibility(View.VISIBLE);
                                            updateUserEmail(firebaseUser);
                                        }
                                    }
                                });
                            }else {
                             try{
                                 throw task.getException();
                             }catch (Exception e){
                                 Toast.makeText(updateEmail.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                             }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateUserEmail(FirebaseUser firebaseUser) {
        firebaseUser.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Verify email
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(updateEmail.this, "Email has been updated. Please verify your new Email", Toast.LENGTH_SHORT).show();

                    // Check user type and navigate to the appropriate setting activity
                    String userId = firebaseUser.getUid();
                    usersRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);
                    lawyersRef = FirebaseDatabase.getInstance().getReference("Registered Lawyers").child(userId);

                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // User is in "Registered Users"
                                usersRef.child("email").setValue(newEmail); //update email in database
                                Intent intent = new Intent(updateEmail.this, userSetting.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // User is not in "Registered Users," check "Registered Lawyers"
                                lawyersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // User is in "Registered Lawyers"
                                            lawyersRef.child("email").setValue(newEmail);
                                            Intent intent = new Intent(updateEmail.this, lawyerSetting.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Handle the case where the user is neither in "Registered Users" nor "Registered Lawyers"
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle the error
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error
                        }
                    });
                } else {
                    // Email update failed
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(updateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class login1_generalUser extends AppCompatActivity {
    private EditText ETEmail, ETPassword;
    private TextView forgetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "login1_generalUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1_general_user);

        ETEmail = findViewById(R.id.ETEmail);
        ETPassword = findViewById(R.id.ETPassword);
        progressBar = findViewById(R.id.progressBarLogin);
        authProfile = FirebaseAuth.getInstance();
        forgetPassword = findViewById(R.id.txtForgetPassword);

        //forget password
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(login1_generalUser.this);
                View dialogView = getLayoutInflater().inflate(R.layout.activity_forget_password,null);
                EditText ETEmailForgetPwd = dialogView.findViewById(R.id.ETEmailForgetPwd);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userEmail = ETEmailForgetPwd.getText().toString();

                        if(TextUtils.isEmpty(userEmail)|| !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(login1_generalUser.this, "Enter your registered email ID",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        authProfile.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(login1_generalUser.this,"Check your email",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(login1_generalUser.this,"Unable to send, failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow()!=null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });


        //Show Hide Password using Eye Icon
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ETPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    //If password is visible then Hide it
                    ETPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                } else {
                    ETPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        //Login user (password and account)
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = ETEmail.getText().toString();
                String txtPassword = ETPassword.getText().toString();

                if (TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(login1_generalUser.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    ETEmail.setError("Email is required");
                    ETEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(login1_generalUser.this, "Please use correct email format", Toast.LENGTH_LONG).show();
                    ETEmail.setError("Valid email is required");
                    ETEmail.requestFocus();
                } else if (TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(login1_generalUser.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    ETPassword.setError("password is required");
                    ETPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(txtEmail, txtPassword);
                }
            }
        });

        //user sign up (create new account)
        TextView ClickToSignUpPage = findViewById(R.id.ClickToSignUpPage);
        ClickToSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login1_generalUser.this, signUp_SelectRole.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //login by account and password
    private void loginUser(String txtEmail, String txtPassword) {
        authProfile.signInWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(login1_generalUser.this, "You are logged in now", Toast.LENGTH_SHORT).show();
                    //need to be changed to switch to homepage
                    navigateToUserProfile();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        ETEmail.setError("User does not exists or is no longer valid. Please register again.");
                        ETEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        ETEmail.setError("Invalid credentials. Kindly check and re-enter.");
                        ETEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(login1_generalUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //navigate to User Profile after login successfully
    private void navigateToUserProfile() {
        finish();
        Intent intent = new Intent(login1_generalUser.this, userProfile.class);
        startActivity(intent);
    }

}
package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class lawyerPersonal_Info extends AppCompatActivity {
    private DatePickerDialog datePicker;
    private ProgressBar progressBar;
    private String txtState,txtGender,txtPhoneNum,txtName,txtBirthday,txtLanguage;
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private String[] state = {"Selangor", "Kuala Lumpur", "Labuan", "Johor", "Perlis", "Sabah", "Sarawak",
            "Melaka", "Pulau Pinang", "Pahang", "Perak", "Negeri Sembilan", "Putrajaya", "Kelantan", "Kedah", "Terengganu"};
    AutoCompleteTextView ACTVState;
    ArrayAdapter<String> adapterState;
    private EditText ETName, ETEmail, ETPassword, ETConfirmPassword, ETBirthday, ETPhoneNum,ETLawyerLanguage;
    private static final String TAG = "lawyerPersonal_Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_personal_info);


//-------------when user click log in, page switch to login1_generalUser-------------
        TextView BtnLogin = findViewById(R.id.TVLawyerHaveAcc_Login);
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lawyerPersonal_Info.this, lawyerLoginPage.class));
            }
        });

        Toast.makeText(lawyerPersonal_Info.this, "You can sign up now", Toast.LENGTH_LONG).show();
        progressBar = findViewById(R.id.progressBar);
        ETEmail = findViewById(R.id.ETLawyerEmail);
        ETPassword = findViewById(R.id.ETLawyerPassword);
        ETConfirmPassword = findViewById(R.id.ETLawyerConfirmPwd);
        progressBar = findViewById(R.id.progressBar);
        ETName =findViewById(R.id.ETLawyerName);
        ETBirthday=findViewById(R.id.ETLawyerDoB);
        ETPhoneNum=findViewById(R.id.ETLawyerPhoneNo);
        ETLawyerLanguage = findViewById(R.id.ETLawyerLanguage);

        //Setting up DatePicker on EditText
        ETBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();   //get today's date
                int day =calendar.get(Calendar.DAY_OF_MONTH);
                int month =calendar.get(Calendar.MONTH);
                int year =calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                datePicker = new DatePickerDialog(lawyerPersonal_Info.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ETBirthday.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,year);
                datePicker.show();
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

        //Show Hide Confirm Password using Eye Icon
        ImageView IVShowHideConfirmPwd = findViewById(R.id.IVShowHideConfirmPwd);
        IVShowHideConfirmPwd.setImageResource(R.drawable.ic_hide_pwd);
        IVShowHideConfirmPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ETConfirmPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    //If password is visible then Hide it
                    ETConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    IVShowHideConfirmPwd.setImageResource(R.drawable.ic_hide_pwd);
                } else {
                    ETConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    IVShowHideConfirmPwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        //dropdown for state
        ACTVState = findViewById(R.id.ACTVLawyerState);

        //dropdown for state
        adapterState = new ArrayAdapter<>(this, R.layout.dropdown_item, state);
        ACTVState.setAdapter(adapterState);
        ACTVState.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(lawyerPersonal_Info.this, "item " + item, Toast.LENGTH_SHORT).show();
            }
        });

        Button BtnNext= findViewById(R.id.btnLawyerNext);
        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RadioButton for Gender
                rgGender = findViewById(R.id.rgGender);
                int selectedRadioButtonId = rgGender.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    rbGender = findViewById(selectedRadioButtonId);
                    txtGender = rbGender.getText().toString();
                } else {
                    // Handle the case when no gender is selected
                    Toast.makeText(lawyerPersonal_Info.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    return; // or do something else to handle this case
                }
                //Obtain the entered data
                String txtEmail = ETEmail.getText().toString();
                String txtPassword = ETPassword.getText().toString();
                String txtConfirmPassword = ETConfirmPassword.getText().toString();
                txtName = ETName.getText().toString();
                txtBirthday = ETBirthday.getText().toString();
                txtPhoneNum = ETPhoneNum.getText().toString();
                String txtGender ;  //Can't obtain the value before verifying if any button was selected or not
                txtState = ACTVState.getText().toString();
                txtLanguage = ETLawyerLanguage.getText().toString();

                if (TextUtils.isEmpty(txtEmail)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    ETEmail.setError("email is required");
                    ETEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please use correct email format", Toast.LENGTH_LONG).show();
                    ETEmail.setError("Valid email is required");
                    ETEmail.requestFocus();
                } else if (TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    ETPassword.setError("password is required");
                    ETPassword.requestFocus();
                } else if (TextUtils.isEmpty(txtConfirmPassword)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    ETConfirmPassword.setError("Password confirmation is required");
                    ETConfirmPassword.requestFocus();
                } else if (!txtPassword.equals(txtConfirmPassword)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please use same password", Toast.LENGTH_LONG).show();
                    ETConfirmPassword.setError("Password Confirmation is required");
                    ETConfirmPassword.requestFocus();
                    //Clear the entered Passwords
                    ETPassword.clearComposingText();
                    ETConfirmPassword.clearComposingText();
                } else if (TextUtils.isEmpty(txtName)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    ETName.setError("Full name is required");
                    ETName.requestFocus();
                } else if (TextUtils.isEmpty(txtLanguage)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please enter your language spoken", Toast.LENGTH_LONG).show();
                    ETLawyerLanguage.setError("Language spoken is required");
                    ETLawyerLanguage.requestFocus();
                } else if (TextUtils.isEmpty(txtBirthday)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    ETBirthday.setError("Date of birth is required");
                    ETBirthday.requestFocus();
                } else if (TextUtils.isEmpty(txtPhoneNum)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please enter your phone number", Toast.LENGTH_LONG).show();
                    ETPhoneNum.setError("Phone number is required");
                    ETPhoneNum.requestFocus();
                } else if (txtPhoneNum.length()<10) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please re-enter your phone number", Toast.LENGTH_LONG).show();
                    ETPhoneNum.setError("Phone number should not less than 10 digits");
                    ETPhoneNum.requestFocus();
                } else if (rgGender.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    rbGender.setError("Gender is required");
                    rbGender.requestFocus();
                } else if (TextUtils.isEmpty(txtState)) {
                    Toast.makeText(lawyerPersonal_Info.this, "Please select your state", Toast.LENGTH_LONG).show();
                    ACTVState.setError("State is required");
                    ACTVState.requestFocus();
                } else{
                    txtGender = rbGender.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(txtEmail, txtPassword, txtGender, txtState, txtPhoneNum,txtName,txtBirthday,txtLanguage);
                }
            }
        });
    }

    //Register User using the credentials given
    private void registerUser(String txtEmail, String txtPassword,String txtGender,String txtState,String txtPhoneNum,String txtName,String txtBirthday,String txtLanguage) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create user profile
        auth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(lawyerPersonal_Info.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails();
                            writeUserDetails.setGender(txtGender);
                            writeUserDetails.setState(txtState);
                            writeUserDetails.setMobile(txtPhoneNum);
                            writeUserDetails.setName(txtName);
                            writeUserDetails.setDoB(txtBirthday);
                            writeUserDetails.setLanguage(txtLanguage);

                            // Add professional information
                            writeUserDetails.setBarNumber("");
                            writeUserDetails.setExpYear("");
                            writeUserDetails.setLawFirm("");
                            writeUserDetails.setSpecialization("");
                            writeUserDetails.setQualification("");

                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Lawyers");

//                    //Update Display Name of User
//                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txtUsername).build();
//                    firebaseUser.updateProfile(profileChangeRequest);

//                            //Enter User Data into the Firebase Realtime Database
//                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtGender, txtState, txtPhoneNum,txtName,txtBirthday,txtLanguage);
//
//                            //Extracting user reference from Database for "Registered Users"
//                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        //send Verification Email
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText(lawyerPersonal_Info.this, "Your personal information had been saved", Toast.LENGTH_LONG).show();

                                        //open User Profile after successful registration
                                        Intent intent = new Intent(lawyerPersonal_Info.this, lawyerProfessional_Info.class);
                                        // Pass user data to the next activity
                                        intent.putExtra("name", txtName);
                                        intent.putExtra("email", txtEmail);
                                        intent.putExtra("gender", txtGender);
                                        intent.putExtra("language", txtLanguage);
                                        intent.putExtra("doB", txtBirthday);
                                        intent.putExtra("state", txtState);
                                        intent.putExtra("phoneNum", txtPhoneNum);
                                        //To Prevent User from returning back to Register Activity on pressing back button after registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); //to close Register Activity
                                    } else {
                                        Toast.makeText(lawyerPersonal_Info.this, "Fail to save your personal information. Please try again", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                ETPassword.setError("Your password is too weak. Kindly use a mix of alphabets, numbers and special characters");
                                ETPassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                ETEmail.setError("your email is invalid or already in use. Kindly re-enter.");
                                ETEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                ETEmail.setError("User is already registered with this email. Use another email.");
                                ETEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(lawyerPersonal_Info.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}

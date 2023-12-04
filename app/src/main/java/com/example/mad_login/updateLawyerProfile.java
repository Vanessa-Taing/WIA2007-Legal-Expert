package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updateLawyerProfile extends AppCompatActivity {
    private EditText ETName,ETPhoneNo,ETBirthday,ETLanguage,ETExpYear,ETBarNumber;
    private ImageView IVback;
    private Button btnUpdate;
    private FirebaseAuth authProfile;
    private String name,mobile,txtState,txtGender,doB,txtLanguage,expYear,barNumber,specialization,lawFirm,qualification,email;
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private AutoCompleteTextView ACTVState,ACTVLawFirm,ACTVSpecialization,ACTVQualification;
    private ArrayAdapter<String> adapterState,adapterLawFirm,adapterSpecialization,adapterQualification;
    private static final String[] LAWFIRM = {"LING & THENG BOOK","other"};
    private static final String[] SPECIALIZATION = {"Family","other"};
    private static final String[] QUALIFICATION ={"CERTIFICATE IN LEGAL PRACTICE(CLP)","other"};
    private static final String[] STATES = {"Selangor", "Kuala Lumpur", "Labuan", "Johor", "Perlis", "Sabah", "Sarawak",
            "Melaka", "Pulau Penang", "Pahang", "Perak", "Negeri Sembilan", "Putrajaya", "Kelantan", "Kedah", "Terengganu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lawyer_profile);

//        //init
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //show Profile Data
        showUserProfile(firebaseUser);


        ETName =findViewById(R.id.ETName);
        ETBirthday = findViewById(R.id.ETBirthday);
        ETPhoneNo =findViewById(R.id.ETPhoneNum);
        btnUpdate =findViewById(R.id.btnUpdate);
        IVback = findViewById(R.id.IVback);
        ETLanguage =findViewById(R.id.ETLanguage);
        ETExpYear=findViewById(R.id.ETExpYear);
        ETBarNumber=findViewById(R.id.ETBarNumber);

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(updateLawyerProfile.this, lawyerSetting.class));
                finish();
            }
        });

        //RadioButton for Gender
        rgGender =findViewById(R.id.rgGender);

        //dropdown for state
        ACTVState = findViewById(R.id.ACTVState);
        ACTVLawFirm = findViewById(R.id.ACTVLawFirm);
        ACTVQualification =findViewById(R.id.ACTVQualification);
        ACTVSpecialization = findViewById(R.id.ACTVSpecialization);

        //dropdown for state
        adapterState = new ArrayAdapter<>(updateLawyerProfile.this, R.layout.dropdown_item, STATES);
        ACTVState.setAdapter(adapterState);
        ACTVState.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(updateLawyerProfile.this, "Selected State " + item, Toast.LENGTH_SHORT).show();
            }
        });

        //dropdown for lawFirm
        adapterLawFirm = new ArrayAdapter<>(updateLawyerProfile.this, R.layout.dropdown_item, LAWFIRM);
        ACTVLawFirm.setAdapter(adapterLawFirm);
        ACTVLawFirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(updateLawyerProfile.this, "Selected Law Firm " + item, Toast.LENGTH_SHORT).show();
            }
        });

        //dropdown for specialization
        adapterSpecialization = new ArrayAdapter<>(updateLawyerProfile.this, R.layout.dropdown_item, SPECIALIZATION);
        ACTVSpecialization.setAdapter(adapterSpecialization);
        ACTVSpecialization.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(updateLawyerProfile.this, "Selected Specialization " + item, Toast.LENGTH_SHORT).show();
            }
        });

        //dropdown for qualification
        adapterQualification = new ArrayAdapter<>(updateLawyerProfile.this, R.layout.dropdown_item, QUALIFICATION);
        ACTVQualification.setAdapter(adapterQualification);
        ACTVQualification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(updateLawyerProfile.this, "Selected Qualification " + item, Toast.LENGTH_SHORT).show();
            }
        });

        //Setting up DatePicker on EditText
        ETBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Extracting saved dd,m,yyyy into different variables by creating an array delimited by "/"
                String textSADoB[] = doB.split("/");

                int day =Integer.parseInt(textSADoB[0]);
                int month =Integer.parseInt(textSADoB[1])-1; //to take care of month index starting from
                int year =Integer.parseInt(textSADoB[2]);

                DatePickerDialog picker;

                //Date Picker Dialog
                picker = new DatePickerDialog(updateLawyerProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ETBirthday.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        //update profile
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capture updated data
                name = ETName.getText().toString().trim();
                doB = ETBirthday.getText().toString().trim();
                mobile = ETPhoneNo.getText().toString().trim();
                txtState = ACTVState.getEditableText().toString().trim();
                txtLanguage = ETLanguage.getText().toString().trim();
                expYear = ETExpYear.getText().toString().trim();
                barNumber = ETBarNumber.getText().toString().trim();
                qualification = ACTVQualification.getEditableText().toString().trim();
                lawFirm = ACTVLawFirm.getEditableText().toString().trim();
                specialization = ACTVSpecialization.getEditableText().toString().trim();

                // Validate and update profile
                updateProfile(firebaseUser);
            }
        });
    }
    //update profile
    private void updateProfile(FirebaseUser firebaseUser) {
        email = firebaseUser.getEmail();
        int selectedGenderID = rgGender.getCheckedRadioButtonId();
        rbGender = findViewById(selectedGenderID);
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(updateLawyerProfile.this, "Please enter your full name", Toast.LENGTH_LONG).show();
            ETName.setError("Full name is required");
            ETName.requestFocus();
        } else if (TextUtils.isEmpty(txtLanguage)) {
            Toast.makeText(updateLawyerProfile.this, "Please enter your language spoken", Toast.LENGTH_LONG).show();
            ETLanguage.setError("Language spoken is required");
            ETLanguage.requestFocus();
        } else if (TextUtils.isEmpty(barNumber)) {
            Toast.makeText(updateLawyerProfile.this, "Please enter your bar number", Toast.LENGTH_LONG).show();
            ETBarNumber.setError("Bar number is required");
            ETBarNumber.requestFocus();
        }else if (TextUtils.isEmpty(expYear)) {
            Toast.makeText(updateLawyerProfile.this, "Please enter your experience year", Toast.LENGTH_LONG).show();
            ETExpYear.setError("Year of experience is required");
            ETExpYear.requestFocus();
        }else if (TextUtils.isEmpty(doB)) {
            Toast.makeText(updateLawyerProfile.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
            ETBirthday.setError("Date of birth is required");
            ETBirthday.requestFocus();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(updateLawyerProfile.this, "Please enter your phone number", Toast.LENGTH_LONG).show();
            ETPhoneNo.setError("Phone number is required");
            ETPhoneNo.requestFocus();
        } else if (mobile.length()<10) {
            Toast.makeText(updateLawyerProfile.this, "Please re-enter your phone number", Toast.LENGTH_LONG).show();
            ETPhoneNo.setError("Phone number should not less than 10 digits");
            ETPhoneNo.requestFocus();
        } else if (TextUtils.isEmpty(rbGender.getText())) {
            Toast.makeText(updateLawyerProfile.this, "Please select your gender", Toast.LENGTH_LONG).show();
            rbGender.setError("Gender is required");
            rbGender.requestFocus();
        } else if (TextUtils.isEmpty(txtState)) {
            Toast.makeText(updateLawyerProfile.this, "Please select your state", Toast.LENGTH_LONG).show();
            ACTVState.setError("State is required");
            ACTVState.requestFocus();
        }else if (TextUtils.isEmpty(lawFirm)) {
            Toast.makeText(updateLawyerProfile.this, "Please select your law firm", Toast.LENGTH_LONG).show();
            ACTVLawFirm.setError("Law Firm is required");
            ACTVLawFirm.requestFocus();
        }else if (TextUtils.isEmpty(specialization)) {
            Toast.makeText(updateLawyerProfile.this, "Please select your specialization", Toast.LENGTH_LONG).show();
            ACTVSpecialization.setError("Specialization is required");
            ACTVSpecialization.requestFocus();
        }else if (TextUtils.isEmpty(qualification)) {
            Toast.makeText(updateLawyerProfile.this, "Please select your qualification", Toast.LENGTH_LONG).show();
            ACTVQualification.setError("Qualification is required");
            ACTVQualification.requestFocus();
        } else {
            //Obtain the data entered by user
            txtGender = rbGender.getText().toString();
            name =ETName.getText().toString();
            doB = ETBirthday.getText().toString();
            mobile = ETPhoneNo.getText().toString();
            txtState = ACTVState.getText().toString();
            lawFirm = ACTVLawFirm.getText().toString();
            specialization = ACTVSpecialization.getText().toString();
            qualification = ACTVQualification.getText().toString();
            txtLanguage=ETLanguage.getText().toString();
            expYear = ETExpYear.getText().toString();
            barNumber = ETBarNumber.getText().toString();

            //Enter User Data into the Firebase Realtime Database. Set up dependencies
            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(email,doB,txtGender,mobile, txtState, name,  txtLanguage,  barNumber,  expYear, lawFirm, specialization, qualification);

            //extract user reference from Database for "Registered Lawyers"
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Lawyers");
            String userID = firebaseUser.getUid();
            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //Setting new display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(updateLawyerProfile.this,"Update successful!",Toast.LENGTH_LONG).show();

                        //Stop user from returning to userSetting on pressing back button and close activity
                        Intent intent = new Intent(updateLawyerProfile.this,lawyerProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        try{
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(updateLawyerProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
    //fetch data from Firebase and display
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Extracting User Reference from the Database for "Registered Lawyers"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Lawyers");
        referenceProfile.child(userID)  //From "Registered Lawyers", find the ID of user and add listener
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        if (readUserDetails != null) {
                            name = readUserDetails.name;
                            doB = readUserDetails.doB;
                            txtGender = readUserDetails.gender;
                            mobile = readUserDetails.mobile;
                            txtState =readUserDetails.state;
                            txtLanguage = readUserDetails.language;
                            expYear = readUserDetails.expYear;
                            barNumber = readUserDetails.barNumber;
                            lawFirm =readUserDetails.lawFirm;
                            specialization =readUserDetails.specialization;
                            qualification=readUserDetails.qualification;

                            // Populate UI elements with user data
                            ETName.setText(name);
                            ETBirthday.setText(doB);
                            ETPhoneNo.setText(mobile);
                            ACTVState.setText(txtState);
                            ETLanguage.setText(txtLanguage);
                            ETBarNumber.setText(barNumber);
                            ETExpYear.setText(expYear);
                            ACTVSpecialization.setText(specialization);
                            ACTVQualification.setText(qualification);
                            ACTVLawFirm.setText(lawFirm);

                            // Enable EditText fields to make them editable
                            ETName.setEnabled(true);
                            ETBirthday.setEnabled(true);
                            ETPhoneNo.setEnabled(true);
                            ETLanguage.setEnabled(true);
                            ETExpYear.setEnabled(true);
                            ETBarNumber.setEnabled(true);

                            // Show Gender through Radio Button
                            if (txtGender.equals("Male")) {
                                rbGender = findViewById(R.id.radio_male);
                            } else {
                                rbGender = findViewById(R.id.radio_female);
                            }
                            rbGender.setChecked(true);  //set the previous selected gender as default

                            // Set state in AutoCompleteTextView
                            String[] states = getResources().getStringArray(R.array.state);
                            ArrayAdapter<String> adapterState = new ArrayAdapter<>(updateLawyerProfile.this,R.layout.dropdown_item, states);
                            ACTVState.setAdapter(adapterState);

                            // Select the state if it matches the user's state
                            if (readUserDetails.state != null) {
                                int position = adapterState.getPosition(readUserDetails.state);
                                if (position != -1) {
                                    ACTVState.setText(adapterState.getItem(position), false);
                                }
                            }
                            // Handle item selection in AutoCompleteTextView
                            ACTVState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedState = parent.getItemAtPosition(position).toString();
                                    // Do something with the selected state if needed
                                    Toast.makeText(updateLawyerProfile.this, "Selected State: " + selectedState, Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Set specialization in AutoCompleteTextView
                            String[] specializations = getResources().getStringArray(R.array.specialization);
                            ArrayAdapter<String> adapterSpecialization = new ArrayAdapter<>(updateLawyerProfile.this,R.layout.dropdown_item, specializations);
                            ACTVSpecialization.setAdapter(adapterSpecialization);

                            // Select the specialization if it matches the user's specialization
                            if (readUserDetails.specialization != null) {
                                int position = adapterSpecialization.getPosition(readUserDetails.specialization);
                                if (position != -1) {
                                    ACTVSpecialization.setText(adapterSpecialization.getItem(position), false);
                                }
                            }
                            // Handle item selection in AutoCompleteTextView
                            ACTVSpecialization.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedSpecialization = parent.getItemAtPosition(position).toString();
                                    // Do something with the selected specialization if needed
                                    Toast.makeText(updateLawyerProfile.this, "Selected Specialization: " + selectedSpecialization, Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Set lawFirm in AutoCompleteTextView
                            String[] lawFirm = getResources().getStringArray(R.array.lawFirm);
                            ArrayAdapter<String> adapterLawFirm = new ArrayAdapter<>(updateLawyerProfile.this,R.layout.dropdown_item, lawFirm);
                            ACTVLawFirm.setAdapter(adapterLawFirm);

                            // Select the specialization if it matches the user's lawFirm
                            if (readUserDetails.lawFirm != null) {
                                int position = adapterLawFirm.getPosition(readUserDetails.lawFirm);
                                if (position != -1) {
                                    ACTVLawFirm.setText(adapterLawFirm.getItem(position), false);
                                }
                            }
                            // Handle item selection in AutoCompleteTextView
                            ACTVLawFirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedLawFirm = parent.getItemAtPosition(position).toString();
                                    // Do something with the selected specialization if needed
                                    Toast.makeText(updateLawyerProfile.this, "Selected Law Firm: " + selectedLawFirm, Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Set qualification in AutoCompleteTextView
                            String[] qualifications = getResources().getStringArray(R.array.qualification);
                            ArrayAdapter<String> adapterQualification= new ArrayAdapter<>(updateLawyerProfile.this,R.layout.dropdown_item, qualifications);
                            ACTVQualification.setAdapter(adapterQualification);

                            // Select the qualification if it matches the user's qualification
                            if (readUserDetails.qualification != null) {
                                int position = adapterQualification.getPosition(readUserDetails.qualification);
                                if (position != -1) {
                                    ACTVQualification.setText(adapterQualification.getItem(position), false);
                                }
                            }
                            // Handle item selection in AutoCompleteTextView
                            ACTVQualification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedQualification = parent.getItemAtPosition(position).toString();
                                    // Do something with the selected Qualification if needed
                                    Toast.makeText(updateLawyerProfile.this, "Selected Qualification: " + selectedQualification, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            Toast.makeText(updateLawyerProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(updateLawyerProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
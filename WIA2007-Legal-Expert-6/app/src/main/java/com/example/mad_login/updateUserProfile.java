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

public class updateUserProfile extends AppCompatActivity {
    private EditText ETName,ETPhoneNo,ETBirthday;
    private ImageView IVback;
    private Button btnUpdate;
    private FirebaseAuth authProfile;
    private String name,mobile,txtState,txtGender,doB;
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private AutoCompleteTextView ACTVState;
    private ArrayAdapter<String> adapterState;
    private static final String[] STATES = {"Selangor", "Kuala Lumpur", "Labuan", "Johor", "Perlis", "Sabah", "Sarawak",
            "Melaka", "Pulau Penang", "Pahang", "Perak", "Negeri Sembilan", "Putrajaya", "Kelantan", "Kedah", "Terengganu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

//        //init
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //show Profile Data
        showUserProfile(firebaseUser);


        ETName =findViewById(R.id.ETName);
        ETBirthday = findViewById(R.id.ETBirthday);
        ETPhoneNo =findViewById(R.id.ETPhoneNo);
        btnUpdate =findViewById(R.id.btnUpdate);
        IVback = findViewById(R.id.IVback);

        IVback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(updateUserProfile.this, userSetting.class));
                finish();
            }
        });

        //RadioButton for Gender
        rgGender =findViewById(R.id.rgGender);

        //dropdown for state
        ACTVState = findViewById(R.id.ACTVState);

        //dropdown for state
        adapterState = new ArrayAdapter<>(updateUserProfile.this, R.layout.dropdown_item, STATES);
        ACTVState.setAdapter(adapterState);
        ACTVState.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(updateUserProfile.this, "Selected State " + item, Toast.LENGTH_SHORT).show();
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
                picker = new DatePickerDialog(updateUserProfile.this, new DatePickerDialog.OnDateSetListener() {
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

                // Validate and update profile
                updateProfile(firebaseUser);
            }
        });
    }
//update profile
    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID = rgGender.getCheckedRadioButtonId();
        rbGender = findViewById(selectedGenderID);
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(updateUserProfile.this, "Please enter your full name", Toast.LENGTH_LONG).show();
            ETName.setError("Full name is required");
            ETName.requestFocus();
        } else if (TextUtils.isEmpty(doB)) {
            Toast.makeText(updateUserProfile.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
            ETBirthday.setError("Date of birth is required");
            ETBirthday.requestFocus();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(updateUserProfile.this, "Please enter your phone number", Toast.LENGTH_LONG).show();
            ETPhoneNo.setError("Phone number is required");
            ETPhoneNo.requestFocus();
        } else if (mobile.length()<10) {
            Toast.makeText(updateUserProfile.this, "Please re-enter your phone number", Toast.LENGTH_LONG).show();
            ETPhoneNo.setError("Phone number should not less than 10 digits");
            ETPhoneNo.requestFocus();
        } else if (TextUtils.isEmpty(rbGender.getText())) {
            Toast.makeText(updateUserProfile.this, "Please select your gender", Toast.LENGTH_LONG).show();
            rbGender.setError("Gender is required");
            rbGender.requestFocus();
        } else if (TextUtils.isEmpty(txtState)) {
            Toast.makeText(updateUserProfile.this, "Please select your state", Toast.LENGTH_LONG).show();
            ACTVState.setError("State is required");
            ACTVState.requestFocus();
        } else {
            //Obtain the data entered by user
            txtGender = rbGender.getText().toString();
            name =ETName.getText().toString();
            doB = ETBirthday.getText().toString();
            mobile = ETPhoneNo.getText().toString();
            txtState = ACTVState.getText().toString();

            //Enter User Data into the Firebase Realtime Database. Set up dependencies
            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtGender,txtState,mobile,name,doB);

            //extract user reference from Database for "Registered Users"
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userID = firebaseUser.getUid();
            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //Setting new display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(updateUserProfile.this,"Update successful!",Toast.LENGTH_LONG).show();

                        //back to userSetting after successfully update profile
                        Intent intent = new Intent(updateUserProfile.this,userSetting.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        try{
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(updateUserProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
    //fetch data from Firebase and display
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Extracting User Reference from the Database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID)  //From "Registered Users", find the ID of user and add listener
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    name = firebaseUser.getDisplayName();
                    doB = readUserDetails.doB;
                    txtGender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;
                    txtState =readUserDetails.state;

                    // Populate UI elements with user data
                    ETName.setText(name);
                    ETBirthday.setText(doB);
                    ETPhoneNo.setText(mobile);
                    ACTVState.setText(txtState);


                    // Enable EditText fields to make them editable
                    ETName.setEnabled(true);
                    ETBirthday.setEnabled(true);
                    ETPhoneNo.setEnabled(true);

                    // Show Gender through Radio Button
                    if (txtGender.equals("Male")) {
                        rbGender = findViewById(R.id.radio_male);
                    } else {
                        rbGender = findViewById(R.id.radio_female);
                    }
                    rbGender.setChecked(true);  //set the previous selected gender as default

                    // Set state in AutoCompleteTextView
                    String[] states = getResources().getStringArray(R.array.state); // Corrected array name
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(updateUserProfile.this,R.layout.dropdown_item, states);
                    ACTVState.setAdapter(adapter);

                    // Select the state if it matches the user's state
                    if (readUserDetails.state != null) {
                        int position = adapter.getPosition(readUserDetails.state);
                        if (position != -1) {
                            ACTVState.setText(adapter.getItem(position), false);
                        }
                    }
                    // Handle item selection in AutoCompleteTextView
                    ACTVState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedState = parent.getItemAtPosition(position).toString();
                            // Do something with the selected state if needed
                            Toast.makeText(updateUserProfile.this, "Selected State: " + selectedState, Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(updateUserProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(updateUserProfile.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    }
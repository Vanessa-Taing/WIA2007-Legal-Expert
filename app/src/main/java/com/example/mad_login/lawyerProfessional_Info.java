package com.example.mad_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class lawyerProfessional_Info extends AppCompatActivity {
    private EditText ETBarNumber,ETExpYear;
    private String[] lawFirm = {"LING & THENG BOOK","other"};
    private String[] specialization = {"civil","consumer" , "contract", "criminal" , "family" ,"islamic"};
    private String[] qualification ={"CERTIFICATE IN LEGAL PRACTICE(CLP)","other"};
    private AutoCompleteTextView ACTVLawFirm,ACTVSpecialization,ACTVQualification;
    private ArrayAdapter<String> adapterLawFirm,adapterSpecialization,adapterQualification;
    private Button btnSubmit;
    private String txtName,txtEmail;
    private String txtGender;
    private String txtState;
    private String txtPhoneNum;
    private String txtBirthday;
    private String txtLanguage;


    //Add Firebase database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_professional_info);

        // Retrieve data from the Intent
        Intent intent = getIntent();
        txtName = intent.getStringExtra("name");
        txtEmail = intent.getStringExtra("email");
        txtLanguage = intent.getStringExtra("language");
        txtPhoneNum = intent.getStringExtra("phoneNum");
        txtBirthday = intent.getStringExtra("doB");
        txtState = intent.getStringExtra("state");
        txtGender = intent.getStringExtra("gender");

        //initialization
        ETBarNumber = findViewById(R.id.ETBarNumber);
        ETExpYear = findViewById(R.id.ETExpYear);

        //dropdown for LawFirm
        ACTVLawFirm = findViewById(R.id.ACTVLawFirm);
        adapterLawFirm = new ArrayAdapter<>(this, R.layout.dropdown_item, lawFirm);
        ACTVLawFirm.setAdapter(adapterLawFirm);
        ACTVLawFirm.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(lawyerProfessional_Info.this, "item " + item, Toast.LENGTH_SHORT).show();
            }
        });

        //dropdown for Specialization
        ACTVSpecialization = findViewById(R.id.ACTVSpecialization);
        adapterSpecialization = new ArrayAdapter<>(this, R.layout.dropdown_item, specialization);
        ACTVSpecialization.setAdapter(adapterSpecialization);
        ACTVSpecialization.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(lawyerProfessional_Info.this, "item " + item, Toast.LENGTH_SHORT).show();
            }
        });

        //dropdown for qualification
        ACTVQualification = findViewById(R.id.ACTVQualification);
        adapterQualification = new ArrayAdapter<>(this, R.layout.dropdown_item, qualification);
        ACTVQualification.setAdapter(adapterQualification);
        ACTVQualification.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(lawyerProfessional_Info.this, "item " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // Add TextChangedListener to enable/disable the submit button based on input
        addTextChangedListener();

        // Add OnClickListener to the submit button
        btnSubmit = findViewById(R.id.btnLawyerSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the submit button click
                saveToFirebase();
            }
        });
    }

    private void addTextChangedListener() {
        // Add TextChangedListener to enable/disable the submit button based on input
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Enable/disable the submit button based on input
                checkInputAndEnableButton();
            }
        };
        ETExpYear.addTextChangedListener(textWatcher);
        // Add TextChangedListener for law firm AutoCompleteTextView
        ACTVLawFirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Enable/disable the submit button based on input
                checkInputAndEnableButton();
            }
        });

// Add TextChangedListener for specialization AutoCompleteTextView
        ACTVSpecialization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Enable/disable the submit button based on input
                checkInputAndEnableButton();
            }
        });

// Add TextChangedListener for qualification AutoCompleteTextView
        ACTVQualification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed in this case
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Enable/disable the submit button based on input
                checkInputAndEnableButton();
            }
        });
    }

    private void checkInputAndEnableButton() {
        // Check if all required fields are filled to enable the submit button
        String barNumber = ETBarNumber.getText().toString().trim();
        String expYear = ETExpYear.getText().toString().trim();
        String lawFirmText = ACTVLawFirm.getEditableText().toString().trim();
        String specializationText = ACTVSpecialization.getEditableText().toString().trim();
        String qualificationText = ACTVQualification.getEditableText().toString().trim();

        boolean lawFirmSelected = containsItem(lawFirm, lawFirmText);
        boolean specializationSelected = containsItem(specialization, specializationText);
        boolean qualificationSelected = containsItem(qualification, qualificationText);

        Log.d("CheckButton", "lawFirmSelected: " + lawFirmSelected);
        Log.d("CheckButton", "specializationSelected: " + specializationSelected);
        Log.d("CheckButton", "qualificationSelected: " + qualificationSelected);

        boolean enableButton = !barNumber.isEmpty() && !expYear.isEmpty()
                && lawFirmSelected && specializationSelected && qualificationSelected;

        btnSubmit.setEnabled(enableButton);
    }

    private boolean containsItem(String[] array, String item) {
        for (String value : array) {
            if (value.toLowerCase().equals(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    private void saveToFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Get entered data
            String barNumber = ETBarNumber.getText().toString();
            String expYear = ETExpYear.getText().toString();
            String lawFirm = ACTVLawFirm.getText().toString();
            String specialization = ACTVSpecialization.getText().toString();
            String qualification = ACTVQualification.getText().toString();

            // Update existing user node with professional information
            databaseReference.child("Registered Lawyers").child(userId).child("barNumber").setValue(barNumber);
            databaseReference.child("Registered Lawyers").child(userId).child("expYear").setValue(expYear);
            databaseReference.child("Registered Lawyers").child(userId).child("lawFirm").setValue(lawFirm);
            databaseReference.child("Registered Lawyers").child(userId).child("specialization").setValue(specialization);
            databaseReference.child("Registered Lawyers").child(userId).child("qualification").setValue(qualification);

            // Create an instance of ReadWriteUserDetails and set its properties
            ReadWriteUserDetails userDetails = new ReadWriteUserDetails(txtEmail,txtBirthday,txtGender,txtPhoneNum,txtState,txtName, txtLanguage,barNumber,expYear,lawFirm,specialization,qualification);

            // Save data to Firebase
            databaseReference.child("Registered Lawyers").child(userId).setValue(userDetails)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "You have sign up successfully", Toast.LENGTH_LONG).show();
                            // Intent to switch to the profile page
                            Intent intent = new Intent(this, lawyerLoginPage.class);
                            startActivity(intent);

                            // Finish the current activity to prevent going back to it from the profile page
                            finish();
                        } else {
                            // Failed to save data
                            Toast.makeText(this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
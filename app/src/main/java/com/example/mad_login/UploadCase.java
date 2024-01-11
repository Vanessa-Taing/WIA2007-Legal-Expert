package com.example.mad_login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UploadCase extends Fragment {


    private static final int UPLOAD_DOC_REQUEST_CODE = 1;
    private String[] caseType = {"civil","consumer" , "contract", "criminal" , "family" ,"islamic"};
    private AutoCompleteTextView ACTVCaseType;
    private ArrayAdapter<String> adapterCaseType;
    private EditText ETCaseName, ETCaseDescription;
    private Button btnSubmit,btnUplaodDoc;
    private ProgressBar progressBar;
    private TextView TVSkipUpload;
    private static final String TAG = "userCaseDescription";

    private String userId,txtCaseName,txtCaseDescription,txtCaseType,documentUrl;

    public UploadCase() {
        // Required empty public constructor
    }


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_case, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ETCaseName = view.findViewById(R.id.ETCaseName);
        ETCaseDescription = view.findViewById(R.id.ETCaseDescription);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        progressBar = view.findViewById(R.id.progressBar);
        ACTVCaseType = view.findViewById(R.id.ACTVCaseType);
        btnUplaodDoc = view.findViewById(R.id.btnUplaodDoc);

        btnUplaodDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), uploadDoc.class), UPLOAD_DOC_REQUEST_CODE);
            }
        });

        //set when user click skip upload, switch to login page


        // Set up the AutoCompleteTextView for caseType
        adapterCaseType = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, caseType);
        ACTVCaseType.setAdapter(adapterCaseType);

        // Set up item click listener for caseType
        ACTVCaseType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), "Selected item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up click listener for the Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Double Confirmation");
                builder.setMessage("Are you absolutely sure you want to submit this case?");
                builder.setPositiveButton("Yes, Submit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String userId = "no UID";
                                userId = currentUser.getUid();

                                uploadCase(userId, txtCaseName, txtCaseDescription, txtCaseType, documentUrl);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();

                // Obtain the entered data
                txtCaseName = ETCaseName.getText().toString();
                txtCaseDescription = ETCaseDescription.getText().toString();
                txtCaseType = ACTVCaseType.getText().toString();

                // Check if documentUrl is not null
                if (TextUtils.isEmpty(txtCaseName) || TextUtils.isEmpty(txtCaseDescription) || TextUtils.isEmpty(txtCaseType)) {
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    // Get the current user
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        dialog.show();

                    } else {
                        // Handle the case where the user is not authenticated
                        Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void uploadCase(String userId, String txtCaseName, String txtCaseDescription, String txtCaseType, String documentUrl) {
        // Get a reference to the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("Cases");

        // Create a Case object with the entered data, including the document information
        documentedCaseModel userCase = new documentedCaseModel(txtCaseName, txtCaseDescription, txtCaseType, documentUrl);

        // Use the reference to the database to push a new child node with a unique ID for the case
        String caseId = databaseReference.push().getKey();

        databaseReference.child(caseId).setValue(userCase)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(getActivity(), "Your case is submitted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), documentedCase.class));

                        } else {
                            // Handle the error
                            Toast.makeText(getActivity(), "Failed to submit your case", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
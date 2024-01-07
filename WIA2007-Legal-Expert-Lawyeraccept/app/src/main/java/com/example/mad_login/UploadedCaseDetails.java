package com.example.mad_login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadedCaseDetails extends AppCompatActivity {

    Button viewDocBtn ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_case_details);

        // Retrieve data from intent
        Intent intent = getIntent();
        String caseName = intent.getStringExtra("caseName");
        String caseType = intent.getStringExtra("caseType");
        String caseDescription = intent.getStringExtra("caseDescription");

        String caseId = intent.getStringExtra("caseId");
        String currentUserId = intent.getStringExtra("lawyerId");
        String clientId = intent.getStringExtra("clientId");
        String caseStatus = intent.getStringExtra("status");



        //String documentUrl = intent.getStringExtra("documentUrl");
        // Set data to TextViews
        TextView caseNameTextView = findViewById(R.id.caseName);
        TextView caseTypeTextView = findViewById(R.id.caseType);
        TextView caseDescriptionTextView = findViewById(R.id.caseDescription);

        caseNameTextView.setText("Case Name: " + caseName);
        caseTypeTextView.setText("Case Type: " + caseType);
        caseDescriptionTextView.setText("Case Description: " + caseDescription);



        Button terminateBtn = findViewById(R.id.btnTerminate);
        terminateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the status is one of the allowed statuses for termination
                if (isTerminationAllowed(caseStatus)) {
                    showTerminateCaseDialog(caseId,clientId,currentUserId,view.getContext());}
                else {
                    showToast(view.getContext(), "Termination is not allowed for the current case status.");
                }
            }
        });


        // Set up ActionBar
        ActionBar actionBar = getSupportActionBar();

        // Check if ActionBar is not null
        if (actionBar != null) {
            // Set ActionBar background drawable or any other customization
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.my_primary)));
        } else {
            // Log a message or handle the situation where ActionBar is null
            Log.e("ActionBar", "ActionBar is null");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Case Details");


    }




    private boolean isTerminationAllowed(String caseStatus) {
        return  "accepted".equals(caseStatus) ||
                 "Initiate".equals(caseStatus) ||
                "OnGoing".equals(caseStatus) || "OnHold".equals(caseStatus);
    }

    private void showTerminateCaseDialog(String caseId , String currentUserId , String receiverId, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_terminate_case, null);
        RadioGroup radioGroupReason = view.findViewById(R.id.radioGroupReason);
        EditText etCustomReason = view.findViewById(R.id.etCustomReason);

        radioGroupReason.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioOtherReason) {
                etCustomReason.setVisibility(View.VISIBLE);
            } else {
                etCustomReason.setVisibility(View.GONE);
            }
        });

        alertDialogBuilder.setTitle("Select Terminate reason");
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton("Submit", (dialog, which) -> {
            int selectedRadioButtonId = radioGroupReason.getCheckedRadioButtonId();
            String selectedReason;

            if (selectedRadioButtonId == R.id.radioOtherReason) {
                // If "Other reasons" is selected, use the custom reason from EditText
                selectedReason = etCustomReason.getText().toString().trim();
            } else {
                RadioButton selectedRadioButton = view.findViewById(selectedRadioButtonId);
                selectedReason = selectedRadioButton.getText().toString();
            }

            if (!selectedReason.isEmpty()) {
                // Update the status with the selected reason
                updateStatusWithReason(caseId,currentUserId, receiverId,"Terminate", selectedReason, context);
            } else {
                showToast(context, "Please select or specify a reason for rejection.");
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateStatusWithReason(String caseId,String currentUserId , String receiverId, String newStatus, String reason, Context context) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Confirm Action");
        alertDialogBuilder.setMessage("Are you sure you want to terminate this case?");
        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

            DatabaseReference lawyerStatusRef = FirebaseDatabase.getInstance().getReference()
                    .child("Registered Lawyers")
                    .child(receiverId)
                    .child("ReceivedRequests")
                    .child(currentUserId)
                    .child(caseId);
            lawyerStatusRef.child("status").setValue(newStatus);
            lawyerStatusRef.child("TerminateReason").setValue(reason);

            DatabaseReference userStatusRef = FirebaseDatabase.getInstance().getReference()
                    .child("Registered Users")
                    .child(currentUserId)
                    .child("Cases")
                    .child(caseId)
                    .child("SentRequest")
                    .child(receiverId);
            userStatusRef.child("status").setValue(newStatus);
            userStatusRef.child("TerminateReason").setValue(reason);

            showToast(context, "Case " + newStatus + " with reason: " + reason);
        });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

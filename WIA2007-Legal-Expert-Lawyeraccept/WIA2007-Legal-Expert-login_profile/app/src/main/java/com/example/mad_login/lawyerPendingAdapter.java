package com.example.mad_login;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class lawyerPendingAdapter extends RecyclerView.Adapter<lawyerPendingAdapter.ViewHolder> {

    private List<client_CaseModel> pendingCaseList;
    private String currentLawyerID;
    private TextView textNoAcceptedCases;

    public lawyerPendingAdapter(List<client_CaseModel> pendingCaseList,String currentLawyerID) {
        this.pendingCaseList = pendingCaseList;
        this.currentLawyerID = currentLawyerID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_status_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        client_CaseModel caseModel = pendingCaseList.get(position);
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());
        holder.clientName.setText(caseModel.getClientName());

        holder.clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click event to navigate to UploadedCaseDetails activity
                Context context = view.getContext();
                Intent intent = new Intent(context, UploadedCaseDetails.class);

                // Pass relevant data to UploadedCaseDetails activity
                intent.putExtra("caseName", caseModel.getCaseName());
                intent.putExtra("caseType", caseModel.getCaseType());
                intent.putExtra("caseDescription", caseModel.getCaseDescription());
                // intent.putExtra("documentUrl", model.getDocumentUrl());

                context.startActivity(intent);
            }
        });

        // Accept button click listener
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(caseModel, "accepted",v.getContext());
            }
        });

        // Reject button click listener
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRejectionReasonDialog(caseModel, view.getContext());
            }
        });
    }

    private void showRejectionReasonDialog(client_CaseModel caseModel, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_rejection_reason, null);
        RadioGroup radioGroupReason = view.findViewById(R.id.radioGroupReason);
        EditText etCustomReason = view.findViewById(R.id.etCustomReason);

        radioGroupReason.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioOtherReason) {
                etCustomReason.setVisibility(View.VISIBLE);
            } else {
                etCustomReason.setVisibility(View.GONE);
            }
        });

        alertDialogBuilder.setTitle("Reject Case with Reason");
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
                updateStatusWithReason(caseModel, "rejected", selectedReason, context);
            } else {
                showToast(context, "Please select or specify a reason for rejection.");
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateStatusWithReason(client_CaseModel caseModel, String newStatus, String reason, Context context) {
        String senderID = caseModel.getClientID();
        String caseID = caseModel.getCaseID();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Confirm Action");
        alertDialogBuilder.setMessage("Are you sure you want to reject this case?");
        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

            DatabaseReference lawyerStatusRef = FirebaseDatabase.getInstance().getReference()
                    .child("Registered Lawyers")
                    .child(currentLawyerID)
                    .child("ReceivedRequests")
                    .child(senderID)
                    .child(caseID);
            lawyerStatusRef.child("status").setValue(newStatus);
            lawyerStatusRef.child("rejectionReason").setValue(reason);

            DatabaseReference userStatusRef = FirebaseDatabase.getInstance().getReference()
                    .child("Registered Users")
                    .child(senderID)
                    .child("Cases")
                    .child(caseID)
                    .child("SentRequest")
                    .child(currentLawyerID);
            userStatusRef.child("status").setValue(newStatus);
            userStatusRef.child("rejectionReason").setValue(reason);

            showToast(context, "Case " + newStatus + " with reason: " + reason);
        });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateStatus(client_CaseModel caseModel, String newStatus,Context context) {
        String senderID = caseModel.getClientID();
        String caseID = caseModel.getCaseID();

        String confirmationMessage = (newStatus.equals("accepted"))
                ? "Are you sure you want to accept this case?"
                : "Are you sure you want to reject this case?";

        //alert dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Confirm Action");
        alertDialogBuilder.setMessage(confirmationMessage);
        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

            DatabaseReference lawyerStatusRef = FirebaseDatabase.getInstance().getReference()
                .child("Registered Lawyers")
                .child(currentLawyerID)
                .child("ReceivedRequests")
                .child(senderID)
                .child(caseID);
            lawyerStatusRef.child("status").setValue(newStatus);

            DatabaseReference userStatusRef = FirebaseDatabase.getInstance().getReference()
                .child("Registered Users")
                .child(senderID)
                .child("Cases")
                .child(caseID)
                .child("SentRequest")
                .child(currentLawyerID);
            userStatusRef.child("status").setValue(newStatus);

            showToast(context, "Case " + newStatus + "!");
        });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return pendingCaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView caseName, caseType, clickableText,clientName;
        Button btnAccept , btnReject ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            clientName = itemView.findViewById(R.id.clientName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}

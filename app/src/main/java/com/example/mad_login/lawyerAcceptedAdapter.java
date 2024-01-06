package com.example.mad_login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class lawyerAcceptedAdapter extends RecyclerView.Adapter<lawyerAcceptedAdapter.ViewHolder> {

    private List<client_CaseModel> acceptedCaseList ;
    private Context context;
    private AlertDialog alertDialog;
    private FirebaseUser firebaseUser;
    private String currentLawyerID;

    public lawyerAcceptedAdapter(List<client_CaseModel> acceptedCaseList,String currentLawyerID) {
        this.acceptedCaseList = acceptedCaseList;
        this.currentLawyerID = currentLawyerID ;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_case_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lawyerAcceptedAdapter.ViewHolder holder, int position) {
        client_CaseModel caseModel = acceptedCaseList.get(position);
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());
        holder.clientName.setText(caseModel.getClientName());



        holder.btnUpdateCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateCaseDialog(caseModel,v.getContext());
            }


        });

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

                intent.putExtra("clientId",caseModel.getClientID());
                intent.putExtra("caseId",caseModel.getCaseID());
                intent.putExtra("lawyerId",currentLawyerID);
                intent.putExtra("status",caseModel.getStatus());

                context.startActivity(intent);
            }
        });

    }


    private void showUpdateCaseDialog(client_CaseModel caseModel,Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_case, null);

        RadioGroup radioGroupStatus = view.findViewById(R.id.radioGroupStatus);
        alertDialogBuilder.setTitle("Update Case");
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton("Update", (dialog, which) -> {
            int selectedCaseStatusId = radioGroupStatus.getCheckedRadioButtonId();
            String selectedStatus;

            RadioButton selectedRadioButton = view.findViewById(selectedCaseStatusId);
            selectedStatus = selectedRadioButton.getText().toString();


            if (!selectedStatus.isEmpty()) {
                // Update the status with the selected reason
                Log.e("YourTag", "Selected status available.");
                updateStatus(caseModel,  selectedStatus, context);
            } else {
                Log.e("YourTag", "Selected status is empty.");
                showToast(context, "Please select the case status.");
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateStatus(client_CaseModel caseModel, String newCaseStatus, Context context) {
        String senderID = caseModel.getClientID();
        String caseID = caseModel.getCaseID();

        Log.d("YourTag", "currentLawyerID: " + currentLawyerID);
        Log.d("YourTag", "senderID: " + senderID);
        Log.d("YourTag", "caseID: " + caseID);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Confirm Action");
        alertDialogBuilder.setMessage("Are you sure you want to update this case?");
        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {


            if (currentLawyerID != null) {
            DatabaseReference lawyerStatusRef = FirebaseDatabase.getInstance().getReference()
                    .child("Registered Lawyers")
                    .child(currentLawyerID)
                    .child("ReceivedRequests")
                    .child(senderID)
                    .child(caseID);
            lawyerStatusRef.child("status").setValue(newCaseStatus);

            DatabaseReference userStatusRef = FirebaseDatabase.getInstance().getReference()
                    .child("Registered Users")
                    .child(senderID)
                    .child("Cases")
                    .child(caseID)
                    .child("SentRequest")
                    .child(currentLawyerID);
            userStatusRef.child("status").setValue(newCaseStatus);

            showToast(context, "Case is being updated to " + newCaseStatus);
            } else {
                Log.e("YourTag", "Current lawyer ID is empty or null");
                showToast(context, "Unable to update case status. Please try again.");
            }
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
      return acceptedCaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView caseName, caseType, clickableText, clientName;
        Button btnUpdateCase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            clientName = itemView.findViewById(R.id.clientName);
            btnUpdateCase = itemView.findViewById(R.id.btnUpdateCase);

        }
    }

}

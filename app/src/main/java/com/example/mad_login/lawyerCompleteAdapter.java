package com.example.mad_login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class lawyerCompleteAdapter extends RecyclerView.Adapter<lawyerCompleteAdapter.ViewHolder>  {
    private List<client_CaseModel> activeCaseList ;
    private Context context;
    private AlertDialog alertDialog;
    private FirebaseUser firebaseUser;
    private String currentLawyerID;

            public lawyerCompleteAdapter(List<client_CaseModel> activeCaseList,String currentLawyerID) {
        this.activeCaseList = activeCaseList;
        this.currentLawyerID = currentLawyerID ;
    }

    public lawyerCompleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.lawyer_complete_item, parent, false);
        return new lawyerCompleteAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull lawyerCompleteAdapter.ViewHolder holder, int position) {
        client_CaseModel caseModel = activeCaseList.get(position);
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());
        holder.clientName.setText(caseModel.getClientName());
        holder.activeCaseStatus.setText("Case status: " + caseModel.getStatus());


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

    }




    @Override
    public int getItemCount() {
        return activeCaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView caseName, caseType, clickableText, clientName, activeCaseStatus;
        Button btnUpdateCase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            clientName = itemView.findViewById(R.id.clientName);
            btnUpdateCase = itemView.findViewById(R.id.btnUpdateCase);
            activeCaseStatus = itemView.findViewById(R.id.TVactiveCaseStatus);

        }
    }

}

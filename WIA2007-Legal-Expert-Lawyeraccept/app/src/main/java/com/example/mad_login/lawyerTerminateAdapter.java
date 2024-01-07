package com.example.mad_login;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class lawyerTerminateAdapter extends RecyclerView.Adapter<lawyerTerminateAdapter.ViewHolder>{

    private List<client_CaseModel> rejectedCaseList;
    private String currentLawyerID;

    public lawyerTerminateAdapter(List<client_CaseModel> rejectedCaseList, String currentLawyerID) {
        this.rejectedCaseList = rejectedCaseList;
        this.currentLawyerID = currentLawyerID;
    }

    public lawyerTerminateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lawyer_terminate_item, parent, false);
        return new lawyerTerminateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lawyerTerminateAdapter.ViewHolder holder, int position) {
        client_CaseModel caseModel = rejectedCaseList.get(position);
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());
        holder.clientName.setText(caseModel.getClientName());
      holder.terminateReason.setText(caseModel.getRejectionReason());

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
        return rejectedCaseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView caseName, caseType, clickableText,clientName,terminateReason;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            terminateReason = itemView.findViewById(R.id.terminateReason);
            clientName = itemView.findViewById(R.id.clientName);
        }
    }
}

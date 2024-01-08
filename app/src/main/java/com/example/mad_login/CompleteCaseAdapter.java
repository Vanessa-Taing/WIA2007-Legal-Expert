package com.example.mad_login;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompleteCaseAdapter extends RecyclerView.Adapter<CompleteCaseAdapter.ViewHolder>{
    private List<CaseModel> caseList;
    private CompleteCaseAdapter.OnItemClickListener listener;

    public CompleteCaseAdapter(List<CaseModel> caseList, CompleteCaseAdapter.OnItemClickListener listener) {
        this.caseList = caseList;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(String caseId,String receiverId,String status,String caseType,String rejectionReason,String caseName);
    }
    public void onBindViewHolder(@NonNull CompleteCaseAdapter.ViewHolder holder, int position) {
        CaseModel caseModel = caseList.get(position);

        // Bind data to the views in the RecyclerView item
        String CaseName = caseModel.getCaseName().toString();
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());
        holder.statusTxt.setText("Case status: " + caseModel.getStatus());

        // Set a click listener for the clickableText
        holder.clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, UploadedCaseDetails.class);

                // Pass relevant data to UploadedCaseDetails activity
                intent.putExtra("caseName", caseModel.getCaseName());
                intent.putExtra("caseType", caseModel.getCaseType());
                intent.putExtra("caseDescription", caseModel.getCaseDescription());
                //   intent.putExtra("documentUrl",model.getDocumentUrl());

                context.startActivity(intent);
            }
        });



        holder.btnRateLawyer.setOnClickListener(new View.OnClickListener() {

            @Override
           public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, LawyerFeedback.class);

                // Pass relevant data to UploadedCaseDetails activity
                intent.putExtra("caseName", CaseName);
                intent.putExtra("caseType", caseModel.getCaseType());
                intent.putExtra("caseDescription", caseModel.getCaseDescription());
                intent.putExtra("caseLawyerID", caseModel.getLawyerID());
                intent.putExtra("caseID", caseModel.getCaseID());
                //   intent.putExtra("documentUrl",model.getDocumentUrl());

                context.startActivity(intent);
           }
        });
    }

    public CompleteCaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each RecyclerView item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_case_item, parent, false);
        return new CompleteCaseAdapter.ViewHolder(view);
    }

    public int getItemCount() {
        return caseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView caseName, caseType, clickableText,statusTxt;
        Button btnRateLawyer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            btnRateLawyer = itemView.findViewById(R.id.btnRateLawyer);
            statusTxt= itemView.findViewById(R.id.statusTxt);

        }
    }



}

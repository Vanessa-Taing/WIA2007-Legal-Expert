package com.example.legalexpert_casestatus2;

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

public class TerminationAdapter extends RecyclerView.Adapter<TerminationAdapter.ViewHolder>{

    private List<CaseModel> caseList;
    private TerminationAdapter.OnItemClickListener listener;

    public TerminationAdapter(List<CaseModel> caseList, TerminationAdapter.OnItemClickListener listener) {
        this.caseList = caseList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String caseId,String receiverId,String status,String caseType,String terminateReason,String caseName);
    }

    public void onBindViewHolder(@NonNull TerminationAdapter.ViewHolder holder, int position) {
        CaseModel caseModel = caseList.get(position);

        // Bind data to the views in the RecyclerView item
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());
        holder.caseStatus.setText("Case status: " + caseModel.getStatus());
        holder.terminateReason.setText("Terminate Reason "+ caseModel.getTerminateReason());



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


    }

    public TerminationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each RecyclerView item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.terminate_case_item, parent, false);
        return new TerminationAdapter.ViewHolder(view);
    }

    public int getItemCount() {
        return caseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView caseName, caseType, clickableText,caseStatus,terminateReason;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            caseStatus= itemView.findViewById(R.id.caseStatus);
            terminateReason = itemView.findViewById(R.id.terminateReason);

        }
    }

}


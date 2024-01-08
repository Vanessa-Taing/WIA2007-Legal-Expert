package com.example.mad_login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {


    private List<CaseModel> caseList;
    private OnItemClickListener listener;

    public RequestAdapter(List<CaseModel> caseList, OnItemClickListener listener) {
        this.caseList = caseList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String caseId,String receiverId,String status,String caseType,String rejectionReason,String caseName);
    }
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CaseModel caseModel = caseList.get(position);

        // Bind data to the views in the RecyclerView item
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());
        holder.statusTxt.setText("Request status: " + caseModel.getStatus());

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

                intent.putExtra("caseId",caseModel.getCaseID());
                intent.putExtra("currentUserId",caseModel.getCurrentUserId());
                intent.putExtra("receiverId",caseModel.getReceiverId());
                intent.putExtra("status",caseModel.getStatus());
                context.startActivity(intent);
            }
        });
        // Set the label for the btnLawyerDetails based on the status
        if ("rejected".equals(caseModel.getStatus())) {
            holder.btnLawyerDetails.setText("View Reject Details");
        } else {
            holder.btnLawyerDetails.setText("View Lawyer Details");
        }

       holder.btnLawyerDetails.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {
               listener.onItemClick(caseModel.getCaseID(),caseModel.getReceiverId(),caseModel.getStatus(),caseModel.getCaseType(),caseModel.getRejectionReason(),caseModel.getCaseName());
           }
       });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each RecyclerView item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return caseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView caseName, caseType, clickableText,statusTxt;
        Button btnLawyerDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            btnLawyerDetails = itemView.findViewById(R.id.btnLawyerDetails);
            statusTxt= itemView.findViewById(R.id.statusTxt);

        }
    }
}


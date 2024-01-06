package com.example.legalexpert_casestatus2;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.ViewHolder> {

    private List<CaseModel> submissionList;
    private OnItemClickListener listener;


    public SubmissionAdapter(List<CaseModel> submissionList, OnItemClickListener listener) {
        this.submissionList = submissionList;
        this.listener = listener;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views in the RecyclerView item
        //    holder.caseDescription.setText(model.getCaseDescription());
        CaseModel caseModel = submissionList.get(position);
        holder.caseName.setText(caseModel.getCaseName());
        holder.caseType.setText(caseModel.getCaseType());


        // Set a click listener for the clickableText
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
                //   intent.putExtra("documentUrl",model.getDocumentUrl());

                context.startActivity(intent);
            }
        });

        //click select lawyer button
        holder.selectLawyerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                listener.onItemClick(caseModel.getCaseID(),caseModel.getCaseType());
            }
        });
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each RecyclerView item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submission_item, parent, false);
        return new ViewHolder(view);
    }
    public int getItemCount() {
        return submissionList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  caseName, caseType,clickableText ;
        Button selectLawyerButton ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
            selectLawyerButton = itemView.findViewById(R.id.selectLawyerButton);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(String caseID,String caseType);
    }

}

package com.example.mad_login;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class caseAdapter extends FirebaseRecyclerAdapter<CaseModel4,caseAdapter.CaseViewHolder> {

private Context context;
    public caseAdapter(@NonNull FirebaseRecyclerOptions<CaseModel4> options , Context context) {
        super(options);
        this.context = context;
    }

    protected void onBindViewHolder(@NonNull CaseViewHolder holder, int position, @NonNull CaseModel4 model) {
        // Bind data to the views in the RecyclerView item
    //    holder.caseDescription.setText(model.getCaseDescription());
        holder.caseName.setText(model.getCaseName());
        holder.caseType.setText(model.getCaseType());

        // Set a click listener for the clickableText
        holder.clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click event to navigate to UploadedCaseDetails activity
                Context context = view.getContext();
                Intent intent = new Intent(context, UploadedCaseDetails.class);
                // Pass relevant data to UploadedCaseDetails activity
                intent.putExtra("caseName", model.getCaseName());
                intent.putExtra("caseType", model.getCaseType());
                intent.putExtra("caseDescription", model.getCaseDescription());
                intent.putExtra("documentUrl",model.getDocumentUrl());

                context.startActivity(intent);
            }
        });

        };

    @NonNull
    @Override
    public CaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each RecyclerView item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submission_item, parent, false);
        return new CaseViewHolder(view);
    }

    public static class CaseViewHolder extends RecyclerView.ViewHolder {
        TextView  caseName, caseType,clickableText;

        public CaseViewHolder(@NonNull View itemView) {
            super(itemView);
            caseName = itemView.findViewById(R.id.caseName);
            caseType = itemView.findViewById(R.id.caseType);
            clickableText = itemView.findViewById(R.id.clickableText);
        }
    }



}

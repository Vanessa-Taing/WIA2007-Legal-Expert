package com.example.mad_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RejectedRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_request);


        // Enable the up button
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Rejected Request details");

        //intent
        String caseID = getIntent().getStringExtra("caseId");
        String caseType = getIntent().getStringExtra("caseType");


        //rejected lawyer name
        TextView rejectedLawyer = findViewById(R.id.rejectName);
        String rejectedLawyerName = getIntent().getStringExtra("name");
        rejectedLawyer.setText(rejectedLawyerName);

        //rejectreason
        TextView TVrejectionReason = findViewById(R.id.TVrejectionReason);
        String rejectionReason = getIntent().getStringExtra("rejectionReason");
        TVrejectionReason.setText(rejectionReason);


        //reselect lawyer button
        Button btnReselectLawyer = findViewById(R.id.btnReselectLawyer);
        btnReselectLawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RejectedRequest.this, MainActivityLawyer.class);
                intent.putExtra("caseID", caseID);
                intent.putExtra("caseType", caseType);

                startActivity(intent);
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.mad_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class caseDetails extends AppCompatActivity {
    private TextView titleTextView, casetypeTextView, summaryTextView, urlTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details);

        titleTextView = findViewById(R.id.Dkeywords);
        casetypeTextView = findViewById(R.id.DcaseType);
        summaryTextView = findViewById(R.id.Dsummary);
        urlTextView = findViewById(R.id.Durl);

        String title = getIntent().getStringExtra("keywords");
        String casetype = getIntent().getStringExtra("casetype");
        String summary = getIntent().getStringExtra("summary");
        String url = getIntent().getStringExtra("url");

        titleTextView.setText(title);
        casetypeTextView.setText(casetype);
        summaryTextView.setText(summary);
        urlTextView.setText(url);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("case details");

//        Toolbar toolbar = findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
    }
}
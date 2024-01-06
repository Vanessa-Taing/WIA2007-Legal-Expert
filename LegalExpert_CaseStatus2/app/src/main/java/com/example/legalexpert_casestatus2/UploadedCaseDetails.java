package com.example.legalexpert_casestatus2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UploadedCaseDetails extends AppCompatActivity {

    Button viewDocBtn ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_case_details);




        // Retrieve data from intent
        Intent intent = getIntent();
        String caseName = intent.getStringExtra("caseName");
        String caseType = intent.getStringExtra("caseType");
        String caseDescription = intent.getStringExtra("caseDescription");
        String documentUrl = intent.getStringExtra("documentUrl");
        // Set data to TextViews
        TextView caseNameTextView = findViewById(R.id.caseName);
        TextView caseTypeTextView = findViewById(R.id.caseType);
        TextView caseDescriptionTextView = findViewById(R.id.caseDescription);

        caseNameTextView.setText("Case Name: " + caseName);
        caseTypeTextView.setText("Case Type: " + caseType);
        caseDescriptionTextView.setText("Case Description: " + caseDescription);



        // Set up ActionBar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Details");
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

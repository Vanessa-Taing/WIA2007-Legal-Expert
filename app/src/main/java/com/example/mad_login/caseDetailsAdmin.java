package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class caseDetailsAdmin extends AppCompatActivity {
    private TextView titleTextView, casetypeTextView, summaryTextView, urlTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_details_admin);
        Button btnDeleteCase = findViewById(R.id.btnDeleteCase);

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


        btnDeleteCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference casesRef = database.getReference("Cases Code");

                // Using the equalTo() method to search for the child with the matching "url" value
                Query query = casesRef.orderByChild("url").equalTo(url);

                // Add a listener to check if there is a matching child
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If a matching child is found, delete it
                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                childSnapshot.getRef().removeValue();
                            }
                            Intent intent = new Intent (caseDetailsAdmin.this, adminCases.class);
                            startActivity(intent);
                        } else {
                            // No matching child found, display a message to the user
                            Toast.makeText(getApplicationContext(), "No matching child found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

    }
}
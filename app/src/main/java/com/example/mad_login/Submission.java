package com.example.mad_login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Submission extends Fragment {
    private TextView textNoSubmissionCases;

    private RecyclerView recyclerView;
    private List<CaseModel> submissionList;
    private SubmissionAdapter submissionAdapter;
    private DatabaseReference casesRef;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    private String userID = currentUser.getUid();



    public Submission() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submission, container, false);
        textNoSubmissionCases = view.findViewById(R.id.textNoSubmissionCases);


        recyclerView = view.findViewById(R.id.submissionRV);
        submissionList = new ArrayList<>();
        submissionAdapter = new SubmissionAdapter(submissionList, new SubmissionAdapter.OnItemClickListener() {
            public void onItemClick(String caseID,String caseType) {
                fetchDetails(caseID,caseType);
            }
        });

        //set adapter to recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(submissionAdapter);

        // Initialize Firebase Database references
        casesRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userID).child("Cases");

        // Retrieve cases for the current user
        retrieveSubmissions();

        return view;
    }


    private void retrieveSubmissions() {
        final boolean[] hasSubmssiobCases = {false};

        casesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                submissionList.clear();

                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
                    String caseID = caseSnapshot.getKey();

                    if (!caseSnapshot.hasChild("SentRequest")) {

                        String caseDescription = caseSnapshot.child("caseDescription").getValue(String.class);
                        String caseName = caseSnapshot.child("caseName").getValue(String.class);
                        String caseType = caseSnapshot.child("caseType").getValue(String.class);

                        CaseModel caseModel = new CaseModel();
                        caseModel.setCaseID(caseID);
                        caseModel.setCaseDescription(caseDescription);
                        caseModel.setCaseName(caseName);
                        caseModel.setCaseType(caseType);

                        submissionList.add(caseModel);
                        hasSubmssiobCases[0] = true;
                    }

                }

                submissionAdapter.notifyDataSetChanged();
                if (!hasSubmssiobCases[0]) {
                    textNoSubmissionCases.setVisibility(View.VISIBLE);
                } else {
                    // Hide the message if there are pending cases
                    textNoSubmissionCases.setVisibility(View.GONE);
                }

            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private  void  fetchDetails(String caseID,String caseType){
        Intent intent = new Intent(getActivity(), MainActivityLawyer.class);
        intent.putExtra("caseID", caseID);
        intent.putExtra("caseType",caseType);
        startActivity(intent);
    }
}



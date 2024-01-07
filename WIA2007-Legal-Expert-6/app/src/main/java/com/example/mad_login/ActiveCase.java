package com.example.mad_login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ActiveCase extends Fragment {
    private TextView textNoActiveCases;


    private RecyclerView recyclerView;
    private List<CaseModel> caseList;
    private ActiveCaseAdapter activeCaseAdapter;
    private DatabaseReference casesRef;
    private DatabaseReference lawyerRef;
    private FirebaseAuth mAuth;
    private String senderUserID;



    public ActiveCase() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         mAuth = FirebaseAuth.getInstance();
         senderUserID = mAuth.getCurrentUser().getUid();


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_active_case, container, false);
        textNoActiveCases = view.findViewById(R.id.textNoActiveCases);


        recyclerView = view.findViewById(R.id.activeCaseRV);
        caseList = new ArrayList<>();
        activeCaseAdapter = new ActiveCaseAdapter(caseList, new ActiveCaseAdapter.OnItemClickListener() {
            public void onItemClick(String caseId,String receiverId,String status,String caseType,String rejectionReason,String caseName) {
                fetchLawyerDetails(caseId,receiverId,status,caseType,rejectionReason,caseName);
            }
        });


        // Initialize Firebase Database references
        casesRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(senderUserID).child("Cases");
        lawyerRef = FirebaseDatabase.getInstance().getReference("Registered Lawyers");

        //set adapter to recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(activeCaseAdapter);
        // Retrieve cases for the current user
        retrieveCases();

        return view;
    }

    private void retrieveCases() {
        final boolean[] hasActiveCases = {false};

        casesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                caseList.clear();

                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
                    String caseId = caseSnapshot.getKey();
                    CaseModel caseModel = caseSnapshot.getValue(CaseModel.class);
                    if (caseModel != null) {
                        // Iterate through SentRequest node
                        for (DataSnapshot requestSnapshot : caseSnapshot.child("SentRequest").getChildren()) {
                            String receiverId = requestSnapshot.getKey();
                            String status = requestSnapshot.child("status").getValue(String.class);

                            if ("Initiate".equals(status) || "OnGoing".equals(status)||"OnHold".equals(status)) {
                                // Fetch additional details from the current case
                                String caseDescription = caseSnapshot.child("caseDescription").getValue(String.class);
                                String caseName = caseSnapshot.child("caseName").getValue(String.class);
                                String caseType = caseSnapshot.child("caseType").getValue(String.class);


                                CaseModel caseModelWithDetails = new CaseModel();
                                caseModelWithDetails.setCaseID(caseId);
                                caseModelWithDetails.setCaseDescription(caseDescription);
                                caseModelWithDetails.setCaseName(caseName);
                                caseModelWithDetails.setCaseType(caseType);
                                caseModelWithDetails.setReceiverId(receiverId);
                                caseModelWithDetails.setStatus(status);
                                caseModelWithDetails.setCurrentUserId(senderUserID);


                                // Add the case model to the list
                                caseList.add(caseModelWithDetails);
                                hasActiveCases[0] = true;

                            }
                        }
                    }
                }


                if (!hasActiveCases[0]) {
                    textNoActiveCases.setVisibility(View.VISIBLE);
                } else {
                    // Hide the message if there are pending cases
                    textNoActiveCases.setVisibility(View.GONE);
                }
                // Notify the adapter about the data change
                activeCaseAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void fetchLawyerDetails(String caseId,String receiverId,String status,String caseType,String rejectionReason,String caseName) {
        lawyerRef.child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot lawyerSnapshot) {
                if(lawyerSnapshot.exists()) {
                    String doB = lawyerSnapshot.child("doB").getValue(String.class);
                    String email = lawyerSnapshot.child("email").getValue(String.class);
                    String expYear = lawyerSnapshot.child("expYear").getValue(String.class);
                    String gender = lawyerSnapshot.child("gender").getValue(String.class);
                    String language = lawyerSnapshot.child("language").getValue(String.class);
                    String lawFirm = lawyerSnapshot.child("lawFirm").getValue(String.class);
                    String mobile = lawyerSnapshot.child("mobile").getValue(String.class);
                    String name = lawyerSnapshot.child("name").getValue(String.class);
                    String qualification = lawyerSnapshot.child("qualification").getValue(String.class);
                    String specialization = lawyerSnapshot.child("specialization").getValue(String.class);
                    String state = lawyerSnapshot.child("state").getValue(String.class);




                        Intent intent = new Intent(getActivity(), AcceptedLawyerDetails.class);

                        intent.putExtra("doB", doB);
                        intent.putExtra("email", email);
                        intent.putExtra("expYear", expYear);
                        intent.putExtra("gender", gender);
                        intent.putExtra("language", language);
                        intent.putExtra("lawFirm", lawFirm);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("name", name);
                        intent.putExtra("qualification", qualification);
                        intent.putExtra("specialization", specialization);
                        intent.putExtra("state", state);
                        intent.putExtra("receiverId", receiverId);
                        intent.putExtra("caseId", caseId);
                        intent.putExtra("status", status);
                        intent.putExtra("caseName",caseName);

                        startActivity(intent);}

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching lawyer details: " + error.getMessage());
            }
        });
    }
}
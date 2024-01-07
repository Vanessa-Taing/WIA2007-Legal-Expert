package com.example.mad_login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class lawyerAcceptedCase extends Fragment {
    private TextView textNoAcceptedCases;
    private RecyclerView recyclerView;
    private lawyerAcceptedAdapter adapter;

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;

    private List<client_CaseModel> acceptedCaseList;
    private static DatabaseReference lawyerRequestRef;

    public lawyerAcceptedCase() {
    }


    //    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lawyer_accepted_case, container, false);
        textNoAcceptedCases = view.findViewById(R.id.textNoAcceptedCases);

        // Initialize FirebaseAuth instance
        authProfile = FirebaseAuth.getInstance();
        // Get the current user from FirebaseAuth
        firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser != null) {
            String currentLawyerID = firebaseUser.getUid();
            Log.d("Authentication", "User ID in pending case: " + currentLawyerID);

            recyclerView = view.findViewById(R.id.RVpendingCase);
            acceptedCaseList = new ArrayList<>();
            adapter = new lawyerAcceptedAdapter(acceptedCaseList,currentLawyerID);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

            lawyerRequestRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(currentLawyerID).child("ReceivedRequests");

            // Retrieve received requests for the current lawyer
            retrieveReceivedRequests();
        } else {
            Log.d("Authentication", "No user is currently signed in");
        }

        return view;
    }

    private void retrieveReceivedRequests() {
        final boolean[] hasPendingCases = {false};

        lawyerRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                acceptedCaseList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot senderSnapshot : dataSnapshot.getChildren()) {
                        String senderID = senderSnapshot.getKey();
                        Log.d("retrieveReceivedRequest", "sender ID in pending case: " + senderID);

                        for (DataSnapshot caseSnapshot : senderSnapshot.getChildren()) {

                            String caseID = caseSnapshot.getKey();
                            String status = caseSnapshot.child("status").getValue(String.class);

                            // Log caseID and senderID for debugging
                            Log.d("retrieveReceivedRequest", "caseID: " + caseID);
                            Log.d("retrieveReceivedRequest", "status: " + status);

                            // Check if the status is "sent"
                            if ("accepted".equals(status)) {
                                // Fetch case details from "Registered Users" node
                                fetchCaseDetails(senderID, caseID,status);
                                hasPendingCases[0] = true;
                            }
                        }
                    }
                }
                if (!hasPendingCases[0]) {
                    textNoAcceptedCases.setVisibility(View.VISIBLE);
                } else {
                    // Hide the message if there are pending cases
                    textNoAcceptedCases.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchCaseDetails(String senderID, String caseID,String status) {
        DatabaseReference casesRef = FirebaseDatabase.getInstance().getReference("Registered Users")
                .child(senderID);


        casesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve user details
                String clientPhone = dataSnapshot.child("mobile").getValue(String.class);
                String clientGender = dataSnapshot.child("gender").getValue(String.class);
                String clientName = dataSnapshot.child("name").getValue(String.class);
                String clientState = dataSnapshot.child("state").getValue(String.class);
                Log.d("SenderData", "Received clientName in pending case: " + clientName);

                DataSnapshot casesSnapshot = dataSnapshot.child("Cases").child(caseID);
                Log.d("DataSnapshot", dataSnapshot.toString());

                if (casesSnapshot.exists()) {
                    String caseDescription = casesSnapshot.child("caseDescription").getValue(String.class);
                    String caseName = casesSnapshot.child("caseName").getValue(String.class);
                    String caseType = casesSnapshot.child("caseType").getValue(String.class);
                    Log.d("CaseData", "Received caseType in pending case: " + caseType);

                    // Create a client_CaseModel object and set the values
                    client_CaseModel caseModel = new client_CaseModel();
                    caseModel.setCaseDescription(caseDescription);
                    caseModel.setCaseName(caseName);
                    caseModel.setCaseType(caseType);
                    caseModel.setClientName(clientName);
                    caseModel.setClientState(clientState);
                    caseModel.setClientGender(clientGender);
                    caseModel.setClientPhone(clientPhone);
                    caseModel.setCaseID(caseID);
                    caseModel.setClientID(senderID);
                    caseModel.setStatus(status);

                    acceptedCaseList.add(caseModel);

                    adapter.notifyDataSetChanged();

                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    //dialog for update case

}
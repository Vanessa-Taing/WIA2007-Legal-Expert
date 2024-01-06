package com.example.mad_login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Submission extends Fragment {

    caseAdapter caseadapter;

    RecyclerView recyclerView;



    public Submission() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_submission, container, false);
        recyclerView = view.findViewById(R.id.submissionRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Assuming you are using Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser != null) {
            String userId = currentUser.getUid();
            //set up firebaseRecyclerOptions
            FirebaseRecyclerOptions<CaseModel4> options = new FirebaseRecyclerOptions.Builder<CaseModel4>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users").child(userId).child("Cases"), CaseModel4.class)
                    .build();
            //initialize caseAdapter
            caseadapter = new caseAdapter(options,getContext());


            //set adapter to recyclerview
            recyclerView.setAdapter(caseadapter);

        }
        return view ;
    }


    public void onStart() {
        super.onStart();
        caseadapter.startListening();
    }

    public void onStop() {
        super.onStop();
        caseadapter.stopListening();
    }



}
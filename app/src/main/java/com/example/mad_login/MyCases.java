package com.example.mad_login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCases extends Fragment {
    private static final String ARG_PARAM1 = "param1";

  private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyCases() {
        // Required empty public constructor
    }

    public static MyCases newInstance(String param1, String param2) {
        MyCases fragment = new MyCases();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cases, container, false);

        ViewPager innerViewPager = view.findViewById(R.id.innerViewPager);
        TabLayout innerTabLayout = view.findViewById(R.id.innerTabLayout);

        InnerPagerAdapter innerPagerAdapter = new InnerPagerAdapter(getChildFragmentManager());

        Submission submissionFragment = new Submission();
        Request requestFragment = new Request();
        ActiveCase activeCaseFragment = new ActiveCase();
        Termination terminationFragment = new Termination();
        CompleteCase completeCaseFragment = new CompleteCase();

        innerPagerAdapter.addFragment(submissionFragment);
        innerPagerAdapter.addFragment(requestFragment);
        innerPagerAdapter.addFragment(activeCaseFragment);
        innerPagerAdapter.addFragment(completeCaseFragment);
        innerPagerAdapter.addFragment(terminationFragment);

        innerViewPager.setAdapter(innerPagerAdapter);
        innerTabLayout.setupWithViewPager(innerViewPager);

//        String senderUserID = "6s7vuO60iiQ86DEWTbnfxulHEU52";
//
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference casesRef = rootRef.child("Registered Users").child(senderUserID).child("Cases");
//
//        casesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<String> submissionCases = new ArrayList<>();
//                ArrayList<String> requestCases = new ArrayList<>();
//                ArrayList<String> terminationCases = new ArrayList<>();
//
//                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
//                    String caseId = caseSnapshot.getKey();
//
//                    if (caseSnapshot.hasChild("sentRequest")) {
//                        for (DataSnapshot receiverSnapshot : caseSnapshot.child("sentRequest").getChildren()) {
//                            String receiverID = receiverSnapshot.getKey();
//
//                            if (receiverSnapshot.hasChild("status")) {
//                                String status = receiverSnapshot.child("status").getValue(String.class);
//
//                                if ("sent".equals(status) || "accept".equals(status) || "rejected".equals(status)) {
//                                    requestCases.add(caseId);
//                                } else if ("terminate".equals(status)) {
//                                    terminationCases.add(caseId);
//                                } else {
//                                    submissionCases.add(caseId);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                Submission submissionFragment = (Submission) innerPagerAdapter.getItem(0);
//             //   submissionFragment.displaySubmissionCases(submissionCases);
//
//                Request requestFragment = (Request) innerPagerAdapter.getItem(1);
//               requestFragment.displayRequestCases(requestCases);
//
//               Termination terminationFragment = (Termination) innerPagerAdapter.getItem(2);
//              //  terminationFragment.displayTerminationCases(terminationCases);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("MyCases", "Error retrieving cases: " + error.getMessage());
//            }
//        });
//
      return view;
//    }
    }
}
package com.example.mad_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class lawyerRequestStatus extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_request_status);

        tabLayout = findViewById(R.id.tabLayoutCaseStatus);
        viewPager = findViewById(R.id.viewPagerCaseStatus);


        Adapter_CaseStatusFragment adapter = new Adapter_CaseStatusFragment(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


     adapter.addFragment(new lawyerPendingCase(), "Pending");
      adapter.addFragment(new lawyerAcceptedCase(), "Accepted");
      adapter.addFragment(new lawyerRejectedCase(), "Rejected");
        adapter.addFragment(new lawyerActiveCase(),"Active Case");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
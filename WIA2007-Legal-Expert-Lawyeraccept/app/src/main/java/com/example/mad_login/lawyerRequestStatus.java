package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mad_login.lawyerAppointment.lawyerAppointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        adapter.addFragment(new lawyerActiveCase(),"Active");
        adapter.addFragment(new lawyerCompleteCase(),"Completed");
        adapter.addFragment(new lawyerTerminateCase(), "Terminate");


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
                if (item.getItemId() == R.id.lawyermenu_request) {
                    // Navigate to lawyerRequestStatus activity
                    startActivity(new Intent(lawyerRequestStatus.this, lawyerRequestStatus.class));
                    return true;
                }
                else if (item.getItemId()==R.id.lawyermenu_profile) {
                    startActivity(new Intent(lawyerRequestStatus.this, lawyerProfile.class));
                    return true ;
                }
                else if (item.getItemId()==R.id.lawyermenu_chat) {
                    startActivity(new Intent(lawyerRequestStatus.this, lawyer_ChatActivity.class));
                    return true ;
                }
                else if (item.getItemId()==R.id.lawyermenu_appointment) {
                    startActivity(new Intent(lawyerRequestStatus.this, lawyerAppointment.class));
                    return true ;
                }
                else if (item.getItemId()==R.id.lawyermenu_home){
                    startActivity(new Intent(lawyerRequestStatus.this, lawyerProfile.class));
                    return true ;
                }
                else {
                    // Handle other menu items here if needed
                    return false;
                }
            }
        });


    }
}
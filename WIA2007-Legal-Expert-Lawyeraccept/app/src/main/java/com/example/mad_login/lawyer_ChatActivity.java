package com.example.mad_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mad_login.Fragments.AllChatsFragment;
import com.example.mad_login.Fragments.ClientsFragment;
import com.example.mad_login.lawyerAppointment.lawyerAppointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class lawyer_ChatActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_chat);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
                if (item.getItemId() == R.id.lawyermenu_request) {
                    // Navigate to lawyerRequestStatus activity
                    startActivity(new Intent(lawyer_ChatActivity.this, lawyerRequestStatus.class));
                    return true;
                }
                else if (item.getItemId()==R.id.lawyermenu_profile) {
                    startActivity(new Intent(lawyer_ChatActivity.this, lawyerProfile.class));
                    return true ;
                }
                else if (item.getItemId()==R.id.lawyermenu_chat) {
                    startActivity(new Intent(lawyer_ChatActivity.this, lawyer_ChatActivity.class));
                    return true ;
                }
                else if (item.getItemId()==R.id.lawyermenu_appointment) {
                    startActivity(new Intent(lawyer_ChatActivity.this, lawyerAppointment.class));
                    return true ;
                }
                else if (item.getItemId()==R.id.lawyermenu_home){
                    startActivity(new Intent(lawyer_ChatActivity.this, lawyerProfile.class));
                    return true ;
                }
                else {
                    // Handle other menu items here if needed
                    return false;
                }
            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Adding Fragments to the adapter
        viewPagerAdapter.addFragment(new AllChatsFragment(), "All Chats");
        viewPagerAdapter.addFragment(new ClientsFragment(), "Clients");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }


    }


}


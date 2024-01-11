package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("My Cases");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ViewPager viewPager  = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        Adapter1 adapter = new Adapter1(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //connect tablayout and viewpager
        tabLayout.setupWithViewPager(viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_cases).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.menu_chat).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_lawyer).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    startActivity(new Intent(getApplicationContext(), documentedCase.class));
                    return true;
                } else if (itemId == R.id.menu_cases) {
                    startActivity(new Intent(getApplicationContext(), MainActivity4.class));
                    return true;
                }  else if (itemId == R.id.menu_lawyer) {
                    startActivity(new Intent(getApplicationContext(), LawyerListActivity.class));
                    return true;
                } else if (itemId == R.id.menu_profile) {
                    startActivity(new Intent(getApplicationContext(), userProfile.class));
                    return true;
                }else if (itemId == R.id.menu_chat) {
                    startActivity(new Intent(getApplicationContext(), User_ChatActivity.class));
                    return true;
                }
                return false;
            }
        });


    }
}
package com.example.mad_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mad_login.Fragments.UserChatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class User_ChatActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        ViewPager viewPager = findViewById(R.id.view_pager);
        UserChatPagerAdapter pagerAdapter = new UserChatPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_cases).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_chat).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.menu_lawyer).setChecked(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
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


    private static class UserChatPagerAdapter extends FragmentPagerAdapter {
        public UserChatPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new UserChatFragment();
        }

        @Override
        public int getCount() {
            return 1;  // Since there is only one fragment
        }
    }
}
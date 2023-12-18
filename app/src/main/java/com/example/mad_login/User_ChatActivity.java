package com.example.mad_login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mad_login.Adapter.UserAdapter;
import com.example.mad_login.Fragments.AllChatsFragment;
import com.example.mad_login.Fragments.ClientsFragment;
import com.example.mad_login.Fragments.UserChatFragment;
import com.google.android.material.tabs.TabLayout;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class User_ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        ViewPager viewPager = findViewById(R.id.view_pager);
        UserChatPagerAdapter pagerAdapter = new UserChatPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);


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

package com.example.legalexpert_casestatus2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class InnerPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();


    public InnerPagerAdapter(@NonNull FragmentManager fm)
        {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

    public void addFragment(Fragment fragment) {

        fragments.add(fragment);
    }


        public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {

        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Submission";
            case 1:
                return "Request";
            case 2 :
                return "Active";
            case 3 :
                return "Complete";
            case 4:
                return "Terminate";

            default:
                return null;
        }
    }

    }






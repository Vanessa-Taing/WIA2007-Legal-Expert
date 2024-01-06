package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class InnerPagerAdapter extends FragmentPagerAdapter {
    private List<CaseModel4> caseList;
    public InnerPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new Submission();
            case 1 :
                return new Request();
            case 2 :
                return new OnGoing();
            case 3 :
                return new Resolution();
            case 4 :
                return new Closed();
            case 5 :
                return new Termination();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {

        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "Submission";
            case 1 :
                return "Request";
            case 2 :
                return "OnGoing";
            case 3 :
                return "Resolution";
            case 4 :
                return "Closed";
            case 5 :
                return "Termination";
            default:
                return null;

        }
    }

}

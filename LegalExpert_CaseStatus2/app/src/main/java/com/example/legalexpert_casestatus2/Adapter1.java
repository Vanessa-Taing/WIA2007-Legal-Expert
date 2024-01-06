package com.example.legalexpert_casestatus2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Adapter1 extends FragmentPagerAdapter {

    public Adapter1(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MyCases();
            case 1 :
                return new UploadCase();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2; //number of tabs
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "My Cases";
            case 1 :
                return "Upload a Case";
            default:
                return null ;
        }
    }
}

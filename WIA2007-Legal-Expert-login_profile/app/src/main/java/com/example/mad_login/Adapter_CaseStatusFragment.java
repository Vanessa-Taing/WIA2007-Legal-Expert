package com.example.mad_login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Adapter_CaseStatusFragment extends FragmentPagerAdapter {

    private final ArrayList<Fragment>fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTittle = new ArrayList<>();
    public Adapter_CaseStatusFragment(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

       return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment,String tittle){
        fragmentArrayList.add(fragment);
        fragmentTittle.add(tittle);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTittle.get(position);
    }
}

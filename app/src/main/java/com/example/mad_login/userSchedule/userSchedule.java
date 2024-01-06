package com.example.mad_login.userSchedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mad_login.R;
import com.example.mad_login.lawyerAppointment.lawyerPastAppointment;
import com.example.mad_login.lawyerAppointment.lawyerUpcomingAppointment;
import com.example.mad_login.lawyerProfile;
import com.example.mad_login.userProfile;

public class userSchedule extends AppCompatActivity  implements View.OnClickListener {

    ColorStateList def ;
    TextView item1, item2,select ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.my_primary)));
        setTitle("Schedule");

        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);


        item1.setOnClickListener(this);
        item2.setOnClickListener(this);

        select = findViewById(R.id.select);
        def = item2.getTextColors();

        loadFragment(new userUpcomingAppointment());
    }
    public void onClick(View view){
        if(view.getId()==R.id.item1){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            loadFragment(new userUpcomingAppointment());
        }
        else if(view.getId()==R.id.item2){
            item1.setTextColor(def);
            item2.setTextColor(Color.WHITE);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            loadFragment(new userPastAppointment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment); // R.id.fragmentContainer is the container in your activity layout where the fragment will be placed
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), userProfile.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    }

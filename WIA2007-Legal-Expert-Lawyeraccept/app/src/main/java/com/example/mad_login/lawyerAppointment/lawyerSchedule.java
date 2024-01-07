package com.example.mad_login.lawyerAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mad_login.R;
import com.example.mad_login.lawyerProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class lawyerSchedule extends AppCompatActivity implements View.OnClickListener {

    ColorStateList def ;
    TextView item1, item2,select ;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_schedule);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.my_primary)));
        setTitle("Schedule");

        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);


        item1.setOnClickListener(this);
        item2.setOnClickListener(this);

       select = findViewById(R.id.select);
       def = item2.getTextColors();

        loadFragment(new lawyerUpcomingAppointment());
    }
    public void onClick(View view){
        if(view.getId()==R.id.item1){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            loadFragment(new lawyerUpcomingAppointment());
        }
        else if(view.getId()==R.id.item2){
            item1.setTextColor(def);
            item2.setTextColor(Color.WHITE);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            loadFragment(new lawyerPastAppointment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment); // R.id.fragmentContainer is the container in your activity layout where the fragment will be placed
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), lawyerProfile.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
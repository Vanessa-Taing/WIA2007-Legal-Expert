package com.example.mad_login.lawyerAppointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mad_login.R;
import com.example.mad_login.lawyerProfile;
import com.example.mad_login.lawyerRequestStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class lawyerAppointment extends AppCompatActivity {

    private TextView textNoAppointment;


    private RecyclerView recyclerView;
    private lawyerAppointmentAdapter adapter;
    private List<lawyerAppointmentModel> appointmentsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_appointment);
        textNoAppointment = findViewById(R.id.textNoAppointment);

        // Initialize RecyclerView and the list of appointments
        recyclerView = findViewById(R.id.RVappointment);
        appointmentsList = new ArrayList<>();
        adapter = new lawyerAppointmentAdapter(appointmentsList);

        // Set up RecyclerView with LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        retrieveAppointmentsFromFirebase();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.my_primary)));
        setTitle("Appointments");

    }

    private void retrieveAppointmentsFromFirebase() {
        final boolean[] hasAppointment = {false};
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments");

        if (currentUser != null) {
            String currentLawyerId = currentUser.getUid();
            appointmentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appointmentsList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String appointmentId = snapshot.getKey();
                        lawyerAppointmentModel appointment = snapshot.getValue(lawyerAppointmentModel.class);

                        // Check if the appointment belongs to the current user (lawyer)
                        if (appointment != null && currentLawyerId.equals(appointment.getLawyerId())&&appointment.getStatus().equals("pending")) {

                            appointment.setAppointmentId(appointmentId);
                            appointmentsList.add(appointment);
                            hasAppointment[0] = true;
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if (!hasAppointment[0]) {
                        textNoAppointment.setVisibility(View.VISIBLE);
                    } else {
                        // Hide the message if there are pending cases
                        textNoAppointment.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }

}

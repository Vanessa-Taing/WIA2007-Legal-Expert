package com.example.mad_login.userAppointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.R;
import com.example.mad_login.userAppointment.userAppointmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class userUpcomingAppointment extends Fragment {


    private TextView textNoUpcoming;

    private RecyclerView recyclerView;
    private userUpcomingAdapter adapter;
    private List<userAppointmentModel> upComingList;


    public userUpcomingAppointment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_upcoming_appointment, container, false);

        textNoUpcoming = view.findViewById(R.id.textNoUpcoming);
        recyclerView = view.findViewById(R.id.RVupcoming);
        upComingList = new ArrayList<>();
        adapter = new userUpcomingAdapter(upComingList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        retrieveUpcomingAppointmentsFromFirebase();
        return view ;
    }

    private void retrieveUpcomingAppointmentsFromFirebase() {
        final boolean[] hasUpComing = {false};
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments");

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            appointmentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    upComingList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String appointmentId = snapshot.getKey();
                        userAppointmentModel appointment = snapshot.getValue(userAppointmentModel.class);

                        if (appointment != null && currentUserId.equals(appointment.getUserId()) && appointment.getStatus().equals("Accepted")) {
                            if (isDateAfterCurrentDate(appointment.getDate())) {
                                appointment.setAppointmentId(appointmentId);
                                upComingList.add(appointment);
                                hasUpComing[0] = true;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if (!hasUpComing[0]) {
                        textNoUpcoming.setVisibility(View.VISIBLE);
                    } else {
                        textNoUpcoming.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }

    private boolean isDateAfterCurrentDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date currentDate = new Date();
            Date appointmentDate = sdf.parse(date);
            return currentDate.before(appointmentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
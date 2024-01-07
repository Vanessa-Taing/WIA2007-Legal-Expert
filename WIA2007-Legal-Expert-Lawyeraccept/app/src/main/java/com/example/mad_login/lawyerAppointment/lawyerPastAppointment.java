package com.example.mad_login.lawyerAppointment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mad_login.R;
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


public class lawyerPastAppointment extends Fragment {

    private TextView textNoPast;
    private RecyclerView recyclerView;
    private lawyerPastAdapter adapter;
    private List<lawyerAppointmentModel> pastList;
    private List<lawyerAppointmentModel> pastAppointmentsList;
    public lawyerPastAppointment() {
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
        View view =  inflater.inflate(R.layout.fragment_lawyer_past_appointment, container, false);

        textNoPast = view.findViewById(R.id.textNoPast);
        recyclerView = view.findViewById(R.id.RVpast);
        pastList = new ArrayList<>();
        adapter = new lawyerPastAdapter(pastList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        retrievePastAppointmentsFromFirebase();
        return view ;

    }
    private void retrievePastAppointmentsFromFirebase() {
        final boolean[] hasPast = {false};
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments");

        if (currentUser != null) {
            String currentLawyerId = currentUser.getUid();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            appointmentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pastList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String appointmentId = snapshot.getKey();
                        lawyerAppointmentModel appointment = snapshot.getValue(lawyerAppointmentModel.class);

                        if (appointment != null && currentLawyerId.equals(appointment.getLawyerId()) && appointment.getStatus().equals("Accepted")) {
                            if (isDateBeforeCurrentDate(appointment.getDate())) {
                                appointment.setAppointmentId(appointmentId);
                                pastList.add(appointment);
                                hasPast[0] = true;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if (!hasPast[0]) {
                        textNoPast.setVisibility(View.VISIBLE);
                    } else {
                        textNoPast.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }

    private boolean isDateBeforeCurrentDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date currentDate = new Date();
            Date appointmentDate = sdf.parse(date);
            return appointmentDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}
package com.example.legalexpert_casestatus2.userAppointment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.legalexpert_casestatus2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userAppointment extends AppCompatActivity {

    private Button btnConfirmAppointment;
    private TextView TVappointmentLawyer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment);

        // Initialize UI components
        TVappointmentLawyer = findViewById(R.id.appointmentLawyer);
        btnConfirmAppointment = findViewById(R.id.btnConfirmAppointment);

        String lawyerName = getIntent().getStringExtra("name");

        TVappointmentLawyer.setText("Book appointment with " + lawyerName);

        // Set up click listener for the "Confirm Appointment" button
        btnConfirmAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
        setTitle("Select Date and Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Appointment");
        builder.setMessage("Do you want to confirm this appointment?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bookAppointment();
                Toast.makeText(userAppointment.this, "Appointment Confirmed!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "No", do nothing
            }
        });
        builder.show();
    }

    private void bookAppointment(){
        String lawyerId = getIntent().getStringExtra("lawyerId");
        String userId = getIntent().getStringExtra("senderUserId");
        String caseId = getIntent().getStringExtra("caseId");
        String caseName = getIntent().getStringExtra("caseName");

        if (TextUtils.isEmpty(caseName)|| caseName == null) {
            // CaseName is empty, log the message
            Log.d("Appointment", "Case Name is empty.");
            return; // Exit the function to prevent further execution
        }



        //get selected data from data picker
        DatePicker datePicker = findViewById(R.id.appointmentDatePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Months are 0-based
        int year = datePicker.getYear();
        String date = day + "/" + month + "/" + year;

        // Get the selected time from the TimePicker
        TimePicker timePicker = findViewById(R.id.appointmentTimePicker);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String amPm;
        if (hour >= 12) {
            amPm = "PM";
            if (hour > 12) {
                hour -= 12;
            }
        } else {
            amPm = "AM";
        }

        String time = String.format("%02d:%02d %s", hour, minute, amPm);

        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments");
        // Generate a unique key for the new appointment
        String appointmentId = appointmentsRef.push().getKey();

        // Create an Appointment object with the necessary information
        AppointmentCaseModel appointment = new AppointmentCaseModel(lawyerId, userId, caseId, "pending",date,time,caseName);

        appointmentsRef.child(appointmentId).setValue(appointment);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
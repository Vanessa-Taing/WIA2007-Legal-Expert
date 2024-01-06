package com.example.mad_login.userSchedule;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.R;
import com.example.mad_login.lawyerAppointment.lawyerAppointmentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class userPastAdapter extends RecyclerView.Adapter<userPastAdapter.ViewHolder>{
    private List<lawyerAppointmentModel> pastList;
    private DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers");

    public userPastAdapter(List<lawyerAppointmentModel> pastList) {
        this.pastList = pastList;
    }

    public userPastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_past_item, parent, false);
        return new userPastAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull userPastAdapter.ViewHolder holder, int position) {
        lawyerAppointmentModel appointment = pastList.get(position);

        // Bind data to the ViewHolder
        holder.dateTextView.setText(appointment.getDate());
        holder.timeTextView.setText(appointment.getTime());
        holder.caseNameTextView.setText(appointment.getCaseName());

        getLawyerInfo(appointment.getLawyerId(), holder.clientNameTextView);
    }

    private void getLawyerInfo(String lawyerId, TextView tvClientName) {
        if (lawyerId != null) {
            lawyerRef.child(lawyerId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    if (userSnapshot.exists()) {
                        String userName = userSnapshot.child("name").getValue(String.class);
                        tvClientName.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        } else {
            // The userId is null, handle this case accordingly
            // You might want to log an error or show a message to the user
            Log.e("getLawyerInfo", "lawyerId is null. Unable to retrieve lawyer information.");
        }
    }

    public int getItemCount() {
        return pastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dateTextView;
        public TextView timeTextView;
        public TextView clientNameTextView;
        public TextView caseNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.pastDate);
            timeTextView = itemView.findViewById(R.id.pastTime);
            clientNameTextView = itemView.findViewById(R.id.TVpastName);
            caseNameTextView = itemView.findViewById(R.id.TVpastCaseName);
        }
    }

}

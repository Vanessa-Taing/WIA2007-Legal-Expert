package com.example.mad_login.lawyerAppointment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class lawyerPastAdapter extends RecyclerView.Adapter<lawyerPastAdapter.ViewHolder> {
    private List<lawyerAppointmentModel> pastList;

    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Registered Users");

    public lawyerPastAdapter(List<lawyerAppointmentModel> pastList) {
        this.pastList = pastList;
    }

    @NonNull
    @Override
    public lawyerPastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_past_item, parent, false);
        return new lawyerPastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lawyerPastAdapter.ViewHolder holder, int position) {
        lawyerAppointmentModel appointment = pastList.get(position);

        // Bind data to the ViewHolder
        holder.dateTextView.setText(appointment.getDate());
        holder.timeTextView.setText(appointment.getTime());
        holder.caseNameTextView.setText(appointment.getCaseName());

        getUserInfo(appointment.getUserId(), holder.clientNameTextView);
    }

    private void getUserInfo(String userId, TextView tvClientName) {
        if (userId != null) {
            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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
            Log.e("getUserInfo", "UserId is null. Unable to retrieve user information.");
        }
    }

    @Override
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

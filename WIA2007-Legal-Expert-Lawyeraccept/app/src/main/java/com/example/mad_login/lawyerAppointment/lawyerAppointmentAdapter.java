package com.example.mad_login.lawyerAppointment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class lawyerAppointmentAdapter extends RecyclerView.Adapter<lawyerAppointmentAdapter.ViewHolder> {
    private List<lawyerAppointmentModel> appointmentsList;
    private OnItemClickListener mListener;

    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Registered Users");


    public interface OnItemClickListener {
        void onAcceptClick(int position);

        void onRejectClick(int position);
    }


    public lawyerAppointmentAdapter(List<lawyerAppointmentModel> appointments) {
        this.appointmentsList = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lawyer_booking_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        lawyerAppointmentModel appointment = appointmentsList.get(position);

        holder.tvDate.setText(appointment.getDate());
        holder.tvTime.setText(appointment.getTime());
        holder.caseName.setText("Case Name: " + appointment.getCaseName());
       // holder.tvClientName.setText(appointment.getClientName());

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update appointment status to "Accepted" in Firebase
                showConfirmationDialog(holder.itemView.getContext(), position, appointment.getAppointmentId(), "Accepted");
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update appointment status to "Rejected" in Firebase
                showConfirmationDialog(holder.itemView.getContext(), position, appointment.getAppointmentId(), "Rejected");
            }
        });

        getUserInfo(appointment.getUserId(), holder.tvClientName);


    }

    private void showConfirmationDialog(Context context, int position, String appointmentId, String newStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to " + newStatus.toLowerCase() + " this appointment?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User confirmed, update appointment status
                        updateAppointmentStatus(position, appointmentId, newStatus);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User canceled the action
                    }
                })
                .show();
    }

    private void updateAppointmentStatus(int position,String appointmentId, String newStatus) {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments");
        if (appointmentId != null) {
            appointmentsRef.child(appointmentId).child("status").setValue(newStatus);
        }
        else {
            // The appointmentId is null, handle this case accordingly
            // You might want to log an error or show a message to the user
            Log.e("updateAppointmentStatus", "AppointmentId is null. Unable to update status.");
        }
    }

    //get client name
    private void getUserInfo(String userId, TextView tvClientName) {
        if (userId != null) {
            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    if (userSnapshot.exists()) {
                        String userName = userSnapshot.child("name").getValue(String.class);
                        tvClientName.setText("Client Name: " + userName);
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
        return appointmentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvTime, tvClientName,caseName;
        private Button btnAccept, btnReject;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.TVdateAppointment);
            tvTime = itemView.findViewById(R.id.TVtimeAppointment);
            tvClientName = itemView.findViewById(R.id.TVclientNameAppointment);
            caseName = itemView.findViewById(R.id.TVcaseAppointment);

            btnAccept = itemView.findViewById(R.id.btnAcceptAppointment);
            btnReject = itemView.findViewById(R.id.btnRejectAppointment);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAcceptClick(position);
                        }
                    }
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRejectClick(position);
                        }
                    }
                }
            });
        }
    }
}
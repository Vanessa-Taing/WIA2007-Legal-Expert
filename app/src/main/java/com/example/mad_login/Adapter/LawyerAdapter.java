package com.example.mad_login.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.Model.LawyerInfo;
import com.example.mad_login.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class LawyerAdapter extends FirebaseRecyclerAdapter<LawyerInfo, LawyerAdapter.MyViewHolder> {

    private OnItemClickListener listener;

    public LawyerAdapter(@NonNull FirebaseRecyclerOptions<LawyerInfo> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull LawyerInfo model) {
        holder.name.setText(model.getName());
        holder.rating.setText(model.getRating());
        holder.specialization.setText(model.getSpecialization());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(model,getRef(position).getKey());
                }
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lawyer_item, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, specialization;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.lawyerName);
            specialization = itemView.findViewById(R.id.specialization);
            rating = itemView.findViewById(R.id.lawyerRating);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(LawyerInfo model, String lawyerID);
    }
}

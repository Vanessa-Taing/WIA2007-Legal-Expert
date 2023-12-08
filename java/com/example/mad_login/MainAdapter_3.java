package com.example.mad_login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MainAdapter_3 extends FirebaseRecyclerAdapter<MainModel_3,MainAdapter_3.myViewHolder> { //myViewHolder represents ViewHolder class used for individual items in RecyclerView

    private OnItemClickListener listener; //use to handle item click event in Recycler Viiew

    public MainAdapter_3(@NonNull FirebaseRecyclerOptions<MainModel_3> options, OnItemClickListener listener) {
        super(options);//configuration object of type 'firebaseRecyclerOptions' that specifies how to retrieve and handle data from firebase realtime database
        this.listener = listener ;
    }


    //below method is called for each item in recycler view. it's responsible for binding data from the"main model" object to the views within the "myViewHolder" class for each item
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel_3 model) {
        //handle legal case
        //assign value to all the row
        holder.casetype.setText(model.getCasetype());
        holder.desc.setText(model.getDesc());
        holder.title.setText(model.getKeywords());

        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener. onItemClick(model);
                }
            }
        });

    }
    // Define an interface for item click events
    public interface OnItemClickListener {
        void onItemClick(MainModel_3 model);
    }

    //below method is responsible for creating new view holder, it's called when new item view needs to be created
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_3,parent,false);
        return new myViewHolder(view);
    }

    //below is inner class that set up reference to the views within an item

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView casetype,title,desc;
//kinda like in the oncreate method
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            casetype=(TextView)itemView.findViewById(R.id.casetypetxt);
            title = (TextView) itemView.findViewById(R.id.titletxt);
            desc = (TextView) itemView.findViewById(R.id.desctxt);


        }

    }


}

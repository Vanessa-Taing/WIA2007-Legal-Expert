package com.example.mad_login;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_login.Adapter.LawyerAdapter;
import com.example.mad_login.Model.LawyerInfo;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// View.onCLickListener is more general that handles clicks events for any type of view(UI element)
// MainAdapter.onItemClickListener is more specific and used in context of recyclerView
public class LawyerListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LawyerAdapter lawyerAdapter;

    private Set<String> selectedFilters = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_list);

        Button buttonCivil = findViewById(R.id.button_civil);
        Button buttonConsumer = findViewById(R.id.button_consumer);
        Button buttonContract = findViewById(R.id.button_contract);
        Button buttonCriminal = findViewById(R.id.button_criminal);
        Button buttonIslamic = findViewById(R.id.button_islamic);
        Button buttonFamily = findViewById(R.id.button_family);


        // Change the background color & text of the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Find Lawyers");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.my_primary)));
        }

        //recycler view setup
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //filter option handling (set up what will happen when click the button)
        buttonCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilter("Civil", buttonCivil);
            }
        });
        buttonConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilter("Consumer", buttonConsumer);
            }
        });

        buttonContract.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                toggleFilter("Contract",buttonContract);
            }
        });
        buttonCriminal.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                toggleFilter("Criminal",buttonCriminal);
            }
        } );
        buttonFamily.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                toggleFilter("Family",buttonFamily);
            }
        } );
        buttonIslamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFilter("Islamic", buttonIslamic);
            }
        });




        //firebaseRecycler adapter set up
        FirebaseRecyclerOptions<LawyerInfo> options =
                new FirebaseRecyclerOptions.Builder<LawyerInfo>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers"), LawyerInfo.class)
                        .build();

        lawyerAdapter = new LawyerAdapter(options, new LawyerAdapter.OnItemClickListener() {
            public void onItemClick(LawyerInfo model, String lawyerID) {
                Intent intent = new Intent(LawyerListActivity.this, LawyerDetailsActivity.class);
                intent.putExtra("uid", lawyerID);
                intent.putExtra("name", model.getName());
                intent.putExtra("imageUrl", model.getImageUrl());
                intent.putExtra("lawfirm", model.getLawFirm());
                intent.putExtra("DOB", model.getDoB());
                intent.putExtra("expYear", model.getExpYear());
                intent.putExtra("specialization", model.getSpecialization());
                intent.putExtra("gender", model.getGender());
                intent.putExtra("language", model.getLanguage());
                intent.putExtra("state", model.getState());
                intent.putExtra("mobile", model.getMobile());
                intent.putExtra("email", model.getEmail());
                intent.putExtra("rating", model.getRating());
                intent.putExtra("lawyerInfo", model);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(lawyerAdapter);

        //recyclerView.setLayoutManager is like telling someone how to arrange the item
        //recyclerView.setAdapter is like actually put the item to there and let him arrange
    }



    //handle search
    //onCreateOptionMenu is like a method when we want to add smtg special in the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu); //create search and put it in the menu
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Name");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        String lowercaseQuery = str.toLowerCase(); // Convert user input to lowercase
        FirebaseRecyclerOptions<LawyerInfo> options =
                new FirebaseRecyclerOptions.Builder<LawyerInfo>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").orderByChild("name").startAt(lowercaseQuery).endAt(str + "\uf8ff"), LawyerInfo.class)
                        .build();

        lawyerAdapter = new LawyerAdapter(options, new LawyerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LawyerInfo model, String lawyerID) {
                //so intent is like a magical messenger bird that helps you move from one room to another (in your app) (so it takes 2 parameters)
                Intent intent = new Intent(LawyerListActivity.this, LawyerDetailsActivity.class);
                intent.putExtra("uid", lawyerID);
                intent.putExtra("name", model.getName());
                intent.putExtra("lawfirm", model.getLawFirm());
                intent.putExtra("DOB", model.getDoB());
                intent.putExtra("expYear", model.getExpYear());
                intent.putExtra("specialization", model.getSpecialization());
                intent.putExtra("gender", model.getGender());
                intent.putExtra("language", model.getLanguage());
                intent.putExtra("state", model.getState());
                intent.putExtra("mobile", model.getMobile());
                intent.putExtra("email", model.getEmail());
                intent.putExtra("rating", model.getRating());
                startActivity(intent); //release the magical messenger bird
            }


        });
        recyclerView.setAdapter(lawyerAdapter);
        lawyerAdapter.startListening();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            onBackPressed();
            return true;

        } else if (item.getItemId() == R.id.search) {
            // Handle the search action here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //filter option
    //toggle filter (event that happen when filter option being clicked)
    //contextCompat is a class in android (specifically part of the androidX library) introduce by google
    //contextCompat provide methods to do certain things in a way that works for all Android devices
    private void toggleFilter(String filter, Button button) {
        if (selectedFilters.contains(filter)) {
            selectedFilters.remove(filter);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.my_primary));
        } else {
            selectedFilters.add(filter);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_purple));
        }
        Log.d("Filter", "Toggling filter: " + filter);

        applyFilters();
    }


    private void applyFilters() {
        FirebaseRecyclerOptions<LawyerInfo> options;

        if (selectedFilters.isEmpty()) {
            // Query to get all lawyers when no filters are selected
            options = new FirebaseRecyclerOptions.Builder<LawyerInfo>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers"), LawyerInfo.class)
                    .build();
        } else {
            // Construct a query based on selected filters
            List<String> selectedFilterList = new ArrayList<>(selectedFilters);

            // Construct the query for filtering by specialization
            options = new FirebaseRecyclerOptions.Builder<LawyerInfo>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers")
                            .orderByChild("specialization").startAt(selectedFilterList.get(0))
                            .endAt(selectedFilterList.get(selectedFilterList.size() - 1) + "\uf8ff"), LawyerInfo.class)
                    .build();
        }


        //after the item is listed base on what user apply, then related item will display
        //MainAdapter.ObItemClickListener() to to define what happen if the item in recyclerView is clicked

        lawyerAdapter = new LawyerAdapter(options, new LawyerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LawyerInfo model, String lawyerID) {
                Intent intent = new Intent(LawyerListActivity.this, LawyerDetailsActivity.class);
                intent.putExtra("uid", lawyerID);
                intent.putExtra("name", model.getName());
                intent.putExtra("lawfirm", model.getLawFirm());
                intent.putExtra("DOB", model.getDoB());
                intent.putExtra("expYear", model.getExpYear());
                intent.putExtra("specialization", model.getSpecialization());
                intent.putExtra("gender", model.getGender());
                intent.putExtra("language", model.getLanguage());
                intent.putExtra("state", model.getState());
                intent.putExtra("mobile", model.getMobile());
                intent.putExtra("email", model.getEmail());
                intent.putExtra("rating", model.getRating());
                startActivity(intent);
            }
        });
        // Set the adapter for the recyclerView
        recyclerView.setAdapter(lawyerAdapter);

        // Start listening for changes
        lawyerAdapter.startListening();
    }

    @Override
    //is call when activity become visible to user
    protected void onStart() {
        super.onStart();
        lawyerAdapter.startListening(); //specific to firebase's recyclerView. tell adapter to start listening to firebase changes and update the UI
    }

    @Override
    //is call when activity no longer visible to user(means it goes into the background)
    protected void onStop() {
        super.onStop();
        lawyerAdapter.stopListening();
    }

}

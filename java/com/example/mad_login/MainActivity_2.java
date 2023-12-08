package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// View.onCLickListener is more general that handles clicks events for any type of view(UI element)
// MainAdapter.onItemClickListener is more specific and used in context of recyclerView
public class MainActivity_2 extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    private BottomNavigationView bottomNavigationView;

    private Set<String> selectedFilters = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        Button buttonCivil = findViewById(R.id.button_civil);
        Button buttonConsumer = findViewById(R.id.button_consumer);
        Button buttonContract = findViewById(R.id.button_contract);
        Button buttonCriminal = findViewById(R.id.button_criminal);
        Button buttonIslamic = findViewById(R.id.button_islamic);
        Button buttonFamily = findViewById(R.id.button_family);


        // Change the background color & text of the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("lawyers");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
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

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_lawyer);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    startActivity(new Intent(getApplicationContext(), userProfile.class));
                    finish();
                    return true;
                } else if (itemId == R.id.menu_cases) {
                    startActivity(new Intent(getApplicationContext(), MainActivity_3.class));
                    finish();
                    return true;
                } else if (itemId == R.id.menu_lawyer) {
                    return true;
                }
                return false;
            }
        });


        //firebaseRecycler adapter set up
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers"), MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options, new MainAdapter.OnItemClickListener() {
            public void onItemClick(MainModel model) {
                Intent intent = new Intent(MainActivity_2.this, lawyerDetails.class);
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

        recyclerView.setAdapter(mainAdapter);

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
        searchView.setQueryHint("search any keywords");


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
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").orderByChild("name").startAt(lowercaseQuery).endAt(str + "\uf8ff"), MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options, new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainModel model) {

                //so intent is like a magical messenger bird that helps you move from one room to another (in your app) (so it takes 2 parameters)
                Intent intent = new Intent(MainActivity_2.this, lawyerDetails.class);
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
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.startListening();

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
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.mainPurple));
        } else {
            selectedFilters.add(filter);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.darkPurple));
        }

        applyFilters();
    }


    private void applyFilters() {
        FirebaseRecyclerOptions<MainModel> options;

        if (selectedFilters.isEmpty()) {
            options = new FirebaseRecyclerOptions.Builder<MainModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers"), MainModel.class)
                    .build();
        } else {
            // Construct a query based on selected filters
            List<String> selectedFilterList = new ArrayList<>(selectedFilters);
            options = new FirebaseRecyclerOptions.Builder<MainModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers")
                            .orderByChild("specialization").startAt(selectedFilterList.get(0))
                            .endAt(selectedFilterList.get(selectedFilterList.size() - 1) + "\uf8ff"), MainModel.class)
                    .build();
        }


        //after the item is listed base on what user apply, then related item will display
        //MainAdapter.ObItemClickListener() to to define what happen if the item in recyclerView is clicked

        mainAdapter = new MainAdapter(options, new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainModel model) {
                Intent intent = new Intent(MainActivity_2.this, lawyerDetails.class);
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
        recyclerView.setAdapter(mainAdapter);// main adapter is like the manager who decide what items to show
       mainAdapter.startListening(); //telling manager to pay attention to changes of data, and update himself
    }

    @Override
    //is call when activity become visible to user
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening(); //specific to firebase's recyclerView. tell adapter to start listening to firebase changes and update the UI
    }

    @Override
    //is call when activity no longer visible to user(means it goes into the background)
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }


}

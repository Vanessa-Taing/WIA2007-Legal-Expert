package com.example.mad_login;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// View.onCLickListener is more general that handles clicks events for any type of view(UI element)
// MainAdapter.onItemClickListener is more specific and used in context of recyclerView
public class adminCases extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterCase adapter_Case;

    private Set<String> selectedFilters = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_main);
        FirebaseApp.initializeApp(this);

        Button buttonCivil = findViewById(R.id.button_civil);
        Button buttonConsumer = findViewById(R.id.button_consumer);
        Button buttonContract = findViewById(R.id.button_contract);
        Button buttonCriminal = findViewById(R.id.button_criminal);
        Button buttonIslamic = findViewById(R.id.button_islamic);
        Button buttonFamily = findViewById(R.id.button_family);
        Button buttonAddCase = findViewById(R.id.btnAddCase);


        // Change the background color & text of the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("cases");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //recycler view setup
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //filter option handling (set up what will happen when click the button)
        buttonCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilter("civil", buttonCivil);
            }
        });
        buttonConsumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilter("consumer", buttonConsumer);
            }
        });

        buttonContract.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                toggleFilter("contract",buttonContract);
            }
        });
        buttonCriminal.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                toggleFilter("criminal",buttonCriminal);
            }
        } );
        buttonFamily.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                toggleFilter("family",buttonFamily);
            }
        } );
        buttonIslamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFilter("islamic", buttonIslamic);
            }
        });




        //firebaseRecycler adapter set up
        FirebaseRecyclerOptions<ModelCases> options =
                new FirebaseRecyclerOptions.Builder<ModelCases>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Cases Code"), ModelCases.class)
                        .build();

        adapter_Case = new AdapterCase(options, new AdapterCase.OnItemClickListener() {
            public void onItemClick(ModelCases model) {
                Intent intent = new Intent(adminCases.this, caseDetailsAdmin.class);
                intent.putExtra("casetype", model.getCasetype());
                intent.putExtra("keywords", model.getKeywords());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("summary", model.getSummary());
                intent.putExtra("url", model.getUrl());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter_Case);

        buttonAddCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCases.this, adminAddCase.class);
                startActivity(intent);
            }

        });

        //recyclerView.setLayoutManager is like telling someone how to arrange the item
        //recyclerView.setAdapter is like actually put the item to there and let him arrange
        /*
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_cases);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    if (AppUtils.getUserType() == "Lawyer"){
                        startActivity(new Intent(getApplicationContext(), lawyerProfile.class));
                        finish();
                        return true;
                    } else if (AppUtils.getUserType() == "User"){
                        startActivity(new Intent(getApplicationContext(), userProfile.class));
                        finish();
                        return true;
                    }
                    return true;
                } else if (itemId == R.id.menu_cases) {
                    return true;
                } else if (itemId == R.id.menu_lawyer) {
                    if (AppUtils.getUserType() == "Lawyer"){
                        startActivity(new Intent(getApplicationContext(), lawyerProfile.class));
                        finish();}
                    else {
                    startActivity(new Intent(getApplicationContext(), MainActivity_2.class));}
                    finish();
                    return true;
                }
                return false;
            }
        });
        */
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Start the new Intent here
        Intent intent = new Intent(this, login_selectRole.class);
        startActivity(intent);

        // Indicate that we've handled the user's interaction with the app icon
        return true;
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
        FirebaseRecyclerOptions<ModelCases> options =
                new FirebaseRecyclerOptions.Builder<ModelCases>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Cases Code").orderByChild("keywords").startAt(lowercaseQuery).endAt(str + "\uf8ff"), ModelCases.class)
                        .build();

        adapter_Case = new AdapterCase(options, new AdapterCase.OnItemClickListener() {
            @Override
            public void onItemClick(ModelCases model) {

                //so intent is like a magical messenger bird that helps you move from one room to another (in your app) (so it takes 2 parameters)
                Intent intent = new Intent(adminCases.this, caseDetailsAdmin.class);
                intent.putExtra("casetype", model.getCasetype());
                intent.putExtra("keywords", model.getKeywords());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("summary", model.getSummary());
                intent.putExtra("url", model.getUrl());
                startActivity(intent); //release the magical messenger bird
            }


        });
        recyclerView.setAdapter(adapter_Case);
        adapter_Case.startListening();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            finish();
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
        FirebaseRecyclerOptions<ModelCases> options;

        if (selectedFilters.isEmpty()) {
            options = new FirebaseRecyclerOptions.Builder<ModelCases>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Cases Code"), ModelCases.class)
                    .build();
        } else {
            // Construct a query based on selected filters
            List<String> selectedFilterList = new ArrayList<>(selectedFilters);
            options = new FirebaseRecyclerOptions.Builder<ModelCases>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Cases Code")
                            .orderByChild("casetype").startAt(selectedFilterList.get(0))
                            .endAt(selectedFilterList.get(selectedFilterList.size() - 1) + "\uf8ff"), ModelCases.class)
                    .build();
        }


        //after the item is listed base on what user apply, then related item will display
        //AdapterCase.ObItemClickListener() to to define what happen if the item in recyclerView is clicked

        adapter_Case = new AdapterCase(options, new AdapterCase.OnItemClickListener() {
            @Override
            public void onItemClick(ModelCases model) {
                Intent intent = new Intent(adminCases.this, caseDetailsAdmin.class);
                intent.putExtra("casetype", model.getCasetype());
                intent.putExtra("keywords", model.getKeywords());
                intent.putExtra("desc", model.getDesc());
                intent.putExtra("summary", model.getSummary());
                intent.putExtra("url", model.getUrl());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter_Case);// main adapter is like the manager who decide what items to show
       adapter_Case.startListening(); //telling manager to pay attention to changes of data, and update himself
    }

    @Override
    //is call when activity become visible to user
    protected void onStart() {
        super.onStart();
        adapter_Case.startListening(); //specific to firebase's recyclerView. tell adapter to start listening to firebase changes and update the UI
    }

    @Override
    //is call when activity no longer visible to user(means it goes into the background)
    protected void onStop() {
        super.onStop();
        adapter_Case.stopListening();
    }

}

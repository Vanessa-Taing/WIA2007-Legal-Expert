package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

// View.onCLickListener is more general that handles clicks events for any type of view(UI element)
// MainLawyerAdapter.onItemClickListener is more specific and used in context of recyclerView
public class MainActivityLawyer extends AppCompatActivity {

    private String lawyerId;
    RecyclerView recyclerView;
    MainLawyerAdapter mainLawyerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lawyer);

        String caseType = getIntent().getStringExtra("caseType");
        String caseID = getIntent().getStringExtra("caseID");

        // Change the background color & text of the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Suggested Lawyers");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //recycler view setup
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        //firebaseRecycler adapter set up
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").orderByChild("specialization").equalTo(caseType), MainModel.class)
                        .build();

        mainLawyerAdapter = new MainLawyerAdapter(options, new MainLawyerAdapter.OnItemClickListener() {
            public void onItemClick(MainModel model, String lawyerID) {
                MainActivityLawyer.this.lawyerId = lawyerID;

                Intent intent = new Intent(MainActivityLawyer.this, lawyerDetails.class);
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
                intent.putExtra("lawyer_id", lawyerID);
                intent.putExtra("caseID",caseID);
                startActivity(intent);
            }


        });

        recyclerView.setAdapter(mainLawyerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager is like telling someone how to arrange the item
        //recyclerView.setAdapter is like actually put the item to there and let him arrange
        // Check if the search result is empty and print a message

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

        mainLawyerAdapter = new MainLawyerAdapter(options, new MainLawyerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainModel model, String lawyerId) {

                //so intent is like a magical messenger bird that helps you move from one room to another (in your app) (so it takes 2 parameters)
                Intent intent = new Intent(MainActivityLawyer.this, lawyerDetails.class);
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
        recyclerView.setAdapter(mainLawyerAdapter);
        mainLawyerAdapter.startListening();


    }


    @Override
    //is call when activity become visible to user
    protected void onStart() {
        super.onStart();
        mainLawyerAdapter.startListening(); //specific to firebase's recyclerView. tell adapter to start listening to firebase changes and update the UI
    }

    @Override
    //is call when activity no longer visible to user(means it goes into the background)
    protected void onStop() {
        super.onStop();
        mainLawyerAdapter.stopListening();
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

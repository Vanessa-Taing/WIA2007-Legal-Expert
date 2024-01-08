package com.example.mad_login;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_login.Model.LawyerInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LawyerDetailsActivity extends AppCompatActivity {
    private TextView TVname, TVlawfirm, TVexpYear, TVspecialization, TVlanguage, TVstate, TVmobile, TVemail, IVimage;
    private Button BtnChatLawyer;

    private RatingBar RBRating;
    private String lawyerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);

        TVname = findViewById(R.id.lawyerName);
        TVlawfirm = findViewById(R.id.lawFirm);
        TVexpYear = findViewById(R.id.EXPYear);
        TVspecialization = findViewById(R.id.specialization);
        TVlanguage = findViewById(R.id.language);
        TVstate = findViewById(R.id.state);
        TVmobile = findViewById(R.id.mobile);
        TVemail = findViewById(R.id.email);
        BtnChatLawyer = findViewById(R.id.btnContactLawyer);
        RBRating = findViewById(R.id.rating);

        lawyerID = getIntent().getStringExtra("uid");

        DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(lawyerID).child("Rating");

        ValueEventListener ratingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalRating = 0;
                int ratingCount = 0;

                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
                    LawyerRating lawyerRating = caseSnapshot.getValue(LawyerRating.class);
                    if (lawyerRating != null) {
                        totalRating += lawyerRating.getRating();
                        ratingCount++;
                    }
                }

                if (ratingCount > 0) {
                    float averageRating = (float) totalRating / ratingCount;
                    // Set the averageRating value to the RatingBar
                    RBRating.setRating(averageRating);
                } else {
                    // No ratings available
                    RBRating.setRating(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that may occur during the retrieval process
            }
        };


        // Attach the listener to the ratingsRef
        ratingsRef.addValueEventListener(ratingsListener);

        String uid = getIntent().getStringExtra("uid");
        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String lawfirm = getIntent().getStringExtra("lawfirm");
        String dob = getIntent().getStringExtra("DOB");
        String expyear = getIntent().getStringExtra("expYear");
        String spec = getIntent().getStringExtra("specialization");
        String gender = getIntent().getStringExtra("gender");
        String language = getIntent().getStringExtra("language");
        String state = getIntent().getStringExtra("state");
        String mobile = getIntent().getStringExtra("mobile");
        String email = getIntent().getStringExtra("email");
        LawyerInfo lawyerInfo = getIntent().getParcelableExtra("lawyerInfo");

        Log.d("LawyerDetailsActivity", "uid: " + uid);
        Log.d("LawyerDetailsActivity", "name: " + name);
        Log.d("LawyerDetailsActivity", "imageUrl: " + imageUrl);


        TVname.setText(name);
        TVemail.setText("Email: " + email);
        TVstate.setText("State: " + state);
        TVmobile.setText("Mobile: " + mobile);
        TVspecialization.setText("Specialization: " + spec);
        TVlanguage.setText("Language: " + language);
        TVlawfirm.setText(lawfirm);
        TVexpYear.setText("Experience year: " + expyear);
        //IVimage

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.my_primary)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("lawyer details");

        Intent intent = getIntent();
        LawyerInfo model = (LawyerInfo) intent.getSerializableExtra("lawyerInfo");

        BtnChatLawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model != null) {
                    Intent chatIntent = new Intent(LawyerDetailsActivity.this, MessageActivity.class);
                    chatIntent.putExtra("userid", uid);
                    chatIntent.putExtra("username", name);
                    chatIntent.putExtra("imageUrl", imageUrl);

                    Log.d("LawyerDetailsActivity", "Intent extras: " + chatIntent.getExtras());

                    startActivity(chatIntent);
                }else {
                    Log.e("LawyerDetailsActivity", "LawyerInfo model is null");
                }
            }
        });



//        Toolbar toolbar = findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
    }
}
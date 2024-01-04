package com.example.mad_login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_login.Model.FeedbackApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FeedbackOnApp extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText etFeedback;
    private Button btnSubmitFeedback;
    private FeedbackDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_on_app);

        ratingBar = findViewById(R.id.ratingBar);
        etFeedback = findViewById(R.id.ETFeedback);
        btnSubmitFeedback = findViewById(R.id.BtnSubmitFeedback);

        databaseHelper = new FeedbackDatabaseHelper(this);

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        float rating = ratingBar.getRating();
        String comments = etFeedback.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userId = firebaseUser != null ? firebaseUser.getUid() : "";
        long timestamp = System.currentTimeMillis();  // Current timestamp

        FeedbackApp feedback = new FeedbackApp(rating, comments, userId, timestamp);
        long newRowId = databaseHelper.addFeedback(feedback);

        if (newRowId != -1) {
            Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
        }

        // You can clear the fields or perform any other actions after submission
        ratingBar.setRating(0);
        etFeedback.getText().clear();
    }
}

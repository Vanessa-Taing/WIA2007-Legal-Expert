package com.example.mad_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class lawyerDetails extends AppCompatActivity {
    private TextView TVname, TVlawfirm, TVDOB, TVexpYear, TVspecialization, TVgender, TVlanguage, TVstate, TVmobile, TVemail;
    private ImageView IVimage;
    private Button btnAppointment,btnContactLawyer;
    private FirebaseUser firebaseUser;
    private RatingBar RBRating;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);

        TVname = findViewById(R.id.lawyerName);
        TVlawfirm = findViewById(R.id.lawFirm);
        TVDOB = findViewById(R.id.DOB);
        TVexpYear = findViewById(R.id.EXPYear);
        TVspecialization = findViewById(R.id.specialization);
        TVgender = findViewById(R.id.gender);
        TVlanguage = findViewById(R.id.language);
        TVstate = findViewById(R.id.state);
        TVmobile = findViewById(R.id.mobile);
        TVemail = findViewById(R.id.email);
        IVimage = findViewById(R.id.lawyerImage);
        btnAppointment =findViewById(R.id.btnAppointment);
        btnContactLawyer =findViewById(R.id.btnContactLawyer);
        RBRating = findViewById(R.id.rating);


        auth =FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();



        //  stored the lawyer data under a node called "Registered Lawyers"
        String lawyerId = auth.getCurrentUser().getUid();
        DatabaseReference lawyerRef = FirebaseDatabase.getInstance().getReference().child("Registered Lawyers").child(lawyerId);

        lawyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReadWriteUserDetails readUserDetails = dataSnapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails!=null) {
                    String name = readUserDetails.name;
                    String lawfirm = readUserDetails.lawFirm;
                    String dob = readUserDetails.doB;
                    String expyear =readUserDetails.expYear;
                    String spec = readUserDetails.specialization;
                    String gender = readUserDetails.gender;
                    String language = readUserDetails.language;
                    String state =readUserDetails.state;
                    String mobile = readUserDetails.mobile;
                    String email = readUserDetails.email;
                    //set rating

                    // Update the UI with the retrieved data
                    TVname.setText(name);
                    TVDOB.setText("Date of birth: " + dob);
                    TVemail.setText("Email: " + email);
                    TVgender.setText("Gender: " + gender);
                    TVstate.setText("State: " + state);
                    TVmobile.setText("Mobile: " + mobile);
                    TVspecialization.setText("Specialization: " + spec);
                    TVlanguage.setText("Language: " + language);
                    TVlawfirm.setText("Law Firm: "+lawfirm);
                    TVexpYear.setText("Experience year: " + expyear);
                    //set rating

                    //code to set profile picture
                    // Set default profile picture
                    IVimage.setImageResource(R.drawable.ic_baseline_account_box_24);
                    // Check if the user has a profile picture
                    if (firebaseUser.getPhotoUrl() != null) {
                        // Load the uploaded profile picture using Picasso
                        Uri uri = firebaseUser.getPhotoUrl();
                        Picasso.get().load(uri).into(IVimage);
                    }
                }else{
                    Toast.makeText(lawyerDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(lawyerDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });




    }
}
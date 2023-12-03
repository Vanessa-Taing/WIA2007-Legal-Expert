package com.example.legalexpert_lawyeractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

public class lawyerDetails extends AppCompatActivity {
    private TextView TVname, TVlawfirm, TVDOB, TVexpYear, TVspecialization, TVgender, TVlanguage, TVstate, TVmobile, TVemail, IVimage;
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

        String name = getIntent().getStringExtra("name");
        String lawfirm = getIntent().getStringExtra("lawfirm");
        String dob = getIntent().getStringExtra("DOB");
        String expyear = getIntent().getStringExtra("expYear");
        String spec = getIntent().getStringExtra("specialization");
        String gender = getIntent().getStringExtra("gender");
        String language = getIntent().getStringExtra("language");
        String state = getIntent().getStringExtra("state");
        String mobile = getIntent().getStringExtra("mobile");
        String email = getIntent().getStringExtra("email");

        TVname.setText(name);
        TVDOB.setText("Date of birth: " + dob);
        TVemail.setText("Email: " + email);
        TVgender.setText("Gender: " + gender);
        TVstate.setText("State: " + state);
        TVmobile.setText("Mobile: " + mobile);
        TVspecialization.setText("Specialization: " + spec);
        TVlanguage.setText("Language: " + language);
        TVlawfirm.setText(lawfirm);
        TVexpYear.setText("Experience year: " + expyear);
        //IVimage

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainPurple)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("lawyer details");

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
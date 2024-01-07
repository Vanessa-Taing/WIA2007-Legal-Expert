package com.example.mad_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class signUp_SelectRole extends AppCompatActivity {
    ImageView IVCitizen, IVLawyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_select_role);

        IVCitizen = findViewById(R.id.IVCitizen);
        IVLawyer = findViewById(R.id.IVLawyer);

        IVCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to Citizen login page
                Intent intent = new Intent(signUp_SelectRole.this, SignUp_generalUser.class);
                startActivity(intent);
            }
        });

        IVLawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to Lawyer login page
                Intent intent = new Intent(signUp_SelectRole.this, lawyerPersonal_Info.class);
                startActivity(intent);
            }
        });
    }
}

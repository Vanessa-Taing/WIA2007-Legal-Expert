package com.example.mad_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class adminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        EditText etPassword = findViewById(R.id.ETAdminPassword);
        TextView tvBackbtn = findViewById(R.id.tvBackbtn);
        Button btnSubmitAdmin = findViewById(R.id.btnSubmitAdmin);

        tvBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminLogin.this, login_selectRole.class);
                startActivity(intent);
            }
        });

        btnSubmitAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                if (password.equals("admin")){
                    Intent intent = new Intent(adminLogin.this, adminCases.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(adminLogin.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
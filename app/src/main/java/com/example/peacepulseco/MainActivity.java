package com.example.peacepulseco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int black = getResources().getColor(R.color.black);
        EditText editTextEmail = findViewById(R.id.etEmail);
        EditText editTextPassword = findViewById(R.id.etPassword);

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String defaultText = "Enter Your Email";
                    String currentText = editTextEmail.getText().toString().trim();
                    if (currentText.equals(defaultText)) {
                        editTextEmail.setText("");
                        editTextEmail.setTextColor(black);
                    }
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String defaultText = "Enter Your Password";
                    String currentText = editTextPassword.getText().toString().trim();
                    if (currentText.equals(defaultText)) {
                        editTextPassword.setText("");
                        editTextPassword.setTextColor(black);
                    }
                }
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RequestActivity.class);
                startActivity(intent);
            }
        });

        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LawyerSignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
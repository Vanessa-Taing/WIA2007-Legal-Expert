package com.example.mad_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class lawyerSuccessSignUpNotification extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_success_sign_up_notification);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(lawyerSuccessSignUpNotification.this, lawyerLoginPage.class);
                startActivity(intent);
                finish();
            }
        },3000);
}
}

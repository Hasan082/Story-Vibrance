package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hasan.storyvibrance.R;

public class WelcomeActivity extends AppCompatActivity {

    Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        // Set OnClickListener for the Register button
        register.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for the Login button
        login.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });



    }
}
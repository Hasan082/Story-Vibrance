package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hasan.storyvibrance.R;

public class SignupActivity extends AppCompatActivity {

    Button signup;
    TextView loginTextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = findViewById(R.id.signup);
        loginTextBtn = findViewById(R.id.loginTextBtn);


        signup.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
        loginTextBtn.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }
}
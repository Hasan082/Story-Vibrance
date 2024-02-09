package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hasan.storyvibrance.Feed.FeedActivity;
import com.hasan.storyvibrance.R;

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView signTxtBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        signTxtBtn = findViewById(R.id.signTxtBtn);

        login.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, FeedActivity.class)));
        signTxtBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

    }
}
package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        // Send to Signin page
        binding.register.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
        });

        // Set OnClickListener for the Login button
        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        });



    }
}
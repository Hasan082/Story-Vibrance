package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivitySignupBinding;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase authentication instance

    ActivitySignupBinding binding; // View binding object



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup); // Set content view using data binding
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase authentication instance

        // REGISTRATION PROCESS WITH FIREBASE ===============
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve input values from edittext
                String name = binding.nameEdTxt.getText().toString().trim();
                String username = binding.usernameEdTxt.getText().toString().trim();
                String password = binding.passEdTxt.getText().toString();

                // Check if input fields are not empty
                if (!name.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    // Create user account with email and password
                    mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Start LoginActivity if all fields are filled
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            // Registration success, show toast message
                            Toast.makeText(SignupActivity.this, "SignUp Success! Please login!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Registration failed, retrieve error message from task
                            String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                            // Registration failed, show error toast message
                            Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Show toast message if any field is empty
                    Toast.makeText(SignupActivity.this, "Username and/or password is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // GO TO LOGIN PAGE IF ALREADY USER================================
        binding.loginTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


}
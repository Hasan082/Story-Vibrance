package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityForgetPassBinding;

public class ForgetPassActivity extends AppCompatActivity {

    ActivityForgetPassBinding binding;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_pass);
        mAuth = FirebaseAuth.getInstance();


        binding.sendEmail.setOnClickListener(v -> {
            String email = binding.emailEdTxt.getText().toString().trim();

            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(unused -> {
                            String feedback = "A password reset link has been sent to email.";
                            Toast.makeText(ForgetPassActivity.this, feedback, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPassActivity.this, LoginActivity.class));
                            binding.emailEdTxt.setText("");
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            String feedback;
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                feedback = "No account is registered with this email.";
                            } else {
                                feedback = e.getMessage();
                            }
                            Toast.makeText(ForgetPassActivity.this, feedback, Toast.LENGTH_SHORT).show();
                        });
            } else {
                String fieldErr = "Your email field is empty.";
                Toast.makeText(ForgetPassActivity.this, fieldErr, Toast.LENGTH_SHORT).show();
            }
        });

    }//on create method==============





}
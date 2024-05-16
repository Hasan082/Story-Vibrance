package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase authentication instance
    private FirebaseFirestore db; //FIREBASE FIRESTORE Instance

    ActivitySignupBinding binding; // View binding object



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup); // Set content view using data binding
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase authentication instance
        db = FirebaseFirestore.getInstance();

        // REGISTRATION PROCESS WITH FIREBASE ===============
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve input values from edittext
                String name = binding.nameEdTxt.getText().toString().trim();
                String username = binding.usernameEdTxt.getText().toString().trim();
                String password = binding.passEdTxt.getText().toString();

                // Regular expression pattern for email validation
                String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

                // Check if input fields are not empty
                if (!name.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    if (!username.matches(emailPattern)){
                        Toast.makeText(SignupActivity.this, "Username is not valid!", Toast.LENGTH_SHORT).show();
                    } else  {
                        //Show Loader==========
                        showLoadingIndicator();
                        // Create user account with email and password
                        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                //Add user data to FireStore
                                String uid = mAuth.getUid(); // Extract the UID from Firebase Auth
                                if(uid != null){
                                    Map<String, String> userData = new HashMap<>(); // Declare a HashMap
                                    userData.put("id", uid); //Put the UID to HashMap
                                    userData.put("email", username); //Put the Email to HashMap
                                    String lowercaseName = name.toLowerCase();
                                    userData.put("name", lowercaseName);
                                    db.collection("userdata").document(username).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                // Registration success and data added to database and show  Toast
                                                Toast.makeText(SignupActivity.this, "SignUp Success! Please login!", Toast.LENGTH_SHORT).show();
                                                // Start LoginActivity if all fields are filled
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                //Clear all filed =====
                                                binding.nameEdTxt.setText("");
                                                binding.usernameEdTxt.setText("");
                                                binding.passEdTxt.setText("");
                                                //HIDE LOADING BAR========================
                                                hideLoadingIndicator();
                                            }else {
                                                //HIDE LOADING BAR========================
                                                hideLoadingIndicator();
                                                String err = Objects.requireNonNull(task.getException()).getMessage();
                                                Toast.makeText(SignupActivity.this, err, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                // Registration failed, retrieve error message from task
                                String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                // Registration failed, show error toast message
                                Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                //HIDE LOADING BAR========================
                                hideLoadingIndicator();
                            }
                        });
                    }

                } else {
                    // Show toast message if any field is empty
                    Toast.makeText(SignupActivity.this, "Username and/or password and/or Name is empty", Toast.LENGTH_SHORT).show();
                } //End of if Check empty
            }
        });

        // GO TO LOGIN PAGE IF ALREADY USER================================
        binding.loginTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish(); //CLEAR THE STACK TO AVOID TOO MANY BACKSTAGE
            }
        });
    } // END OF CN CREATE METHOD====

    // Method to show the progress bar
    private void showLoadingIndicator() {
        binding.loadingIndicator.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // Method to hide the loading indicator and enable user interaction
    private void hideLoadingIndicator() {
        binding.loadingIndicator.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


}
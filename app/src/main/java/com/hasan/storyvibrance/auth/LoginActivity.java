package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.BottomNav.NavigationActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    SharedPreferences.Editor sPrefEdit;
    ActivityLoginBinding binding;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        sPrefEdit = sPref.edit();

        binding.login.setOnClickListener(view -> {


            String username = binding.usernameEdTxt.getText().toString().trim();
            String password = binding.passEdTxt.getText().toString();

            if(!username.isEmpty() && !password.isEmpty()){
                //Show loading=======
                showLoadingIndicator();
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //Clear the filed===
                        binding.usernameEdTxt.setText("");
                        binding.passEdTxt.setText("");
                        //Extract the UID from Firebase===========
                        String uid = mAuth.getUid();
                        //Set the UID and Username for autologin===
                        sPrefEdit.putString("uid", uid);
                        sPrefEdit.putString("username", username);
                        sPrefEdit.apply();
                        //Send user to Home
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                        //Hide the Loading animation====
                        hideLoadingIndicator();
                        //Kill the login page===
                        finish();
                    }else  {
                        String err = "Credentials do not match our records.";
                        Toast.makeText(LoginActivity.this ,err, Toast.LENGTH_SHORT).show();
                        hideLoadingIndicator();
                    }
                });

            }else {
                Toast.makeText(LoginActivity.this, "Username and/or Password is empty", Toast.LENGTH_SHORT).show();
            }


        });


        //If New user send to signup page
        binding.signTxtBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        //Forget password redirect===========
        binding.forgetPass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class)));


    }//END OF ON CREATE====

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
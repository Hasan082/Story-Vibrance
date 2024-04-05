package com.hasan.storyvibrance.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hasan.storyvibrance.BottomNav.BottomNavActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    SharedPreferences sPref;
    ActivityLoginBinding binding;

    boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor sPrefEdit = sPref.edit();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String username = binding.usernameEdTxt.getText().toString().trim();
                String password = binding.passEdTxt.getText().toString();

                if(!username.isEmpty() && !password.isEmpty()){
                    //Show loading=======
                    showLoadingIndicator();
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sPrefEdit.putString("username", username);
                                sPrefEdit.apply();
                                binding.usernameEdTxt.setText("");
                                binding.passEdTxt.setText("");
                                String uid = mAuth.getUid();
                                SharedPreferences.Editor editor = sPref.edit();
                                editor.putString("uid", uid);
                                editor.putString("username", uid);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, BottomNavActivity.class));
                                hideLoadingIndicator();
                                finish();
                            }else  {
                                String err = "Credentials do not match our records.";
                                Toast.makeText(LoginActivity.this ,err, Toast.LENGTH_SHORT).show();
                                hideLoadingIndicator();
                            }
                        }
                    });

                }else {
                    Toast.makeText(LoginActivity.this, "Username and/or Password is empty", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //If New user send to signup page
        binding.signTxtBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

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
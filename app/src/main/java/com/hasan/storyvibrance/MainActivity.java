package com.hasan.storyvibrance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.hasan.storyvibrance.BottomNav.NavigationActivity;
import com.hasan.storyvibrance.OnBoard.OnBoardActivity;
import com.hasan.storyvibrance.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean onBoardDone = preferences.getBoolean("onBoardDone", false);

        // Retrieve saved UID and username and Auto login
        String savedUid = preferences.getString("uid", "");
        String savedUsername = preferences.getString("username", "");
        //IF Already Logged In user=========
        if (!savedUid.isEmpty() && !savedUsername.isEmpty()) {
            startActivity(new Intent(this, NavigationActivity.class));
            finish();
        } else if (onBoardDone) {
            //If Previously open the app=====
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            // Show on boarding fragment
            startActivity(new Intent(MainActivity.this, OnBoardActivity.class));
        }
    }


    //ToDo===To be done=====
    // profile page fix
    // User profile click over image
    // Single post view
    // Chat message
    // Audio/Video calling
    // appbar image





}

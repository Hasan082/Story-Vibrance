package com.hasan.storyvibrance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.hasan.storyvibrance.BottomNav.BottomNavActivity;
import com.hasan.storyvibrance.OnBoard.OnBoardOne;
import com.hasan.storyvibrance.auth.WelcomeActivity;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean onBoardDone = preferences.getBoolean("onBoardDone", false);

        // Retrieve saved UID and username and Auto login
        String savedUid = preferences.getString("uid", "");
        String savedUsername = preferences.getString("username", "");
        if (!savedUid.isEmpty() && !savedUsername.isEmpty()) {
            startActivity(new Intent(this, BottomNavActivity.class));
            finish();
        } else if (onBoardDone) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        } else {
            // Show on boarding fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.mainFrame, new OnBoardOne());
            fragmentTransaction.commit();
        }
    }
}

package com.hasan.storyvibrance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hasan.storyvibrance.OnBoard.OnBoardOne;
import com.hasan.storyvibrance.auth.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean onBoardDone = preferences.getBoolean("onBoardDone", false);

        if (onBoardDone) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.mainFrame, new OnBoardOne());
            fragmentTransaction.commit();
        }

    }
}
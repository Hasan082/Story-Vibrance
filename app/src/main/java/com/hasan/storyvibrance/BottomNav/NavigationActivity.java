package com.hasan.storyvibrance.BottomNav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.hasan.storyvibrance.Messenger.MessengerActivity;
import com.hasan.storyvibrance.Notification.NotificationActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.auth.LoginActivity;
import com.hasan.storyvibrance.databinding.ActivityNavigationBinding;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity {

    ActivityNavigationBinding binding;

    SharedPreferences sPref;
    SharedPreferences.Editor sPrefEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation);
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        sPrefEdit = sPref.edit();


        //LOGOUT FUNCTION=====================

        binding.drawerNavView.setNavigationItemSelectedListener(item->{
            if(item.getItemId()==R.id.logout){
                sPrefEdit.remove("uid");
                sPrefEdit.remove("username");
                sPrefEdit.apply();
                startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
            }
            return true;
        });





        //BOTTOM NAVIGATION CODE START FROM HERE==================================
        //Set Home Fragment on First Load=========
        getSupportFragmentManager().beginTransaction().replace(R.id.insertView, new FragmentHome()).commit();

        //Switch Navigation when Bottom Nav press=====
        binding.bottomNavBar.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            boolean fragmentSelected = false;

            if(item.getItemId() == R.id.home){
                selectedFragment = new FragmentHome();
                fragmentSelected = true;
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new FragmentProfile();
                fragmentSelected = true;
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.insertView, selectedFragment).commit();
            }

            return fragmentSelected;
        });



    }


    public void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

}
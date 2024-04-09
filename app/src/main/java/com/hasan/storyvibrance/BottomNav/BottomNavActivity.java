package com.hasan.storyvibrance.BottomNav;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.hasan.storyvibrance.Messenger.MessengerActivity;
import com.hasan.storyvibrance.Notification.NotificationActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.auth.LoginActivity;
import com.hasan.storyvibrance.databinding.ActivityBottomNavBinding;
import java.util.Objects;
public class BottomNavActivity extends AppCompatActivity {

    ActivityBottomNavBinding binding;
    ActionBarDrawerToggle toggle;
    SharedPreferences sPref;
    SharedPreferences.Editor sPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_nav);
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        sPrefEdit = sPref.edit();
// FOR OPEN DRAWER USING HUMBER ICON WITH APP BAR BELOW(DO NOT DELETE, KEEP IT FOR FUTURE REFERENCE)====================
//        setSupportActionBar(binding.toolBar);
//        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.open, R.string.close);
//        binding.drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
// FOR OPEN DRAWER USING HUMBER ICON WITH APP BAR ABOVE(DO NOT DELETE, KEEP IT FOR FUTURE REFERENCE)====================

        //Open Drawer By Appbar Profile Image Click==============================
        binding.customAppBar.findViewById(R.id.appbarImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // ToDo===========================
        //profile
        //update profile

        //Go To Notification page by click Notification icon=====================
        binding.customAppBar.findViewById(R.id.notificationIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BottomNavActivity.this, NotificationActivity.class));
            }
        });

        //Go To Messenger page by click messenger icon==========================
        binding.customAppBar.findViewById(R.id.messengerIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BottomNavActivity.this, MessengerActivity.class));
            }
        });

        binding.drawerNavView.setNavigationItemSelectedListener(item->{


            if(item.getItemId()==R.id.logout){
                sPrefEdit.remove("uid");
                sPrefEdit.remove("username");
                sPrefEdit.apply();
                startActivity(new Intent(BottomNavActivity.this, LoginActivity.class));
                Toast.makeText(BottomNavActivity.this, "Checking....", Toast.LENGTH_SHORT).show();
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
            } else if (item.getItemId() == R.id.search) {
                selectedFragment = new FragmentSearch();
                fragmentSelected = true;
            }else if (item.getItemId() == R.id.profile) {
                selectedFragment = new FragmentProfile();
                fragmentSelected = true;
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.insertView, selectedFragment).commit();
            }

            return fragmentSelected;
        });

    }
}
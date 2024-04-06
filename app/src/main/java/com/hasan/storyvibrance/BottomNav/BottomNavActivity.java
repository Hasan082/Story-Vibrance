package com.hasan.storyvibrance.BottomNav;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityBottomNavBinding;

public class BottomNavActivity extends AppCompatActivity {

    ActivityBottomNavBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_nav);

        //Set Home Fragment on First Load=========
        getSupportFragmentManager().beginTransaction().replace(R.id.insertView, new FragmentHome()).commit();

        //Switch Navigation when Bottom Nav press=====
        binding.bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
            }
        });

    }
}
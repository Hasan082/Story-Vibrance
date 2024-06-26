package com.hasan.storyvibrance.BottomNav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Posts.AddFriendActivity;
import com.hasan.storyvibrance.Posts.AddPostActivity;
import com.hasan.storyvibrance.Posts.FriendListActivity;
import com.hasan.storyvibrance.Posts.SavedPostActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.hasan.storyvibrance.auth.LoginActivity;
import com.hasan.storyvibrance.databinding.ActivityNavigationBinding;
import com.squareup.picasso.Picasso;

public class NavigationActivity extends AppCompatActivity {

    ActivityNavigationBinding binding;

    SharedPreferences sPref;
    SharedPreferences.Editor sPrefEdit;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation);
        db = FirebaseFirestore.getInstance();
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        sPrefEdit = sPref.edit();


        //Drawer NAV FUNCTION======================

        binding.drawerNavView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.add_friend) {
                startActivity(new Intent(NavigationActivity.this, AddFriendActivity.class));
            }
            if (item.getItemId() == R.id.friend_list) {
                startActivity(new Intent(NavigationActivity.this, FriendListActivity.class));
            }
            //LOGOUT=====================
            if (item.getItemId() == R.id.logout) {
                sPrefEdit.remove("uid");
                sPrefEdit.remove("username");
                sPrefEdit.apply();
                startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
                finish();
            }
            //Saved Post=====================
            if (item.getItemId() == R.id.SavedPost) {
                startActivity(new Intent(NavigationActivity.this, SavedPostActivity.class));
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        //BOTTOM NAVIGATION CODE START FROM HERE==================================
        //Set Home Fragment on First Load=========
        getSupportFragmentManager().beginTransaction().replace(R.id.insertView, new FragmentHome()).commit();

        //Switch Navigation when Bottom Nav press=====
        binding.bottomNavBar.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            boolean fragmentSelected = false;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new FragmentHome();
                fragmentSelected = true;
            } else if (item.getItemId() == R.id.profile) {
                selectedFragment = new FragmentProfile();
                fragmentSelected = true;
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.insertView, selectedFragment).commit();
                //Call again when fragment changes
                loadUserProfile();
            }

            return fragmentSelected;
        });


        //Add post button press======================
        binding.addPost.setOnClickListener(v -> startActivity(new Intent(NavigationActivity.this, AddPostActivity.class)));

        //call the appbar data load method
        loadUserProfile();


    }

    private void loadUserProfile() {
        String userName = GetUserName.getUsernameFromSharedPreferences(this);
        db.collection("userdata").document(userName).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String profileUri = documentSnapshot.getString("ProfileImg");
                String personName = documentSnapshot.getString("name");
                String personemail = documentSnapshot.getString("email");
                // Load profile image into the ImageView if appbarOmg is not null
                ImageView appbarImg = binding.drawerLayout.findViewById(R.id.appbarImg);
                ImageView drawerHeaderImg = binding.drawerLayout.findViewById(R.id.drawerHeaderImg);
                TextView personNmId = binding.drawerLayout.findViewById(R.id.personName);
                TextView personEmailAd = binding.drawerLayout.findViewById(R.id.personEmail);
                if (appbarImg != null && profileUri != null)  Picasso.get().load(profileUri).placeholder(R.drawable.person).into(appbarImg);
                else Log.e("appbarImg", "appbarImg or profileUri is null");
                if (drawerHeaderImg != null && profileUri != null)  Picasso.get().load(profileUri).placeholder(R.drawable.person).into(drawerHeaderImg);
                else Log.e("appbarImg", "appbarImg or profileUri is null");
                if (personNmId != null && personName != null)  personNmId.setText(NameCapitalize.capitalize(personName));
                else Log.e("personNmId", "personNmId is null");
                if (personEmailAd != null && personemail != null)  personEmailAd.setText(personemail);
                else Log.e("personNmId", "personNmId is null");

            } else {
                // Handle the case where the document does not exist
                Log.d("document", "No such document");
            }
        }).addOnFailureListener(e -> {
            // Handle any errors that occurred while retrieving the document
            Log.e("Database error", "Error fetching document: " + e.getMessage());
        });
    }


    public void openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }

}
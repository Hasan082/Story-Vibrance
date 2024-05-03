package com.hasan.storyvibrance.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityUpdateProfileBinding;

import java.util.HashMap;
import java.util.Map;


public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;
    SharedPreferences sharedPreferences;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(UpdateProfileActivity.this, R.layout.activity_update_profile);
        db = FirebaseFirestore.getInstance();

        //GET THE USER NAME FROM SharedPreferences
        String userName = getUsernameFromSharedPreferences();


        //GET THE FULL NAME FROM DATABASE AND SET IT
        db.collection("userdata").document(userName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String personName = documentSnapshot.getString("name");
                    String userBio = documentSnapshot.getString("bio");
                    if (personName != null) {
                        binding.setFullName(personName);
                    } else {
                        // Handle the case where personName is null
                        binding.setFullName(getString(R.string.full_name));
                    }
                    if (userBio != null) {
                        binding.setUserBio(userBio);
                    } else {
                        binding.setUserBio(getString(R.string.about_me));
                    }
                    binding.setEmailAddress(userName);
                    //Hide loader
                    binding.spinner.setVisibility(View.GONE);
                } else {
                    //if no data available in database
                    showDatabaseErrorMessage();
                }
            } else {
                // Handle failures
                showDatabaseErrorMessage();
            }
        });

        //Back to profile page===========
        binding.backBtn.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());

        //update data here==============
        binding.updateBtn.setOnClickListener(v -> {
            binding.spinner.setVisibility(View.VISIBLE);
            String username = getUsernameFromSharedPreferences();
            updateProfileData(username);
        });
    }//END OF ON CREATE===============

    private void updateProfileData(String username) {
        String fullName = binding.personName.getText().toString();
        String userBio = binding.aboutBio.getText().toString();

        // Input Validation
        if (fullName.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, "Full name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", fullName);
        updates.put("bio", userBio);

        db.collection("userdata").document(username).update(updates).addOnSuccessListener(unused -> {
            Toast.makeText(UpdateProfileActivity.this, "Info Updated!", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    // Retrieve the username from SharedPreferences
    private String getUsernameFromSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("username", "");
    }

    // Show database error message
    private void showDatabaseErrorMessage() {
        Toast.makeText(UpdateProfileActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
    }


}
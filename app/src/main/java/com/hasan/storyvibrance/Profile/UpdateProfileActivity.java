package com.hasan.storyvibrance.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.DataBaseError;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.databinding.ActivityUpdateProfileBinding;

import java.util.HashMap;
import java.util.Map;


public class UpdateProfileActivity extends AppCompatActivity {

    private ActivityUpdateProfileBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        db = FirebaseFirestore.getInstance();
        // Get username from SharedPreferences
        String userName = GetUserName.getUsernameFromSharedPreferences(this);

        // Fetch user data from Firestore
        db.collection("userdata").document(userName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract user data from document
                    String personName = documentSnapshot.getString("name");
                    String userBio = documentSnapshot.getString("bio");
                    // Set user data to UI
                    if (personName != null) {
                        binding.setFullName(personName);
                    } else {
                        binding.setFullName(getString(R.string.full_name));
                    }
                    if (userBio != null) {
                        binding.setUserBio(userBio);
                    } else {
                        binding.setUserBio(getString(R.string.about_me));
                    }
                    binding.setEmailAddress(userName);
                    binding.spinner.setVisibility(View.GONE);
                } else {
                    // Show error message if document does not exist
                    DataBaseError.showDatabaseErrorMessage(this);
                }
            } else {
                // Show error message if Firestore operation fails
                DataBaseError.showDatabaseErrorMessage(this);
            }

        });

        // Back button click listener
        binding.backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Update button click listener
        binding.updateBtn.setOnClickListener(v -> {
            // Show loading spinner
            binding.spinner.setVisibility(View.VISIBLE);
            // Get username and update profile data
            updateProfileData(userName);
        });

        // Back to the profile page
        binding.backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    } //end of onCreate

    /**
     * Updates the user's profile data in Firestore.
     *
     * @param username The username of the current user.
     */
    private void updateProfileData(String username) {
        String fullName = binding.personName.getText().toString();
        String userBio = binding.aboutBio.getText().toString();

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare updates
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", fullName);
        updates.put("bio", userBio);

        // Update user data in Firestore
        db.collection("userdata").document(username).update(updates).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Profile Info Updated!", Toast.LENGTH_SHORT).show();
            binding.spinner.setVisibility(View.GONE);
        });
    }


}
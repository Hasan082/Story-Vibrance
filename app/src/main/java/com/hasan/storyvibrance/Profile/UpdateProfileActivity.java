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

        binding.spinner.setVisibility(View.VISIBLE);
        //GET THE FULL NAME FROM DATABASE AND SET IT
        db.collection("userdata").document(userName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String personName = documentSnapshot.getString("name");
                    if (personName != null) {
                        binding.setFullName(personName);
                    }  // Handle the case where personName is null
                    binding.setEmailAddress(userName);
                    //Hide loader======
                    binding.spinner.setVisibility(View.GONE);
                } else {
                    //if no data available in database
                    Toast.makeText(UpdateProfileActivity.this, "Data Not available", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle failures
                Toast.makeText(UpdateProfileActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
            }
        });


        //Back to profile page======================
        binding.backBtn.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());



        //update data====

        binding.updateBtn.setOnClickListener(v -> {
            String username = getUsernameFromSharedPreferences();
            updateProfileData(username);
        });


    }//END OF ON CREATE===============

    private void updateProfileData(String username) {
        String fullName = binding.personName.getText().toString();
        String userBio = binding.userBio.getText().toString();
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", fullName);
        updates.put("bio", userBio);
        System.out.println("updates" +updates);

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


}
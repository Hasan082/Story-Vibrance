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

    private SharedPreferences sharedPreferences;
    private ActivityUpdateProfileBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Other initialization code
        String userName = getUsernameFromSharedPreferences();

        db.collection("userdata").document(userName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String personName = documentSnapshot.getString("name");
                    String userBio = documentSnapshot.getString("bio");
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
                    showDatabaseErrorMessage();
                }
            } else {
                showDatabaseErrorMessage();
            }
        });

        binding.backBtn.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());

        binding.updateBtn.setOnClickListener(v -> {
            binding.spinner.setVisibility(View.VISIBLE);
            String username = getUsernameFromSharedPreferences();
            updateProfileData(username);
        });
    }

    private void updateProfileData(String username) {
        String fullName = binding.personName.getText().toString();
        String userBio = binding.aboutBio.getText().toString();

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", fullName);
        updates.put("bio", userBio);

        db.collection("userdata").document(username).update(updates).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Info Updated!", Toast.LENGTH_SHORT).show();
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private String getUsernameFromSharedPreferences() {
        return sharedPreferences.getString("username", "");
    }

    private void showDatabaseErrorMessage() {
        Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show();
    }
}

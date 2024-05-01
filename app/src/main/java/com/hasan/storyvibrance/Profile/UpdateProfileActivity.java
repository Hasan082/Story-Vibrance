package com.hasan.storyvibrance.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityUpdateProfileBinding;


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
        db.collection("userdata").document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String personName = documentSnapshot.getString("name");
                        // Set personName directly
                        binding.setFullName(personName);
                        //SET THE USER NAME AND NON EDITABLE
                        binding.setEmailAddress(userName);
                        //Hide loader======
//                        binding.spinner.setVisibility(View.GONE);
                    } else {
                        //if no data available in database
                        Toast.makeText(UpdateProfileActivity.this, "Data Not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle failures
                    Toast.makeText(UpdateProfileActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }//END OF ON CREATE===============

    // Retrieve the username from SharedPreferences
    private String getUsernameFromSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("username", "");
    }


}
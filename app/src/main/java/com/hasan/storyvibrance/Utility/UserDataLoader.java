package com.hasan.storyvibrance.Utility;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.UserDataModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserDataLoader {

    // Map to cache user data
    public static Map<String, UserDataModel> userDataCache = new HashMap<>();

    public static void loadUserData(String userId, TextView authorName, ImageView authorImg) {
        UserDataModel userData = userDataCache.get(userId);
        if (userData != null) {
            // If user data is available in the cache, populate UI directly
            authorName.setText(userData.getFullName());
            Picasso.get().load(userData.getProfileImg()).into(authorImg);
        } else {
            // If user data is not available in the cache, fetch from Firestore
            FirebaseFirestore.getInstance().collection("userdata").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve user data from the document snapshot
                            String fullName = documentSnapshot.getString("name");
                            String profileImg = documentSnapshot.getString("ProfileImg");

                            // Populate author name and profile image
                            authorName.setText(fullName);
                            Picasso.get().load(profileImg).into(authorImg);

                            // Update the cache with the fetched user data
                            userDataCache.put(userId, new UserDataModel(fullName, profileImg));
                        } else {
                            Log.d("Firestore", "User document not found for user: " + userId);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user document: " + e.getMessage(), e));
        }
    }
}

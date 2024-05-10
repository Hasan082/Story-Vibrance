package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UserDataFetcher {
    private final FirebaseFirestore db;

    public UserDataFetcher(FirebaseFirestore db) {
        this.db = db;
    }

    public void fetchUserData(String username, TextView authorName, ImageView authorImg) {
        db.collection("userdata").document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = document.getString("name");
                    String profileImg = document.getString("ProfileImg");
                    authorName.setText(name);
                    Picasso.get().load(profileImg).into(authorImg);
                } else {
                    Log.d("Userdata", "No such document");
                }
            } else {
                Log.d("Userdata", "get failed with ", task.getException());
            }
        });
    }
}


package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDataFetcher {
    private final FirebaseFirestore db;

    public UserDataFetcher(FirebaseFirestore db) {
        this.db = db;
    }

    public void fetchUserData(String username, UserDataFetchListener listener) {
        db.collection("userdata").document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = document.getString("name");
                    String profileImg = document.getString("ProfileImg");
                    listener.onUserDataFetched(name, profileImg);
                } else {
                    Log.d("Userdata", "No such document");
                }
            } else {
                Log.d("Userdata", "get failed with ", task.getException());
            }
        });
    }

    // Define a callback interface for handling data retrieval
    public interface UserDataFetchListener {
        void onUserDataFetched(String name, String profileImg);
    }


}


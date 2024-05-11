package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;

import java.util.HashMap;
import java.util.Map;

public class PostSaver {



    public static void handleSavePost(PostModel post, boolean newSavedState, ImageView savedIcon, String username) {
        if (newSavedState) {
            savedIcon.setImageResource(R.drawable.post_saved);
        } else {
            savedIcon.setImageResource(R.drawable.post_save);
        }
        // Save or remove the post as "saved" in the database
        saveOrRemovePostAsSaved(post, newSavedState, username);

        // Update the post's savedByUsers map
        Map<String, Boolean> savedByUsers = post.getSavedByUsers();
        if (savedByUsers == null) {
            savedByUsers = new HashMap<>();
        }
        savedByUsers.put(username, newSavedState);
        post.setSavedByUsers(savedByUsers);
    }



    /**
     * Helper function to save or remove the post as saved in Firestore by a specific user.
     *
     * @param post  The PostModel object representing the post to be saved or removed.
     * @param saved A boolean indicating whether the post should be saved (true) or removed (false).
     * @param userId The ID of the user who is saving or removing the post.
     */
    private static void saveOrRemovePostAsSaved(PostModel post, boolean saved, String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference postRef = db.collection("posts").document(post.getPostId());

        // Fetch the current savedByUsers map from the post
        Map<String, Boolean> savedByUsers = post.getSavedByUsers();

        // Update the savedByUsers map based on the save action
        if (saved) {
            // Save the post for the user
            savedByUsers.put(userId, true);
        } else {
            // Remove the post from saved posts for the user
            savedByUsers.remove(userId);
        }

        // Update the post document in the "posts" collection with the modified savedByUsers map
        postRef.update("savedByUsers", savedByUsers)
                .addOnSuccessListener(aVoid -> Log.d("Post Saved", "Post saved state updated successfully"))
                .addOnFailureListener(e -> Log.e("Post not Saved", "Error updating post saved state: " + e.getMessage(), e));
    }



}

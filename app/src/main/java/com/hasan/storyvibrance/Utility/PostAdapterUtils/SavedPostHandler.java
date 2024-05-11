package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.Model.PostSavedModel;
import com.hasan.storyvibrance.R;

import java.util.HashMap;
import java.util.Map;

public class SavedPostHandler {
    private final FirebaseFirestore db;

    public SavedPostHandler(FirebaseFirestore db) {
        this.db = db;
    }

    public void handlePostSaved(PostModel post, FirebaseUser user, ImageView savedIcon) {
        String currentUser = user != null ? user.getUid() : null;
        boolean isPostSaved = post.isPostSavedByUser(currentUser);

        // Toggle the saved state
        boolean newSavedState = !isPostSaved;

        // Update UI immediately
        savedIcon.setImageResource(newSavedState ? R.drawable.post_saved : R.drawable.post_save);

        // Update Firestore document asynchronously
        Map<String, Object> updates = new HashMap<>();
        if (newSavedState) {
            // Add the post to the saved list
            PostSavedModel postSavedModel = new PostSavedModel(currentUser);
            post.addSaved(postSavedModel);
            updates.put("postSaved", post.getPostSaved());
        } else {
            // Remove the post from the saved list
            post.removeSavePost(currentUser);
            updates.put("postSaved", post.getPostSaved());
        }

        db.collection("posts").document(post.getPostId()).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("Post saved", "Post saved state updated successfully"))
                .addOnFailureListener(e -> {
                    Log.e("Post saved Error", "Error updating post saved state: " + e.getMessage(), e);
                    // Revert UI changes if necessary
                    savedIcon.setImageResource(isPostSaved ? R.drawable.post_saved : R.drawable.post_save);
                    if (newSavedState) {
                        post.removeSavePost(currentUser);
                    } else {
                        PostSavedModel postSavedModel = new PostSavedModel(currentUser);
                        post.addSaved(postSavedModel);
                    }
                });
    }
}

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

        if (!isPostSaved) {
            savedIcon.setImageResource(R.drawable.post_saved);
            PostSavedModel postSavedModel = new PostSavedModel(currentUser);
            post.addSaved(postSavedModel);
        } else {
            savedIcon.setImageResource(R.drawable.post_save);
            post.removeSavePost(currentUser);
        }

        // Update the Firestore document to reflect the changes in the saved state
        Map<String, Object> updates2 = new HashMap<>();
        updates2.put("postSaved", post.getPostSaved());
        db.collection("posts").document(post.getPostId()).update(updates2)
                .addOnSuccessListener(aVoid -> Log.d("Post saved", "Post saved state updated successfully"))
                .addOnFailureListener(e -> {
                    Log.e("Post saved Error", "Error updating post saved state: " + e.getMessage(), e);
                    // Revert UI changes if necessary
                    if (!isPostSaved) {
                        savedIcon.setImageResource(R.drawable.post_save);
                        post.removeSavePost(currentUser);
                    } else {
                        savedIcon.setImageResource(R.drawable.post_saved);
                        PostSavedModel postSavedModel = new PostSavedModel(currentUser);
                        post.addSaved(postSavedModel);
                    }
                });
    }



}

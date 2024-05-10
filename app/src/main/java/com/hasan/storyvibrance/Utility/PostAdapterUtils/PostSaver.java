package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;

public class PostSaver {


    public static void handleSavePost(PostModel post, boolean newSavedState, ImageView savedIcon) {
        if (newSavedState) {
            savedIcon.setImageResource(R.drawable.post_saved);
        } else {
            savedIcon.setImageResource(R.drawable.post_save);
        }

        // Save or remove the post as "saved" in the database
        saveOrRemovePostAsSaved(post, newSavedState);

        // Update the post's saved state
        post.setSaved(newSavedState);
    }

    /**
     * Helper function to save or remove the post as saved in Firestore.
     *
     * @param post  The PostModel object representing the post to be saved or removed.
     * @param saved A boolean indicating whether the post should be saved (true) or removed (false).
     */
    private static void saveOrRemovePostAsSaved(PostModel post, boolean saved) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference postRef = db.collection("posts").document(post.getPostId());

        // Update the "saved" field of the post in the database
        postRef.update("saved", saved)
                .addOnSuccessListener(aVoid -> Log.d("Post Saved", "Post saved state updated successfully"))
                .addOnFailureListener(e -> Log.e("Post not Saved", "Error updating post saved state: " + e.getMessage(), e));
    }
}

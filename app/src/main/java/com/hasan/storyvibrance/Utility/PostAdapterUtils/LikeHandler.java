package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.LikeModel;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;

import java.util.HashMap;
import java.util.Map;

public class LikeHandler {
    private final FirebaseFirestore db;

    public LikeHandler(FirebaseFirestore db) {
        this.db = db;
    }

    public void handleLike(PostModel post, FirebaseUser user, ImageView likeIcon, TextView likeCount) {
        String currentUser = user != null ? user.getUid() : null;
        boolean isLiked = post.isLikedByUser(currentUser);
        if (!isLiked) {
            likeIcon.setImageResource(R.drawable.post_liked);
            LikeModel like = new LikeModel(currentUser);
            post.addLike(like);
        } else {
            likeIcon.setImageResource(R.drawable.post_like);
            post.removeLike(currentUser);
        }

        // Update like count in UI immediately
        likeCount.setText(String.valueOf(post.getLikes().size()));

        // Update Firestore
        Map<String, Object> updates = new HashMap<>();
        updates.put("likes", post.getLikes());

        db.collection("posts").document(post.getPostId()).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("Likes done", "Likes updated"))
                .addOnFailureListener(e -> {
                    Log.e("likes Error", "Error updating likes", e);
                    // Revert UI changes if necessary
                    if (!isLiked) {
                        likeIcon.setImageResource(R.drawable.post_like);
                        post.removeLike(currentUser);
                    } else {
                        likeIcon.setImageResource(R.drawable.post_liked);
                        LikeModel like = new LikeModel(currentUser);
                        post.addLike(like);
                    }
                    // Update like count in UI
                    likeCount.setText(String.valueOf(post.getLikes().size()));
                });
    }
}

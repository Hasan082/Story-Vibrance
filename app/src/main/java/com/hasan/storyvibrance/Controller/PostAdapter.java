package com.hasan.storyvibrance.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.LikeModel;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private final Context context;
    private final ArrayList<PostModel> postModels;

    public PostAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_post_card, parent, false);
        return new PostHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        PostModel post = postModels.get(position);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Populate post data directly from the PostModel object
        holder.postTextContent.setText(post.getPostTextContent());
        // Load post media
        Picasso.get().load(post.getPostMedia()).into(holder.postMedia);
        // Set timestamp
        String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(post.getTimestamp()));
        holder.timeStamp.setText(timeAgo);

        // Fetch user data for the author of this post

        db.collection("userdata").document(post.getAuthorUsername()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve user data from the document snapshot
                        String fullName = documentSnapshot.getString("name");
                        String profileImg = documentSnapshot.getString("ProfileImg");

                        // Populate author name and profile image
                        holder.authorName.setText(fullName);
                        Picasso.get().load(profileImg).into(holder.authorImg);
                    } else {
                        Log.d("Firestore", "User document not found for post author");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user document: " + e.getMessage(), e));










        // Populate other post details like author username, like count, comment count, etc.
        holder.authorUsername.setText(post.getAuthorUsername());
        if (post.getLikes() != null && !post.getLikes().isEmpty()) {
            holder.likeCount.setText(String.valueOf(post.getLikes().size()));
        } else {
            holder.likeCount.setText("0");
        }
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            holder.commentCount.setText(String.valueOf(post.getComments().size()));
        } else {
            holder.commentCount.setText("Be first?");
        }

        //CONTROL LIKE =====================================

        // Fetch current user id
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String currentUser = user.getUid();

        // Get the likes list for the current post
        List<LikeModel> likes = post.getLikes();
        if (likes == null) {
            likes = new ArrayList<>();
        }

        if (post.isLikedByUser(currentUser)) {
            holder.likeIcon.setImageResource(R.drawable.post_liked);
        } else {
            holder.likeIcon.setImageResource(R.drawable.post_like);
        }



        final List<LikeModel> finalLikes = likes; // Final reference to the likes list

        //LIKE CONTROLLER==========
        holder.likeIcon.setOnClickListener(v -> {
            // Toggle the like state immediately
            boolean isLiked = post.isLikedByUser(currentUser);
            if (!isLiked) {
                holder.likeIcon.setImageResource(R.drawable.post_liked);
                // Add like locally
                LikeModel like = new LikeModel(currentUser);
                post.addLike(like);
            } else {
                holder.likeIcon.setImageResource(R.drawable.post_like);
                // Remove like locally
                post.removeLike(currentUser);
            }

            // Update like count in UI immediately
            holder.likeCount.setText(String.valueOf(post.getLikes().size()));

            // Update Firestore
            Map<String, Object> updates = new HashMap<>();
            updates.put("likes", post.getLikes());
            db.collection("posts").document(post.getPostId()).update(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Log success message
                        Log.d("Firestore", "Likes updated");
                        notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure if necessary
                        Log.w("Firestore", "Error updating likes", e);
                        // Revert UI changes if necessary
                        if (!isLiked) {
                            holder.likeIcon.setImageResource(R.drawable.post_like);
                            // Remove like locally
                            post.removeLike(currentUser);
                        } else {
                            holder.likeIcon.setImageResource(R.drawable.post_liked);
                            // Add like locally
                            LikeModel like = new LikeModel(currentUser);
                            post.addLike(like);
                        }
                        // Update like count in UI
                        holder.likeCount.setText(String.valueOf(post.getLikes().size()));
                        // Notify the adapter that only this item has changed
                        notifyItemChanged(position);
                    });
        });







    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        // ViewHolder components here
        ImageView authorImg, postMedia, likeIcon;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount, timeStamp;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize ViewHolder components here
            likeIcon = itemView.findViewById(R.id.likeIcon);
            authorImg = itemView.findViewById(R.id.authorImg);
            postMedia = itemView.findViewById(R.id.postMedia);
            authorName = itemView.findViewById(R.id.authorName);
            authorUsername = itemView.findViewById(R.id.authorUsername);
            postTextContent = itemView.findViewById(R.id.postTextContent);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }
    }
}
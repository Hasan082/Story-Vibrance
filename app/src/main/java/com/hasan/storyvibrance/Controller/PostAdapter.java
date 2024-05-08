package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.hasan.storyvibrance.Model.LikeModel;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.Model.UserDataModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private final Context context;
    private final ArrayList<PostModel> postModels;
    private final Map<String, UserDataModel> userDataCache;

    public PostAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
        this.userDataCache = new HashMap<>();
    }

    public void updateData(List<PostModel> newPostModels) {
        postModels.clear();
        postModels.addAll(newPostModels);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_post_card, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        PostModel post = postModels.get(position);
        holder.bind(post, userDataCache);


        // Set initial saved state and icon
        if (post.isSaved()) {
            holder.savedIcon.setImageResource(R.drawable.post_saved);
        } else {
            holder.savedIcon.setImageResource(R.drawable.post_save);
        }

        // Set click listener for save icon
        holder.savedIcon.setOnClickListener(v -> {
            // Toggle saved state
            boolean isSaved = post.isSaved();
            boolean newSavedState = !isSaved;

            // Update UI to reflect saved state immediately
            if (newSavedState) {
                holder.savedIcon.setImageResource(R.drawable.post_saved);
            } else {
                holder.savedIcon.setImageResource(R.drawable.post_save);
            }

            // Save post locally (e.g., in SharedPreferences) or remove it
            if (newSavedState) {
                savePostLocally(post);
            } else {
                removeSavedPost(post);
            }

            // Update the post's saved state
            post.setSaved(newSavedState);

        });


    }

    // Method to save post locally
    private void savePostLocally(PostModel post) {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts", Context.MODE_PRIVATE);
        // Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Serialize the post object to JSON string
        Gson gson = new Gson();
        String postJson = gson.toJson(post);
        // Save post JSON string with unique key (e.g., post ID)
        editor.putString(post.getPostId(), postJson);
        // Commit changes
        editor.apply();
    }

    // Method to remove saved post
    private void removeSavedPost(PostModel post) {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts", Context.MODE_PRIVATE);
        // Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Remove post from SharedPreferences using its unique key (e.g., post ID)
        editor.remove(post.getPostId());
        // Commit changes
        editor.apply();
    }



    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        // ViewHolder components here
        ImageView authorImg, postMedia, likeIcon, savedIcon;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount, timeStamp;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize ViewHolder components here
            savedIcon = itemView.findViewById(R.id.savedIcon);
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

        public void bind(PostModel post, Map<String, UserDataModel> userDataCache) {
            // Populate post data directly from the PostModel object
            postTextContent.setText(post.getPostTextContent());
            // Load post media
            Picasso.get().load(post.getPostMedia()).into(postMedia);
            // Set timestamp
            String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(post.getTimestamp()));
            timeStamp.setText(timeAgo);

            // Fetch user data for the author of the post
            String authorUserId = post.getAuthorUsername();
            UserDataModel authorData = userDataCache.get(authorUserId);
            if (authorData != null) {
                // Author data is available in the cache, populate UI from cache
                authorName.setText(authorData.getFullName());
                Picasso.get().load(authorData.getProfileImg()).into(authorImg);
            } else {
                // Author data is not available in the cache, fetch from Firestore
                FirebaseFirestore.getInstance().collection("userdata").document(authorUserId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // Retrieve user data from the document snapshot
                                String fullName = documentSnapshot.getString("name");
                                String profileImg = documentSnapshot.getString("ProfileImg");

                                // Populate author name and profile image
                                authorName.setText(fullName);
                                Picasso.get().load(profileImg).into(authorImg);

                                // Update the cache with the fetched user data
                                userDataCache.put(authorUserId, new UserDataModel(fullName, profileImg));
                            } else {
                                Log.d("Firestore", "User document not found for post author");
                            }
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error fetching user document: " + e.getMessage(), e));
            }

            // Populate other post details like author username, like count, comment count, etc.
            authorUsername.setText(post.getAuthorUsername());
            likeCount.setText(String.valueOf(post.getLikes() != null ? post.getLikes().size() : 0));
            commentCount.setText(String.valueOf(post.getComments() != null ? post.getComments().size() : 0));

            // Set the initial like button state
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String currentUser = user.getUid();
                likeIcon.setImageResource(post.isLikedByUser(currentUser) ? R.drawable.post_liked : R.drawable.post_like);
            }

            // Set the click listener for the like button
            likeIcon.setOnClickListener(v -> {
                // Toggle the like state immediately
                if (user != null) {
                    String currentUser = user.getUid();
                    boolean isLiked = post.isLikedByUser(currentUser);
                    if (!isLiked) {
                        likeIcon.setImageResource(R.drawable.post_liked);
                        // Add like locally
                        LikeModel like = new LikeModel(currentUser);
                        post.addLike(like);
                    } else {
                        likeIcon.setImageResource(R.drawable.post_like);
                        // Remove like locally
                        post.removeLike(currentUser);
                    }

                    // Update like count in UI immediately
                    likeCount.setText(String.valueOf(post.getLikes().size()));

                    // Update Firestore
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("likes", post.getLikes());
                    FirebaseFirestore.getInstance().collection("posts").document(post.getPostId()).update(updates)
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Likes updated"))
                            .addOnFailureListener(e -> {
                                // Handle failure if necessary
                                Log.e("Firestore", "Error updating likes", e);
                                // Revert UI changes if necessary
                                notifyItemChanged(getAdapterPosition());
                                if (!isLiked) {
                                    likeIcon.setImageResource(R.drawable.post_like);
                                    // Remove like locally
                                    post.removeLike(currentUser);
                                } else {
                                    likeIcon.setImageResource(R.drawable.post_liked);
                                    // Add like locally
                                    LikeModel like = new LikeModel(currentUser);
                                    post.addLike(like);
                                }
                                // Update like count in UI
                                likeCount.setText(String.valueOf(post.getLikes().size()));
                            });
                }
            });
        }
    }
}

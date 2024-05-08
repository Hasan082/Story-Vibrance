package com.hasan.storyvibrance.Controller;

import android.content.Context;
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
import com.hasan.storyvibrance.Utility.UserDataLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        UserDataLoader.loadUserData(userId, holder.authorName, holder.authorImg);


        // Check if the post is saved locally
        boolean dataSaved = isPostSavedLocally(post.getPostId());
        System.out.println("isIdSaved " + dataSaved + " " + post.getPostId());
        // Set the icon based on the saved state
        if (dataSaved) {
            holder.savedIcon.setImageResource(R.drawable.post_saved);
        } else {
            holder.savedIcon.setImageResource(R.drawable.post_save);
        }
        //ToDo====================
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
                savePostLocally(post, userId);
            } else {
                removeSavedPost(post, userId);
            }

            // Update the post's saved state
            post.setSaved(newSavedState);
        });


    }

    /**
     * Saves the given post locally using SharedPreferences.
     * @param post The PostModel object to be saved.
     */
    private void savePostLocally(PostModel post, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts_" + userId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String postJson = gson.toJson(post);
        // Log the saved JSON string
        Log.d("SavedPostJson", postJson);
        // Save post JSON string with unique key (e.g., post ID)
        editor.putString(post.getPostId(), postJson);
        editor.apply();
    }



    /**
     * Removes the saved post locally using SharedPreferences.
     * @param post The PostModel object to be removed.
     */
    private void removeSavedPost(PostModel post, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts" + userId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(post.getPostId());
        editor.apply();
    }

    /**
     * Checks if the post with the given postId is saved locally in SharedPreferences.
     * @param postId The ID of the post to check.
     * @return true if the post is saved locally, false otherwise.
     */
    private boolean isPostSavedLocally(String postId) {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts", Context.MODE_PRIVATE);
        // Check if the post with the given postId exists in SharedPreferences
        boolean isSaved = sharedPreferences.contains(postId);

        // Print out the contents of SharedPreferences for debugging
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("SharedPreferences", "Key: " + entry.getKey() + ", Value: " + entry.getValue().toString());
        }

        return isSaved;
    }

    // Method to check if the current user has saved posts
    private boolean hasSavedPostsLocally(String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedPosts_" + userId, Context.MODE_PRIVATE);
        return !sharedPreferences.getAll().isEmpty();
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
          ImageView authorImg, postMedia, likeIcon, savedIcon;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount, timeStamp;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
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

        /**
         * Binds the post data to the ViewHolder.
         * @param post The PostModel object containing post data.
         * @param userDataCache The cache containing user data.
         */
        public void bind(PostModel post, Map<String, UserDataModel> userDataCache) {
            // Populate post data directly from the PostModel object
            postTextContent.setText(post.getPostTextContent());
            Picasso.get().load(post.getPostMedia()).into(postMedia);
            // Set timestamp
            String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(post.getTimestamp()));
            timeStamp.setText(timeAgo);

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
                if (user != null) {
                    String currentUser = user.getUid();
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
                    FirebaseFirestore.getInstance().collection("posts").document(post.getPostId()).update(updates)
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Likes updated"))
                            .addOnFailureListener(e -> {
                                Log.e("likes Error", "Error updating likes", e);
                                // Revert UI changes if necessary
                                notifyItemChanged(getAdapterPosition());
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
            });
        }
    }
}

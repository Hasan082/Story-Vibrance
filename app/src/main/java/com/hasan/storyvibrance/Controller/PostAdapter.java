package com.hasan.storyvibrance.Controller;

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
import java.util.List;
import java.util.Map;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private final Context context;
    private final ArrayList<PostModel> postModels;

    FirebaseFirestore db;


    public PostAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
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
        db = FirebaseFirestore.getInstance();

        // Populate post data directly from the PostModel object
        holder.authorName.setText(post.getAuthorName());
        holder.postTextContent.setText(post.getPostTextContent());
        Picasso.get().load(post.getPostMedia()).into(holder.postMedia);
        Picasso.get().load(post.getAuthorImg()).into(holder.authorImg);
        // Set timestamp
        String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(post.getTimestamp()));
        holder.timeStamp.setText(timeAgo);

        holder.likeCount.setText(String.valueOf(post.getLikes() != null ? post.getLikes().size() : 0));
        holder.commentCount.setText(String.valueOf(post.getComments() != null ? post.getComments().size() : 0));


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid() : null;
        holder.likeIcon.setImageResource(post.isLikedByUser(userId) ? R.drawable.post_liked : R.drawable.post_like);

        // HANDLE LIKE UNLIKE ACTIVITIES===========================
        holder.likeIcon.setOnClickListener(v -> {
            String currentUser = user != null ? user.getUid() : null;
            boolean isLiked = post.isLikedByUser(currentUser);
            if (!isLiked) {
                holder.likeIcon.setImageResource(R.drawable.post_liked);
                LikeModel like = new LikeModel(currentUser);
                post.addLike(like);
            } else {
                holder.likeIcon.setImageResource(R.drawable.post_like);
                post.removeLike(currentUser);
            }

            // Update like count in UI immediately
            holder.likeCount.setText(String.valueOf(post.getLikes().size()));

            // Update Firestore
            Map<String, Object> updates = new HashMap<>();
            updates.put("likes", post.getLikes());
            db.collection("posts").document(post.getPostId()).update(updates)
                    .addOnSuccessListener(aVoid -> Log.d("Likes done", "Likes updated"))
                    .addOnFailureListener(e -> {
                        Log.e("likes Error", "Error updating likes", e);
                        // Revert UI changes if necessary
                        if (!isLiked) {
                            holder.likeIcon.setImageResource(R.drawable.post_like);
                            post.removeLike(currentUser);
                        } else {
                            holder.likeIcon.setImageResource(R.drawable.post_liked);
                            LikeModel like = new LikeModel(currentUser);
                            post.addLike(like);
                        }
                        // Update like count in UI
                        holder.likeCount.setText(String.valueOf(post.getLikes().size()));
                    });
        });



        // HANDLE POST SAVED==============================================
        // Set the saved icon based on the current saved state of the post
        holder.savedIcon.setImageResource(post.isSaved() ? R.drawable.post_saved : R.drawable.post_save);

        // Set click listener for the saved icon
        holder.savedIcon.setOnClickListener(v -> {
            boolean isSaved = post.isSaved();
            boolean newSavedState = !isSaved;

            if (newSavedState) holder.savedIcon.setImageResource(R.drawable.post_saved);
            else holder.savedIcon.setImageResource(R.drawable.post_save);
            // Save or remove the post as "saved" in the database
            saveOrRemovePostAsSaved(post, newSavedState);
            // Update the post's saved state
            post.setSaved(newSavedState);
        });




    }



    /**
     * Helper function to save or remove the post as saved in Firestore.
     *
     * @param post  The PostModel object representing the post to be saved or removed.
     * @param saved A boolean indicating whether the post should be saved (true) or removed (false).
     */
    private void saveOrRemovePostAsSaved(PostModel post, boolean saved) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference postRef = db.collection("posts").document(post.getPostId());

        // Update the "saved" field of the post in the database
        postRef.update("saved", saved)
                .addOnSuccessListener(aVoid -> Log.d("Post Saved", "Post saved state updated successfully"))
                .addOnFailureListener(e -> Log.e("Post not Saved", "Error updating post saved state: " + e.getMessage(), e));
    }


    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
          ImageView authorImg, postMedia, likeIcon, savedIcon;
        TextView authorName, postTextContent, likeCount, commentCount, timeStamp;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            savedIcon = itemView.findViewById(R.id.savedIcon);
            likeIcon = itemView.findViewById(R.id.likeIcon);
            authorImg = itemView.findViewById(R.id.authorImg);
            postMedia = itemView.findViewById(R.id.postMedia);
            authorName = itemView.findViewById(R.id.authorName);
            postTextContent = itemView.findViewById(R.id.postTextContent);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }

    }
}

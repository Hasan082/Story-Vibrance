package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.Posts.EditPostActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.DialogUtils;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.DeletePostUtils;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.LikeHandler;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.PostSaver;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.UserDataFetcher;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.hasan.storyvibrance.Utility.dpToPx;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


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
        //User Data Fetcher for set user details for post
        String postAuthor = post.getAuthorUsername();
        UserDataFetcher userDataFetcher = new UserDataFetcher(db);
        userDataFetcher.fetchUserData(postAuthor, holder.authorName, holder.authorImg);

        // Populate post data directly from the PostModel object
        holder.postTextContent.setText(post.getPostTextContent());
        holder.authorUsername.setText(post.getAuthorUsername());
        Picasso.get().load(post.getPostMedia()).into(holder.postMedia);
        // Set timestamp
        String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(post.getTimestamp()));
        holder.timeStamp.setText(timeAgo);

        holder.likeCount.setText(String.valueOf(post.getLikes() != null ? post.getLikes().size() : 0));
        holder.commentCount.setText(String.valueOf(post.getComments() != null ? post.getComments().size() : 0));


        // HANDLE LIKE UNLIKE ACTIVITIES===========================
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid() : null;
        // Set the Like icon based on the current Like state of the post
        holder.likeIcon.setImageResource(post.isLikedByUser(userId) ? R.drawable.post_liked : R.drawable.post_like);
        // Post Like click Listener
        holder.likeIcon.setOnClickListener(v -> {
            LikeHandler likeHandler = new LikeHandler(db);
            likeHandler.handleLike(post, user, holder.likeIcon, holder.likeCount);
        });


        // HANDLE POST SAVED==========================================================
        // Check if the current user has saved the post
        String username = GetUserName.getUsernameFromSharedPreferences(context);
        boolean isSavedByCurrentUser = post.getSavedByUsers().containsKey(username);

        // Set the saved icon based on the current saved state of the post
        holder.savedIcon.setImageResource(isSavedByCurrentUser ? R.drawable.post_saved : R.drawable.post_save);

        // Post Save click Listener
        holder.savedIcon.setOnClickListener(v -> {
            boolean newSavedState = !isSavedByCurrentUser;
            PostSaver.handleSavePost(post, newSavedState, holder.savedIcon, username);
        });



        // UPDATE AND DELETE POST HANDLER=========================================
        String currentUser = GetUserName.getUsernameFromSharedPreferences(context);
        // Check if the current user is the owner of the post
        if (userId != null && currentUser.equals(post.getAuthorUsername())) {
            // Show the options icon and set click listener
            holder.optionsIcon.setVisibility(View.VISIBLE);
        } else {
            // Hide the options icon if the current user is not the owner of the post
            holder.optionsIcon.setVisibility(View.GONE);
        }

        holder.optionsIcon.setOnClickListener(v -> {
            // Get the screen coordinates of the options icon
            int[] location = new int[2];
            holder.optionsIcon.getLocationOnScreen(location);

            // Calculate the position for the popup window
            int marginInPx = dpToPx.dpToPx(110, context);
            int x = location[0] - marginInPx;
            int y = location[1] + holder.optionsIcon.getHeight();

            // Inflate the layout for the popup window
            View popupView = LayoutInflater.from(context).inflate(R.layout.popup_options_layout, (ViewGroup) v.getParent(), false);

            // Create the popup window
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            // Set properties for the popup window
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setElevation(10);
            popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            popupWindow.setFocusable(true);

            // Set click listeners for edit and delete options
            TextView editPost = popupView.findViewById(R.id.edit_post);
            TextView deletePost = popupView.findViewById(R.id.delete_post);
            //Edit post click handler
            editPost.setOnClickListener(view -> {
                Intent intent = new Intent(context, EditPostActivity.class);
                intent.putExtra("postId", post.getPostId());
                context.startActivity(intent);
                popupWindow.dismiss();
            });
            //Delete Post click handler
            deletePost.setOnClickListener(view -> {
                // Handle delete option click
                DialogUtils.showConfirmationDialog(context, "Confirmation for Deletion",
                        "Are you sure you want to delete this post?",
                        "Confirm", "No",R.drawable.alert,
                        (dialogInterface, i) -> {
                            // Delete the post from Firebase and dismiss the popup window
                            DeletePostUtils.deletePostFromFirebase(post.getPostId());
                            popupWindow.dismiss();
                        },
                        (dialogInterface, i) -> {
                            popupWindow.dismiss();
                            dialogInterface.dismiss();
                        });
            });

            // Show the popup window at the specified position
            popupWindow.showAtLocation(v, Gravity.TOP | Gravity.START, x, y);
        });




    }


    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        ImageView authorImg, postMedia, likeIcon, savedIcon, optionsIcon;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount, timeStamp;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            optionsIcon = itemView.findViewById(R.id.optionIcon);
            authorUsername = itemView.findViewById(R.id.authorUsername);
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

package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.Posts.EditPostActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.DialogUtils;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.CommentBottomSheetDialog;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.DeletePostUtils;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.LikeHandler;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.SavedPostHandler;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.UserDataFetcher;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.hasan.storyvibrance.Utility.dpToPx;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private FragmentManager fragmentManager;
    private final Context context;
    private final ArrayList<PostModel> postModels;

    FirebaseFirestore db;

    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;



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
        userDataFetcher.fetchUserData(postAuthor, (name, profileImg) -> {
            String fullName = NameCapitalize.capitalize(name);
            holder.authorName.setText(fullName);
            Picasso.get().load(profileImg).into(holder.authorImg);
        });

        // Populate post data directly from the PostModel object
        holder.postTextContent.setText(post.getPostTextContent());
        holder.authorUsername.setText(post.getAuthorUsername());
        Picasso.get().load(post.getPostMedia()).into(holder.postMedia);
        // Set timestamp
        String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(post.getTimestamp()));
        holder.timeStamp.setText(timeAgo);

        holder.likeCount.setText(String.valueOf(post.getLikes() != null ? post.getLikes().size() : 0));



        //Fetch the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user != null ? user.getUid() : null;

        // HANDLE LIKE UNLIKE ACTIVITIES===========================
        // Set the Like icon based on the current Like state of the post
        holder.likeIcon.setImageResource(post.isLikedByUser(userId) ? R.drawable.post_liked : R.drawable.post_like);
        // Post Like click Listener
        holder.likeIcon.setOnClickListener(v -> {
            LikeHandler likeHandler = new LikeHandler(db);
            likeHandler.handleLike(post, user, holder.likeIcon, holder.likeCount);
        });


        // HANDLE POST SAVED==========================================================
        holder.savedIcon.setImageResource(post.isPostSavedByUser(userId) ? R.drawable.post_saved : R.drawable.post_save);
        holder.savedIcon.setOnClickListener(v-> {
            SavedPostHandler savedPostHandler = new SavedPostHandler(db);
            savedPostHandler.handlePostSaved(post, user, holder.savedIcon);
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

        //Open a bottom Sheet when open click on comment Icon
        // Create a Firestore query for comments of the specific post
        Query commentsQuery = db.collection("comments")
                .whereEqualTo("postId", post.getPostId());

        // Create a listener registration
        ListenerRegistration registration = commentsQuery.addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                Log.e("Comment count", "Error getting comments: " + error);
                return;
            }

            if (querySnapshot != null) {
                // Get the count of comments
                int commentCount = querySnapshot.size();
                Log.d("Comment count by post", "onBindViewHolder: "+ post.getPostId()+ ":" + commentCount);
                holder.commentCount.setText(String.valueOf(commentCount));
            }
        });

        // Store the registration in the holder for later cleanup (e.g., in onViewRecycled)
        holder.commentCountListenerRegistration = registration;




        holder.commentIcon.setOnClickListener(v -> {
            // Create an instance of CommentBottomSheetDialog
            CommentBottomSheetDialog bottomSheetDialog = new CommentBottomSheetDialog();
            // Pass the post ID to the CommentBottomSheetDialog
            Bundle args = new Bundle();
            args.putString("postId", post.getPostId());
            bottomSheetDialog.setArguments(args);
            // Show the bottom sheet dialog
            bottomSheetDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), bottomSheetDialog.getTag());

        });



        //Edit and Delete==========
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


    public void fetchCommentsForPost(String postId, OnSuccessListener<QuerySnapshot> successListener, OnFailureListener failureListener) {
        db.collection("comments")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        ImageView authorImg, postMedia, likeIcon, savedIcon, optionsIcon, commentIcon;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount, timeStamp;
        ListenerRegistration commentCountListenerRegistration;
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
            commentIcon = itemView.findViewById(R.id.commentIcon);
        }

    }
}

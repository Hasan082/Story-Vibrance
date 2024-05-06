package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    Context context;
    ArrayList<PostModel> postModels;

    public PostAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_post_card, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        PostModel post = postModels.get(position);


        db.collection("userdata").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", "Listen failed.", error);
                return;
            }
            // Check if the query snapshot contains any documents
            if (value != null && !value.isEmpty()) {
                for (QueryDocumentSnapshot doc : value) {
                    Map<String, Object> data = doc.getData();
                    String fullName = (String) data.get("name");
                    String profileImg = (String) data.get("ProfileImg");
                    Picasso.get().load(profileImg).into(holder.authorImg);
                    holder.authorName.setText(fullName);
                }
            } else {
                Log.d("Firestore", "No documents found in the 'userdata' collection.");
            }
        });



        Picasso.get().load(postModels.get(position).getPostMedia()).into(holder.postMedia);
        holder.authorUsername.setText(postModels.get(position).getAuthorUsername());
        holder.postTextContent.setText(postModels.get(position).getPostTextContent());
        if (post.getLikes() != null && post.getLikes().size() != 0) holder.likeCount.setText(String.valueOf(post.getLikes().size()));
        else holder.likeCount.setText("0");
        if (post.getComments() != null && post.getComments().size() != 0) holder.commentCount.setText(String.valueOf(post.getComments().size()));
        else holder.commentCount.setText("Be first?");
        // Set timestamp
        String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(postModels.get(position).getTimestamp()));
        holder.timeStamp.setText(timeAgo);

        //LIKE CONTROLLER==========

        // Handle like button click

        holder.likeIcon.setOnClickListener(v -> {
            // Get the adapter position of the clicked item
            int pos = holder.getAdapterPosition();
            // Check if the position is valid
            if (pos != RecyclerView.NO_POSITION) {
                // Retrieve the PostModel associated with the clicked item
                PostModel clickedPost = postModels.get(pos);

                // Retrieve the post ID from the PostModel
                String postId = clickedPost.getPostId();

                // Log the post ID
                Log.d("Post Click", "Clicked like icon for post with ID: " + postId);

                // Example: Check if the post is already liked by the user
                // boolean isLiked = clickedPost.isLikedByUser(userId);
            } else {
                Log.e("Error", "Invalid position");
            }
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

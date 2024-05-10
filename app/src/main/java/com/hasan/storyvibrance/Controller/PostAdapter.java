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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hasan.storyvibrance.Model.LikeModel;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.DataBaseError;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.LikeHandler;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.PostSaver;
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

        String postAuthor = post.getAuthorUsername();
          db.collection("userdata").document(postAuthor).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        String profileImg = document.getString("ProfileImg");
                        holder.authorName.setText(name);
                        Picasso.get().load(profileImg).into(holder.authorImg);
                    } else {
                        Log.d("Userdata", "No such document");
                    }
                } else {
                    Log.d("Userdata", "get failed with ", task.getException());
                }
            }
        });


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
        // Set the saved icon based on the current saved state of the post
        holder.savedIcon.setImageResource(post.isSaved() ? R.drawable.post_saved : R.drawable.post_save);
        // Post Save click Listener
        holder.savedIcon.setOnClickListener(v -> {
            boolean isSaved = post.isSaved();
            boolean newSavedState = !isSaved;
            PostSaver.handleSavePost(post, newSavedState, holder.savedIcon);
        });

    }


    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
          ImageView authorImg, postMedia, likeIcon, savedIcon;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount, timeStamp;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
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

package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Model.PostSavedModel;
import com.hasan.storyvibrance.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedPostsAdapter extends RecyclerView.Adapter<SavedPostsAdapter.PostViewHolder>  {

    private final List<PostSavedModel> savedPosts;
    Context context;

    public PostSavedModel getPostAtPosition(int position) {
        if (position >= 0 && position < savedPosts.size()) {
            return savedPosts.get(position);
        }
        return null;
    }


    public SavedPostsAdapter(List<PostSavedModel> savedPosts, Context context) {
        this.savedPosts = savedPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostSavedModel post = savedPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return savedPosts.size();
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView savedPostImg;
        private final TextView savedPostTitle;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            savedPostImg = itemView.findViewById(R.id.savedPostImg);
            savedPostTitle = itemView.findViewById(R.id.savedPostTitle);
        }

        public void bind(PostSavedModel post) {
            // Bind post data to views
            Picasso.get().load(post.getPostMedia()).into(savedPostImg);
            savedPostTitle.setText(post.getPostContent());
        }


    }


}

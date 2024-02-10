package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;

import java.util.ArrayList;

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
        holder.authorImg.setImageResource(postModels.get(position).getAuthorProfileImage());
        holder.postMedia.setImageResource(postModels.get(position).getPostMediaContent());
        holder.authorName.setText(postModels.get(position).getAuthorName());
        holder.authorUsername.setText(postModels.get(position).getAuthorUsername());
        holder.postTextContent.setText(postModels.get(position).getPostTextContent());
        holder.likeCount.setText(String.valueOf(postModels.get(position).getLikeCount()));
        holder.commentCount.setText(String.valueOf(postModels.get(position).getCommentCount()));
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView authorImg, postMedia;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount;


        public PostHolder(@NonNull View itemView) {
            super(itemView);
            authorImg = itemView.findViewById(R.id.authorImg);
            postMedia = itemView.findViewById(R.id.postMedia);
            authorName = itemView.findViewById(R.id.authorName);
            authorUsername = itemView.findViewById(R.id.authorUsername);
            postTextContent = itemView.findViewById(R.id.postTextContent);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
        }
    }
}

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
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

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
//        PostModel postModel = postModels.get(position);

        Picasso.get().load(postModels.get(position).getPostMedia()).into(holder.authorImg);
        Picasso.get().load(postModels.get(position).getPostMedia()).into(holder.postMedia);
        holder.authorName.setText(postModels.get(position).getAuthorName());
        holder.authorUsername.setText(postModels.get(position).getAuthorUsername());
        holder.postTextContent.setText(postModels.get(position).getPostTextContent());
        holder.likeCount.setText(String.valueOf(postModels.get(position).getLikes()));
        holder.commentCount.setText(String.valueOf(postModels.get(position).getComments()));
        // Set timestamp
        String timeAgo = TimeUtils.getTimeAgo(postModels.get(position).getTimestamp());
        holder.timeStamp.setText(timeAgo);

    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        // ViewHolder components here
        ImageView authorImg, postMedia;
        TextView authorName, authorUsername, postTextContent, likeCount, commentCount, timeStamp;



        public PostHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize ViewHolder components here
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

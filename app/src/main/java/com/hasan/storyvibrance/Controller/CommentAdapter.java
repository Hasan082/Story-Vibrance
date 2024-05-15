package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.CommentModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.UserDataFetcher;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentModel> commentList;
    private Context context;
    FirebaseFirestore db;

    // Constructor
    public CommentAdapter(List<CommentModel> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    public void updateData(List<CommentModel> newCommentList) {
        commentList.clear();
        commentList.addAll(newCommentList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentModel comment = commentList.get(position);
        db = FirebaseFirestore.getInstance();
        holder.commentUserName.setText(comment.getUserId());
        holder.commentContent.setText(comment.getCommentText());
        String commentTime = TimeUtils.getTimeAgo(comment.getTimestamp());
        holder.commentTime.setText(commentTime);
        UserDataFetcher userDataFetcher = new UserDataFetcher(db);
        String username = GetUserName.getUsernameFromSharedPreferences(context);
        userDataFetcher.fetchUserData(username, (name, profileImg) -> {
            holder.commentUserName.setText(name);
            Picasso.get().load(profileImg).into(holder.commentProfilePic);
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUserName, commentContent, commentTime;
        ImageView commentProfilePic;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentProfilePic = itemView.findViewById(R.id.commentProfilePic);
            commentUserName = itemView.findViewById(R.id.commentUserName);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentTime = itemView.findViewById(R.id.commentTime);
        }
    }
}
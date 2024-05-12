package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Model.ProfilePostModel;
import com.hasan.storyvibrance.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.PostViewHolder> {
    private final ArrayList<ProfilePostModel> profilePostModel;
    private final Context context;

    public ProfilePostAdapter(ArrayList<ProfilePostModel> profilePostModel, Context context) {
        this.profilePostModel = profilePostModel;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_post_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ProfilePostModel post = profilePostModel.get(position);
        Picasso.get().load(post.getProfilePostMedia()).resize(300, 300).centerCrop().into(holder.profilePostMedia);
    }

    @Override
    public int getItemCount() {
        return profilePostModel.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePostMedia;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePostMedia = itemView.findViewById(R.id.profilePostMedia);
        }
    }
}

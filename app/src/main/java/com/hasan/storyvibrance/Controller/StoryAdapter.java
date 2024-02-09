package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Model.StoryModel;
import com.hasan.storyvibrance.R;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHolder> {
    private final Context context;
    ArrayList<StoryModel> storyModels;

    public StoryAdapter(Context context, ArrayList<StoryModel> storyModels) {
        this.context = context;
        this.storyModels = storyModels;
    }

    @NonNull
    @Override
    public StoryAdapter.StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feed_story_card, parent, false);
        return new StoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.StoryHolder holder, int position) {
        holder.username.setText(storyModels.get(position).getUsername());
        holder.profileImg.setImageResource(storyModels.get(position).getProfileIconResId());
        holder.storyImg.setImageResource(storyModels.get(position).getStoryImageResId());
    }

    @Override
    public int getItemCount() {
        return storyModels.size();
    }

    public class StoryHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profileImg;
        ImageView storyImg;
        public StoryHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userNameTxt);
            storyImg = itemView.findViewById(R.id.storyBgImg);
            profileImg = itemView.findViewById(R.id.userProfile_img);
        }
    }
}

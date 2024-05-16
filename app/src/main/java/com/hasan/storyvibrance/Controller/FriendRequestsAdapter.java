package com.hasan.storyvibrance.Controller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {

    private final List<FriendRequestModel> friendRequests;

    public FriendRequestsAdapter(List<FriendRequestModel> friendRequests) {
        this.friendRequests = friendRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendRequestModel friendRequest = friendRequests.get(position);
        holder.textViewFriendRequestName.setText(friendRequest.getSenderName());
        // Set click listeners for accept and delete buttons
        holder.friendAcceptBtn.setOnClickListener(v -> {
            // Handle accept button click
            // You can add your logic here
        });
        holder.friendDeleteBtn.setOnClickListener(v -> {
            // Handle delete button click
            // You can add your logic here
        });

        // Load profile image using Glide library
        Picasso.get().load(friendRequest.getSenderProfileImageUrl()).into(holder.friendRequestSenderImg);

    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friendRequestSenderImg;
        TextView textViewFriendRequestName;
        AppCompatButton friendAcceptBtn, friendDeleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendRequestSenderImg = itemView.findViewById(R.id.friendRequestSenderImg);
            textViewFriendRequestName = itemView.findViewById(R.id.textViewFriendRequestName);
            friendAcceptBtn = itemView.findViewById(R.id.friendAcceptBtn);
            friendDeleteBtn = itemView.findViewById(R.id.friendDeleteBtn);
        }
    }
}

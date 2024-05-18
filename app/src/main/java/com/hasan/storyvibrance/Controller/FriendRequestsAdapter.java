package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.UserDataFetcher;
import com.hasan.storyvibrance.Utility.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {

    private List<FriendRequestModel> friendRequests;

    FirebaseFirestore db;
    Context context;

    public FriendRequestsAdapter(Context context, List<FriendRequestModel> friendRequests) {
        this.friendRequests = friendRequests;
        this.context = context;
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
        db = FirebaseFirestore.getInstance();

        String currentUsername = GetUserName.getUsernameFromSharedPreferences(context);
        hasPendingFriendRequest(currentUsername, hasPendingRequest -> {

            if (hasPendingRequest) {
                String senderUserId = friendRequest.getSenderId();
                UserDataFetcher userDataFetcher = new UserDataFetcher(db);
                userDataFetcher.fetchUserData(senderUserId, (name, profileImg) -> {
                    String fullName = NameCapitalize.capitalize(name);
                    // Update the sender's name and profile image URL in the FriendRequestModel
                    friendRequest.setSenderName(fullName);
                    friendRequest.setSenderProfileImageUrl(profileImg);

                    // Update the UI with the updated data
                    holder.textViewFriendRequestName.setText(fullName);
                    Picasso.get().load(friendRequest.getSenderProfileImageUrl()).placeholder(R.drawable.edit_person)
                            .error(R.drawable.edit_person)
                            .into(holder.friendRequestSenderImg);
                });

                holder.textViewFriendRequestName.setText(friendRequest.getSenderName());
                Picasso.get().load(friendRequest.getSenderProfileImageUrl()).into(holder.friendRequestSenderImg);
                // Convert the long time to a string representation using TimeUtils
                Log.d("timeAgo", "onBindViewHolder: " + friendRequest.getTimestamp());
                String timeAgo = TimeUtils.getTimeAgo(Long.parseLong(String.valueOf(friendRequest.getTimestamp())));

                holder.friendRequestTime.setText(timeAgo);

                //Accept friend request================
                holder.friendAcceptBtn.setOnClickListener(v-> {
                    // Handle accept button click
                    updateFriendRequestStatus(friendRequest.getRequestId(), holder);
                });
                //delete friend request=================
                holder.friendDeleteBtn.setOnClickListener(v-> {
                    // Handle delete button click
                    deleteFriendRequest(friendRequest.getRequestId(), position);
                });
            }
        });


    }

    private void deleteFriendRequest(String requestId, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("friendRequests").document(requestId)
                .delete()
                .addOnSuccessListener(aVoid -> {
            // Successfully deleted friend request
            Toast.makeText(context, "Request deleted successfully", Toast.LENGTH_SHORT).show();
            Log.d("deleteRequest", "Friend request deleted successfully");
            removeItem(position);
        }).addOnFailureListener(e-> {
            // Failed to delete friend request
            Log.e("deleteRequest", "Error deleting friend request", e);
        });
    }

    private void removeItem(int position) {
        friendRequests.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, friendRequests.size());
    }



    private void updateFriendRequestStatus(String requestId, ViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "confirmed");
        updates.put("timestamp", System.currentTimeMillis());

        db.collection("friendRequests").document(requestId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Successfully updated status
                    Log.d("updateStatus", "Friend request status updated successfully");
                    Toast.makeText(context, "Friend added successfully", Toast.LENGTH_SHORT).show();
                    // Update the UI to show the acceptance message and hide the buttons
                    holder.requestBtnWrapper.setVisibility(View.GONE);
                    holder.friendAdded.setVisibility(View.VISIBLE);
                }).addOnFailureListener(e -> {
                    // Failed to update status
                    Log.e("updateStatus", "Error updating friend request status", e);
                });
    }

    //Pending friend request===
    private void hasPendingFriendRequest(String currentUserId, FriendCheckCallback callback) {

        if (db == null) {
            // Handle the case where FirebaseFirestore.getInstance() returns null
            Log.e("hasPendingFriendRequest", "FirebaseFirestore instance is null");
            callback.onFriendCheckResult(false);
            return;
        }

        // Query the "friendRequests" collection to check if there are any pending requests for the current user
        db.collection("friendRequests")
                .whereEqualTo("recipientId", currentUserId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If there are pending requests, notify the callback
                        boolean hasPendingRequest = !task.getResult().isEmpty();
                        callback.onFriendCheckResult(hasPendingRequest);
                        Log.e("hasPendingFriendRequest", "checking pending friend Success: " + hasPendingRequest);
                    } else {
                        // Log error or handle failure
                        Log.e("hasPendingFriendRequest", "Error checking pending friend requests", task.getException());
                        // Assume no pending request in case of error
                        callback.onFriendCheckResult(false);
                    }
                });
    }


    // Define a callback interface
    interface FriendCheckCallback {
        void onFriendCheckResult(boolean isFriend);
    }


    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public void updateFriendRequests(List<FriendRequestModel> friendRequests) {
        this.friendRequests = friendRequests;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friendRequestSenderImg;
        TextView textViewFriendRequestName, friendAdded, friendRequestTime;
        AppCompatButton friendAcceptBtn, friendDeleteBtn;

        LinearLayout requestBtnWrapper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendRequestSenderImg = itemView.findViewById(R.id.friendRequestSenderImg);
            textViewFriendRequestName = itemView.findViewById(R.id.textViewFriendRequestName);
            friendAcceptBtn = itemView.findViewById(R.id.friendAcceptBtn);
            friendDeleteBtn = itemView.findViewById(R.id.friendDeleteBtn);
            friendAdded = itemView.findViewById(R.id.friendAdded);
            requestBtnWrapper = itemView.findViewById(R.id.requestBtnWrapper);
            friendRequestTime = itemView.findViewById(R.id.friendRequestTime);
        }
    }
}

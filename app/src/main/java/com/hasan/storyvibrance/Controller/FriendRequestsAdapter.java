package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.hasan.storyvibrance.Utility.PostAdapterUtils.UserDataFetcher;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        hasPendingFriendRequest(currentUsername, new FriendCheckCallback() {
            @Override
            public void onFriendCheckResult(boolean hasPendingRequest) {

                if (hasPendingRequest) {
                    String senderUserId = friendRequest.getSenderId();
                    Log.d("senderUserId check", "onFriendCheckResult: " + senderUserId);
                    UserDataFetcher userDataFetcher = new UserDataFetcher(db);
                    userDataFetcher.fetchUserData(senderUserId, (name, profileImg) -> {
                        String fullName = NameCapitalize.capitalize(name);
                        Log.d("onFriendCheckResultname", "onFriendCheckResult: " + fullName);
                        Log.d("onFriendCheckResul profileImg", "onFriendCheckResult: " + profileImg);
                        // Update the sender's name and profile image URL in the FriendRequestModel
                        friendRequest.setSenderName(fullName);
                        friendRequest.setSenderProfileImageUrl(profileImg);

                        // Now you can update the UI with the updated data
                        holder.textViewFriendRequestName.setText(fullName);
                        Picasso.get().load(friendRequest.getSenderProfileImageUrl()).placeholder(R.drawable.edit_person)
                                .error(R.drawable.edit_person)
                                .into(holder.friendRequestSenderImg);
                    });

                    holder.textViewFriendRequestName.setText(friendRequest.getSenderName());
                    Picasso.get().load(friendRequest.getSenderProfileImageUrl()).into(holder.friendRequestSenderImg);

                } else {
                    holder.noFriendMessage.setVisibility(View.VISIBLE);
                    holder.recyclerViewFriendRequests.setVisibility(View.GONE);
                }
            }

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
        TextView textViewFriendRequestName, noFriendMessage;
        AppCompatButton friendAcceptBtn, friendDeleteBtn;
        RecyclerView recyclerViewFriendRequests;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendRequestSenderImg = itemView.findViewById(R.id.friendRequestSenderImg);
            textViewFriendRequestName = itemView.findViewById(R.id.textViewFriendRequestName);
            friendAcceptBtn = itemView.findViewById(R.id.friendAcceptBtn);
            friendDeleteBtn = itemView.findViewById(R.id.friendDeleteBtn);
            noFriendMessage = itemView.findViewById(R.id.noFriendMessage);
            recyclerViewFriendRequests = itemView.findViewById(R.id.recyclerViewFriendRequests);
        }
    }
}

package com.hasan.storyvibrance.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Model.FriendSearchModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.NameCapitalize;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.ViewHolder> {

    private List<FriendSearchModel> searchResults;
    Context context;

    public FriendSearchAdapter(Context context, List<FriendSearchModel> searchResults) {
        this.context = context;
        this.searchResults = searchResults;
    }

    public void setData(List<FriendSearchModel> newData) {
        searchResults.clear();
        searchResults.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_search_result_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendSearchModel friendSearchModel = searchResults.get(position);
        String name = friendSearchModel.getName();
        String capitalizedName = NameCapitalize.capitalize(name);
        holder.textViewUserName.setText(capitalizedName);

        // Load profile image with Picasso
        if (friendSearchModel.getProfileImg() != null && !friendSearchModel.getProfileImg().isEmpty()) {
            Picasso.get().load(friendSearchModel.getProfileImg())
                    .placeholder(R.drawable.edit_person)
                    .error(R.drawable.edit_person)
                    .into(holder.friendSearchImg);
        } else {
            holder.friendSearchImg.setImageResource(R.drawable.edit_person);
        }

        // Set click listener for "Send Request" button
        holder.buttonSendRequest.setOnClickListener(v -> {
            // Disable the button to prevent multiple requests
            holder.buttonSendRequest.setEnabled(false);
            String friendUserId = friendSearchModel.getEmail();
            sendFriendRequest(friendUserId, holder);
        });
    }

    private void sendFriendRequest(String friendUserId, ViewHolder holder) {
        // Get the current user's ID (you can obtain this from your authentication system)
        String currentUserId = GetUserName.getUsernameFromSharedPreferences(context);
        // Create a HashMap to store the friend request details
        Map<String, Object> friendRequestData = new HashMap<>();
        friendRequestData.put("senderId", currentUserId);
        friendRequestData.put("recipientId", friendUserId);
        friendRequestData.put("timestamp", System.currentTimeMillis());
        friendRequestData.put("status", "pending");
        // Add the friend request data to the "friendRequests" collection in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("friendRequests").add(friendRequestData)
                .addOnSuccessListener(documentReference -> {
                    // Friend request added successfully
                    Log.d("AddFriendActivity", "Friend request sent successfully!");
                    // Enable the button after request is sent
                    holder.buttonSendRequest.setEnabled(false);
                    holder.buttonSendRequest.setText("Requested");
                    // You can add further logic here, such as showing a success message to the user
                })
                .addOnFailureListener(e -> {
                    // Error occurred while adding the friend request
                    Log.e("AddFriendActivity", "Error sending friend request: " + e.getMessage());
                    // Enable the button after request is sent
                    holder.buttonSendRequest.setEnabled(true);
                    holder.buttonSendRequest.setText("Add Friend");
                    // You can handle the error here, such as showing an error message to the user
                });
    }




    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        ImageView friendSearchImg;
        
        Button buttonSendRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            friendSearchImg = itemView.findViewById(R.id.friendSearchImg);
            buttonSendRequest = itemView.findViewById(R.id.buttonSendRequest);
        }
    }
}


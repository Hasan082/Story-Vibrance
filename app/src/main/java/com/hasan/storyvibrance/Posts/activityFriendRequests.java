package com.hasan.storyvibrance.Posts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Controller.FriendRequestsAdapter;
import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;

import java.util.ArrayList;
import java.util.List;

public class activityFriendRequests extends AppCompatActivity {
    private RecyclerView recyclerViewFriendRequests;
    private FriendRequestsAdapter friendRequestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        recyclerViewFriendRequests = findViewById(R.id.recyclerViewFriendRequests);
        friendRequestsAdapter = new FriendRequestsAdapter(getFriendRequests()); // Replace getFriendRequests() with your method to fetch friend requests
        recyclerViewFriendRequests.setAdapter(friendRequestsAdapter);
    }

    // Method to fetch friend requests
    private List<FriendRequestModel> getFriendRequests() {
        // Replace this with your implementation to fetch friend requests from the database or other source
        List<FriendRequestModel> friendRequests = new ArrayList<>();
        // Add sample friend requests for testing
        friendRequests.add(new FriendRequestModel("requestId_1", "senderId_1", "Sender 1", "https://example.com/profile_image_1.jpg", System.currentTimeMillis()));
        friendRequests.add(new FriendRequestModel("requestId_2", "senderId_2", "Sender 2", "https://example.com/profile_image_2.jpg", System.currentTimeMillis()));
        friendRequests.add(new FriendRequestModel("requestId_3", "senderId_3", "Sender 3", "https://example.com/profile_image_3.jpg", System.currentTimeMillis()));


        return friendRequests;
    }
}
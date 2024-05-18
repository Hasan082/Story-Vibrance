package com.hasan.storyvibrance.Posts;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hasan.storyvibrance.Controller.FriendRequestsAdapter;
import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.FadeAnimator;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.databinding.ActivityFriendRequestsBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityFriendRequests extends AppCompatActivity {

    ActivityFriendRequestsBinding binding;
    private FriendRequestsAdapter friendRequestsAdapter;

    private FirebaseFirestore db;

    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_requests);

        // Find the ShimmerFrameLayout
        shimmerFrameLayout = binding.shimmerLayout;
        // Start shimmer animation
        shimmerFrameLayout.startShimmer();

        //return back to main page
        binding.backBtn.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        String recipientId = GetUserName.getUsernameFromSharedPreferences(this);
        // Find RecyclerView in layout
        RecyclerView recyclerViewFriendRequests = binding.recyclerViewFriendRequests;

        // Initialize adapter with empty list
        friendRequestsAdapter = new FriendRequestsAdapter(this, new ArrayList<>());
        recyclerViewFriendRequests.setAdapter(friendRequestsAdapter);

        // Fetch friend requests from Firestore and update RecyclerView
        fetchFriendRequests(recipientId);
    }

    // Method to fetch friend requests from Firestore
    private void fetchFriendRequests(String recipientId) {
        db.collection("friendRequests")
                .whereEqualTo("recipientId", recipientId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FriendRequestModel> friendRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert Firestore document to FriendRequestModel
                            Log.d("Friend Name", "onComplete: " + document.get("senderId"));
                            FriendRequestModel friendRequest = document.toObject(FriendRequestModel.class);
                            friendRequest.setRequestId(document.getId());
                            friendRequests.add(friendRequest);
                        }
                        // Update RecyclerView with fetched friend requests
                        friendRequestsAdapter.updateFriendRequests(friendRequests);

                        // Stop shimmer animation after data is loaded
                        new Handler().postDelayed(() -> {
                            // Show message if there are no friend requests
                            if (friendRequests.isEmpty()) {
                                binding.noPendingRequestMessage.setVisibility(View.VISIBLE);
                            } else {
                                binding.noPendingRequestMessage.setVisibility(View.GONE);
                            }
                            FadeAnimator.showElement(binding.recyclerViewFriendRequests);
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }, 1000);

                    } else {
                        // Log error or handle failure
                        Log.e("Friend Request", "Error getting friend requests", task.getException());
                    }



                });
    }






}

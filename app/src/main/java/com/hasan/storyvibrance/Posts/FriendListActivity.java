package com.hasan.storyvibrance.Posts;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hasan.storyvibrance.Controller.FriendListAdapter;
import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.FadeAnimator;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.databinding.ActivityFriendListBinding;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    ActivityFriendListBinding binding;
    private RecyclerView recyclerViewFriendRequests;
    private FriendListAdapter friendListAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView noFriendMessage;
    private FirebaseFirestore db;
    private String recipientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_list);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        binding.friendListRecyclerView.setVisibility(View.GONE);
        shimmerFrameLayout = binding.shimmerLayout;
        shimmerFrameLayout.startShimmer();
        //back to home
        binding.backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        //set adapter and pass data to recyclerview
        friendListAdapter = new FriendListAdapter(this, new ArrayList<>());
        binding.friendListRecyclerView.setAdapter(friendListAdapter);


        String recipientId = GetUserName.getUsernameFromSharedPreferences(this);

        fetchConfirmedFriendRequests(recipientId);
    }

    private void fetchConfirmedFriendRequests(String recipientId) {
        db.collection("friendRequests")
                .whereEqualTo("recipientId", recipientId)
                .whereEqualTo("status", "confirmed")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FriendRequestModel> friendRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert Firestore document to FriendRequestModel
                            FriendRequestModel friendRequest = document.toObject(FriendRequestModel.class);
                            friendRequest.setRequestId(document.getId());
                            friendRequests.add(friendRequest);
                        }
                        // Update RecyclerView with fetched friend requests
                        friendListAdapter.updateFriendRequests(friendRequests);

                        // Stop shimmer animation after data is loaded
                        new Handler().postDelayed(() -> {
                            if (friendRequests.isEmpty()) {
                                binding.noFriendMessage.setVisibility(View.VISIBLE);
                            } else {
                                binding.noFriendMessage.setVisibility(View.GONE);
                                binding.friendListRecyclerView.setVisibility(View.VISIBLE);
                                FadeAnimator.showElement(binding.friendListRecyclerView);
                            }
                            shimmerFrameLayout.setVisibility(View.GONE);
                        }, 1000);

                    } else {
                        // Log error or handle failure
                        Log.e("Friend Request", "Error getting friend requests", task.getException());
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shimmerFrameLayout.stopShimmer();
    }
}
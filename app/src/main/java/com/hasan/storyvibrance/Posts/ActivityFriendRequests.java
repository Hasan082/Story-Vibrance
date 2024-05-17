package com.hasan.storyvibrance.Posts;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hasan.storyvibrance.Controller.FriendRequestsAdapter;
import com.hasan.storyvibrance.Model.FriendRequestModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.databinding.ActivityFriendRequestsBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityFriendRequests extends AppCompatActivity {

    ActivityFriendRequestsBinding binding;
    private RecyclerView recyclerViewFriendRequests;
    private FriendRequestsAdapter friendRequestsAdapter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_friend_requests);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_requests);

        //return back to main page
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        String recipientId = GetUserName.getUsernameFromSharedPreferences(this);

        // Find RecyclerView in layout
        recyclerViewFriendRequests = findViewById(R.id.recyclerViewFriendRequests);

        // Set up RecyclerView
        recyclerViewFriendRequests.setLayoutManager(new LinearLayoutManager(this));

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
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<FriendRequestModel> friendRequests = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert Firestore document to FriendRequestModel
                                Log.d("Friend Name", "onComplete: " + document.get("senderId"));
                                FriendRequestModel friendRequest = document.toObject(FriendRequestModel.class);
                                friendRequests.add(friendRequest);
                            }
                            // Update RecyclerView with fetched friend requests
                            friendRequestsAdapter.updateFriendRequests(friendRequests);
                        } else {
                            // Log error or handle failure
                            Log.e("Friend Request", "Error getting friend requests", task.getException());
                        }
                    }
                });
    }

}

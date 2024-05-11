package com.hasan.storyvibrance.Posts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.Controller.SavedPostsAdapter;
import com.hasan.storyvibrance.Model.PostSavedModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivitySavedPostBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavedPostActivity extends AppCompatActivity {

    ActivitySavedPostBinding binding;
    private SavedPostsAdapter savedPostsAdapter;
    private List<PostSavedModel> savedPostsList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_post);
        // Get an instance of FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        // Get the current Firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Ensure user is not null
        assert user != null;
        // Get the user ID
        String userId = user.getUid();
        // Initialize the list to hold saved posts data and create an adapter for the RecyclerView
        savedPostsList = new ArrayList<>();
        savedPostsAdapter = new SavedPostsAdapter(savedPostsList);
        // Set the adapter to the RecyclerView in the layout
        binding.savedPostRecyclerView.setAdapter(savedPostsAdapter);
        // Fetch saved posts data for the current user from Firestore
        fetchSavedPostsData(db, userId);
        // Set click listener for the back button to navigate back to the previous page
        binding.backBtn.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());
    }

    /**
     * Fetches saved posts data from Firestore for a specific user.
     * Populates the savedPostsList with saved post data.
     * @param db The instance of FirebaseFirestore.
     * @param userId The ID of the user whose saved posts are to be fetched.
     */
    private void fetchSavedPostsData(FirebaseFirestore db, String userId) {
        db.collection("posts").get().addOnSuccessListener(queryDocumentSnapshots -> {
            savedPostsList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                // Get the data from the document
                Map<String, Object> postData = document.getData();
                List<Map<String, Object>> postSavedList = (List<Map<String, Object>>) (postData != null ? postData.get("postSaved") : null);

                if (postSavedList != null) {
                    for (Map<String, Object> savedPost : postSavedList) {
                        String savedUserId = (String) savedPost.get("userId");
                        if (userId != null && userId.equals(savedUserId)) {
                            String postMedia = (String) postData.get("postMedia");
                            String postTextContent = (String) postData.get("postTextContent");
                            // Create a new PostSavedModel object
                            PostSavedModel postSavedModel = new PostSavedModel();
                            postSavedModel.setPostContent(postTextContent);
                            postSavedModel.setPostMedia(postMedia);
                            // Add the new post to the saved posts list
                            savedPostsList.add(postSavedModel);
                        }
                    }
                }
            }

            // Notify the adapter that the data set has changed
            savedPostsAdapter.notifyDataSetChanged();
        });

    }


}
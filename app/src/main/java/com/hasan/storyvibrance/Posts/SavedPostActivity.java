package com.hasan.storyvibrance.Posts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.databinding.ActivitySavedPostBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavedPostActivity extends AppCompatActivity {

    ActivitySavedPostBinding binding;

    FirebaseFirestore db;
    PostModel post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_post);
        db = FirebaseFirestore.getInstance();
        String username = GetUserName.getUsernameFromSharedPreferences(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        db.collection("posts")
                .whereEqualTo("savedByUsers.", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Iterate through queryDocumentSnapshots to access each post
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        PostModel post = documentSnapshot.toObject(PostModel.class);
                        assert post != null;
                        // Log the post data
                        assert post != null;
                        Log.d("PostData", "Post ID: " + post.getPostId());
                        Log.d("PostData", "Author Name: " + post.getAuthorName());
                        // Log other post fields as needed
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Firestore", "Error getting posts: " + e.getMessage());
                });







//        binding.savedPostRecyclerView.setAdapter();






        //Back to previous page====
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

    }

    //TODO== SAVED POST FROM DATABASE

    public List<PostModel> getSavedPostsFromSharedPreferences(String userId) {
        List<PostModel> savedPosts = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("SavedPosts_" + userId, MODE_PRIVATE);
        Gson gson = new Gson();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String postJson = entry.getValue().toString();
            try {
                PostModel post = gson.fromJson(postJson, PostModel.class);
                savedPosts.add(post);
            } catch (JsonSyntaxException e) {
                // Handle JSON syntax exception (malformed JSON)
                Log.e("ImportClass", "Error parsing JSON for post: " + entry.getKey(), e);
            }
        }
        return savedPosts;
    }



}
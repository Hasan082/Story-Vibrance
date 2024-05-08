package com.hasan.storyvibrance.Posts;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hasan.storyvibrance.Controller.SavedPostsAdapter;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivitySavedPostBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavedPostActivity extends AppCompatActivity {

    ActivitySavedPostBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_post);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        List<PostModel> savedPosts = getSavedPostsFromSharedPreferences(userId);
        SavedPostsAdapter savedPostsAdapter = new SavedPostsAdapter(savedPosts);
        binding.savedPostRecyclerView.setAdapter(savedPostsAdapter);

        if (savedPosts.isEmpty()) {
            binding.noSavedPost.setVisibility(View.VISIBLE);
        } else {
            binding.noSavedPost.setVisibility(View.GONE);
        }

        //Back to previous page====
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

    }


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
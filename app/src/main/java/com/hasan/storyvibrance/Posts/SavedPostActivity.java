package com.hasan.storyvibrance.Posts;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.gson.Gson;
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
        List<PostModel> savedPosts = getSavedPostsFromSharedPreferences();
        SavedPostsAdapter savedPostsAdapter = new SavedPostsAdapter(savedPosts);
        binding.savedPostRecyclerView.setAdapter(savedPostsAdapter);

        //Back to previous page====
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

    }


    private List<PostModel> getSavedPostsFromSharedPreferences() {
        List<PostModel> savedPosts = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("SavedPosts", MODE_PRIVATE);
        Gson gson = new Gson();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String postJson = entry.getValue().toString();
            PostModel post = gson.fromJson(postJson, PostModel.class);
            savedPosts.add(post);
        }
        return savedPosts;
    }

}
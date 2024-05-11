package com.hasan.storyvibrance.Posts;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.databinding.ActivitySavedPostBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SavedPostActivity extends AppCompatActivity {

    ActivitySavedPostBinding binding;

    FirebaseFirestore db;
    PostModel post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_post);
        db = FirebaseFirestore.getInstance();
        db.collection("posts").whereEqualTo("saved", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

            }
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
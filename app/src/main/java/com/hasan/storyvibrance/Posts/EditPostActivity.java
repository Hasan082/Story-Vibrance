package com.hasan.storyvibrance.Posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityEditPostBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {

    ActivityEditPostBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_post);
        db = FirebaseFirestore.getInstance();
        String postId = getIntent().getStringExtra("postId");
        binding.spinner.setVisibility(View.VISIBLE);
        //Populate post from firebase
        populatePostContent(postId, db);


        // Save button click listener
        binding.updatePostButton.setOnClickListener(v -> {
            binding.spinner.setVisibility(View.VISIBLE);
            String updatedTextContent = binding.editPostContentInput.getText().toString();
            updatePostContent(postId, updatedTextContent);
        });



        //Back to previous activity
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

    }


    private void updatePostContent(String postId, String updatedTextContent) {
        // Create a map to hold the updated data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("postTextContent", updatedTextContent);

        // Update the document in Firestore
        db.collection("posts").document(postId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                    Toast.makeText(EditPostActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                    binding.spinner.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    // Error handling
                    Log.e("EditPostActivity", "Error updating post", e);
                    Toast.makeText(EditPostActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show();
                    binding.spinner.setVisibility(View.GONE);
                });
    }


    private void populatePostContent(String postId, FirebaseFirestore db) {

        db.collection("posts").document(postId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the post data from the document snapshot
                String postTextContent = documentSnapshot.getString("postTextContent");
                String postMediaUrl = documentSnapshot.getString("postMedia");
                // Populate the UI elements with the post content
                binding.setPostContent(postTextContent);
                Picasso.get().load(postMediaUrl).into(binding.editMediaPreview);
                binding.spinner.setVisibility(View.GONE);
            } else {
                Log.d("EditPostActivity", "No such document");
                binding.spinner.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            Log.e("EditPostActivity", "Error fetching post data", e);
            binding.spinner.setVisibility(View.GONE);
        });
    }

}
package com.hasan.storyvibrance.Posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityEditPostBinding;
import com.squareup.picasso.Picasso;

public class EditPostActivity extends AppCompatActivity {

    ActivityEditPostBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_post);
        db = FirebaseFirestore.getInstance();
        String postId = getIntent().getStringExtra("postId");
        populatePostContent(postId, db);

        //Back to previous activity
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

    }

    private void populatePostContent(String postId, FirebaseFirestore db) {

        db.collection("posts").document(postId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the post data from the document snapshot
                String postTextContent = documentSnapshot.getString("postTextContent");
                String postMediaUrl = documentSnapshot.getString("postMedia");
                // Populate the UI elements with the post content
                binding.editPostContentInput.setText(postTextContent);
                Picasso.get().load(postMediaUrl).into(binding.editMediaPreview);
            } else {
                Log.d("EditPostActivity", "No such document");
            }
        }).addOnFailureListener(e -> {
            Log.e("EditPostActivity", "Error fetching post data", e);
        });
    }

}
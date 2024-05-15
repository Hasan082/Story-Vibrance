package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hasan.storyvibrance.Controller.CommentAdapter;
import com.hasan.storyvibrance.Model.CommentModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentBottomSheetDialog extends BottomSheetDialogFragment {
    FirebaseFirestore db;
    ImageView postCommentButton;
    EditText commentEditText;
    RecyclerView recyclerViewComments;
    CommentAdapter commentAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comment_bottom_sheet_layout,
                container, false);
        postCommentButton = v.findViewById(R.id.postCommentButton);
        commentEditText = v.findViewById(R.id.commentEditText);
        String userId = GetUserName.getUsernameFromSharedPreferences(requireContext());
        db = FirebaseFirestore.getInstance();

        recyclerViewComments = v.findViewById(R.id.recyclerViewComments);
//        commentAdapter = new CommentAdapter(new ArrayList<>(), requireContext());
//        recyclerViewComments.setAdapter(commentAdapter);


        Bundle args = getArguments();
        if (args != null) {
            String postId = args.getString("postId");
            if (postId != null) {
                retrieveComments(postId);
            }
        }
        postCommentButton.setOnClickListener(v1 -> {
            if (args != null) {
                String postId = args.getString("postId");
                String commentContent = commentEditText.getText().toString().trim();

                if (!commentContent.isEmpty()) {

                    CommentModel newComment = new CommentModel(postId, userId, commentContent, System.currentTimeMillis());

                    db.collection("comments")
                            .add(newComment)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(requireContext(), "Comment Added Successfully", Toast.LENGTH_SHORT).show();
                                commentEditText.setText("");
                                // Dismiss the bottom sheet
                                dismiss();
                            })
                            .addOnFailureListener(e -> Toast.makeText(requireContext(), "Comment Adding Failed", Toast.LENGTH_SHORT).show());
                } else {
                    // Handle empty comment content
                    Toast.makeText(requireContext(), "Please enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //if no comment set recyclerview to hide
        recyclerViewComments.post(() -> {
            if (commentAdapter.getItemCount() == 0) {
                recyclerViewComments.setVisibility(View.GONE);
            }
        });

        return v;
    }

    private void retrieveComments(String postId) {
        db.collection("comments")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CommentModel> existingComments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert each document to CommentModel and add to the list
                        CommentModel comment = document.toObject(CommentModel.class);
                        existingComments.add(comment);
                    }
                    // Initialize RecyclerView and adapter with the existing comment list
                    commentAdapter = new CommentAdapter(existingComments, requireContext());
                    recyclerViewComments.setAdapter(commentAdapter);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Comment error", "Error retrieving comments: " + e.getMessage());
                });
    }



    //Adjust the sheet as content height
    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getDialog()).getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}

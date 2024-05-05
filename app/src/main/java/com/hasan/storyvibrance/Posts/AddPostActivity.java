package com.hasan.storyvibrance.Posts;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityAddPostBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {


    ActivityAddPostBinding binding;

    Uri addMediaUri;

    FirebaseFirestore db;
    StorageReference storageReference;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
        //FireStore database================
        db = FirebaseFirestore.getInstance();
        //Firebase Storage===================
        storageReference = FirebaseStorage.getInstance().getReference().child("postImg");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //ADD MEDIA BUTTON CLICK============
        binding.addMediaButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        //CLICK POST BUTTON TO SENT POST CONTENT TO DATABASE=========
        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinner.setVisibility(View.VISIBLE);
                showLoadingIndicator();
                UploadPostDataToFireStore(db, storageReference);
            }
        });

        //Back to main page=================================
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


    }//On Create method end

    private void UploadPostDataToFireStore(FirebaseFirestore db, StorageReference storageReference) {
        String postContent = binding.postContentInput.getText().toString().trim();
        String userName = getUsernameFromSharedPreferences();
        String timeStamp = String.valueOf(System.currentTimeMillis());


        if(!TextUtils.isEmpty(postContent)) {
            Map<String, Object> posts= new HashMap<>();
            posts.put("content", postContent);
            posts.put("authorName", userName);
            posts.put("timestamp", timeStamp);

            if(addMediaUri!=null) {
                //if image available, upload  image storage and post to firestore
                UploadImageToStorage(addMediaUri, posts, storageReference, db);

            }else{
                //if image not available content upload to firestore
                UploadPostToFireStore(posts, db);
            }

        } else {
            Toast.makeText(AddPostActivity.this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
            hideLoadingIndicator();
        }


    }

    //post content database code=========
    private void UploadPostToFireStore(Map<String, Object> posts, FirebaseFirestore db) {

        db.collection("posts").add(posts).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // Post added successfully
                Toast.makeText(AddPostActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                // Clear input fields or reset UI if needed
                binding.postContentInput.setText("");
                binding.mediaPreview.setImageURI(null);
                binding.mediaPreview.setVisibility(View.GONE);
                hideLoadingIndicator();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Post upload failed", Toast.LENGTH_SHORT).show();
                hideLoadingIndicator();
            }
        });

    }

    //post media upload tgo database function=====
    private void UploadImageToStorage(Uri addMediaUri, Map<String, Object> posts, StorageReference storageReference, FirebaseFirestore db) {
        // Get file extension from the media URI
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(addMediaUri.toString());
        // Generate a unique filename using timestamp, UUID, and file extension
        String uniqueId = UUID.randomUUID().toString();
        String fileName = System.currentTimeMillis() + "_" + uniqueId + "." + fileExtension;
        storageReference.child(fileName).putFile(addMediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 // Get the download URL for the uploaded image
                storageReference.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("imgUrl", "onSuccess: " + uri);
                        // Add media URL to post data
                        posts.put("mediaUrl", uri.toString());
                        // Upload post data to Firestore
                        UploadPostToFireStore(posts, db);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                hideLoadingIndicator();
            }
        });
    }


    // IMAGE HANDLE FUNCTION
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                addMediaUri = uri;
                binding.mediaPreview.setImageURI(uri);
                binding.mediaPreview.setVisibility(View.VISIBLE);
            });

    //Shared preference function
    private String getUsernameFromSharedPreferences() {
        return sharedPreferences.getString("username", "");
    }


    private void showLoadingIndicator() {
        binding.spinner.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideLoadingIndicator() {
        binding.spinner.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }



}
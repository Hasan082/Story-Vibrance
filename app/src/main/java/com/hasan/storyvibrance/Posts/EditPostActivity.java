package com.hasan.storyvibrance.Posts;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityEditPostBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditPostActivity extends AppCompatActivity {

    ActivityEditPostBinding binding;
    FirebaseFirestore db;
    Uri addMediaUri;

    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_post);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("postImg");
        String postId = getIntent().getStringExtra("postId");
        binding.spinner.setVisibility(View.VISIBLE);
        //Populate post from firebase
        populatePostContent(postId, db);

        // Click listener for posting content to the database
        binding.updatePostButton.setOnClickListener(v -> {
            showLoadingIndicator();
            // Upload post data to Firestore
            String textCon = binding.editPostContentInput.getText().toString();
            updatePostContent(postId, textCon, addMediaUri);
        });





        binding.editAddMediaButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        //Back to previous activity
        binding.backBtn.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());

    }

    private void updatePostContent(String postId, String updatedTextContent, Uri addMediaUri) {
        // Create a map to hold the updated data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("postTextContent", updatedTextContent);

        if (addMediaUri != null) {
            // If image is available, compress it and then upload it to storage
            Bitmap bitmap = getBitmapFromUri(addMediaUri);
            assert bitmap != null;
            Uri compressedUri = getImageUri(getApplicationContext(), bitmap);
            assert compressedUri != null;
            uploadImageToStorage(compressedUri, postId, updatedData);
        } else {
            // If no image is selected, directly update the post data in Firestore
            updatePostDataInFirestore(postId, updatedData);
        }
    }


    private void uploadImageToStorage(Uri addMediaUri, String postId, Map<String, Object> updatedData) {
        showLoadingIndicator();

        // Generate a unique filename
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(addMediaUri.toString());
        String uniqueId = UUID.randomUUID().toString();
        String fileName = System.currentTimeMillis() + "_" + uniqueId + "." + fileExtension;

        // Upload image to storage
        storageReference.child(fileName).putFile(addMediaUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL for the uploaded image
                    storageReference.child(fileName).getDownloadUrl().addOnSuccessListener(uri -> {
                        // Add media URL to updated data
                        updatedData.put("postMedia", uri.toString());
                        // Update post data in Firestore
                        updatePostDataInFirestore(postId, updatedData);
                    }).addOnFailureListener(e -> {
                        // Failed to get download URL
                        Toast.makeText(getApplicationContext(), "Failed to get image URL", Toast.LENGTH_SHORT).show();
                        hideLoadingIndicator();
                    });
                })
                .addOnFailureListener(e -> {
                    // Image upload failed
                    Toast.makeText(getApplicationContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                    hideLoadingIndicator();
                });
    }

    private void updatePostDataInFirestore(String postId, Map<String, Object> updatedData) {
        // Update the document in Firestore
        db.collection("posts").document(postId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                    hideLoadingIndicator();
                    showPostSuccessDialog();
                })
                .addOnFailureListener(e -> {
                    // Error handling
                    Log.e("EditPostActivity", "Error updating post", e);
                    Toast.makeText(EditPostActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show();
                    hideLoadingIndicator();
                });
    }



    /**
     * Displays an AlertDialog to prompt the user for further action after successfully adding a post.
     * The dialog offers options to either go back or add another post.
     */
    private void showPostSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post Updated Successfully");
        builder.setMessage("What would you like to do next?");
        builder.setPositiveButton("Go Back", (dialog, which) -> {
            dialog.dismiss();
            getOnBackPressedDispatcher().onBackPressed(); // Navigate back to the previous screen
        });
        builder.setNegativeButton("Add Another Post", (dialog, which) -> {
            dialog.dismiss(); // Dismiss the dialog
        });
        builder.show(); // Show the AlertDialog
    }



    private Uri getImageUri(Context context, Bitmap bitmap) {
        // Define maximum dimensions for the resized image
        int maxWidth = 1024;
        int maxHeight = 1024;

        // Get the original dimensions of the bitmap
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        // Calculate the scale factor to fit within the maximum dimensions while maintaining aspect ratio
        float scaleFactor = Math.min((float) maxWidth / originalWidth, (float) maxHeight / originalHeight);

        // Calculate the final dimensions for the resized image
        int scaledWidth = Math.round(originalWidth * scaleFactor);
        int scaledHeight = Math.round(originalHeight * scaleFactor);

        // Resize the bitmap
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

        // Compress the resized bitmap
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

        // Store the compressed bitmap in a temporary file
        File outputDir = context.getCacheDir(); // Get the cache directory
        File outputFile;
        try {
            outputFile = File.createTempFile("compressed_image", ".jpg", outputDir);
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(bytes.toByteArray());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Return the URI of the compressed and resized image
        return Uri.fromFile(outputFile);
    }

    /**
     * Retrieves a Bitmap from the provided URI.
     *
     * @param uri The URI of the image.
     * @return The Bitmap retrieved from the URI, or null if an error occurs.
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Handles the image selection result using Activity Result API.
     * Sets the selected image URI to addMediaUri, updates the image preview, and makes it visible.
     */
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                addMediaUri = uri; // Set the selected image URI
                binding.editMediaPreview.setImageURI(uri); // Set the image for preview
            });

    /**
     * Shows the loading indicator and disables touch events on the window.
     */
    private void showLoadingIndicator() {
        binding.spinner.setVisibility(View.VISIBLE);
        // Disable touch events on the window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * Hides the loading indicator and enables touch events on the window.
     */
    private void hideLoadingIndicator() {
        binding.spinner.setVisibility(View.GONE);
        // Enable touch events on the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
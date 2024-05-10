package com.hasan.storyvibrance.Posts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.DataBaseError;
import com.hasan.storyvibrance.databinding.ActivityAddPostBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        // FireStore database initialization
        db = FirebaseFirestore.getInstance();

        // Firebase Storage initialization
        storageReference = FirebaseStorage.getInstance().getReference().child("postImg");

        // Shared preferences initialization
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Add media button click listener to select images
        binding.addMediaButton.setOnClickListener(v -> mGetContent.launch("image/*"));

        // Click listener for posting content to the database
        binding.postButton.setOnClickListener(v -> {
            // Show loading indicator when posting
            showLoadingIndicator();
            // Upload post data to Firestore
            UploadPostDataToFireStore(db, storageReference);
        });

        // Click listener for navigating back to the main page
        binding.backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());


    } //On Create method end================

    /**
     * Uploads post data to Firestore, including image if available.
     *
     * @param db               The instance of FirebaseFirestore.
     * @param storageReference The reference to Firebase Storage.
     */
    private void UploadPostDataToFireStore(FirebaseFirestore db, StorageReference storageReference) {
        String postContent = binding.postContentInput.getText().toString().trim();
        String userName = getUsernameFromSharedPreferences();
        String timeStamp = String.valueOf(System.currentTimeMillis());

        // Check if post content is not empty
        if (!TextUtils.isEmpty(postContent)) {
            Map<String, Object> posts = new HashMap<>();
            String username = getUsernameFromSharedPreferences();
            // Fetch user data from Firestore
            db.collection("userdata").document(userName).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Extract user data from document
                        String personName = documentSnapshot.getString("name");
                        String userBio = documentSnapshot.getString("bio");
                        // Set user data to UI
                        DataBaseError.showDatabaseErrorMessage(getApplicationContext());
                    } else {
                        // Show error message if document does not exist
                        DataBaseError.showDatabaseErrorMessage(getApplicationContext());
                    }
                } else {
                    // Show error message if Firestore operation fails
                    DataBaseError.showDatabaseErrorMessage(getApplicationContext());
                }
            });

            // Create a map to store post data
            posts.put("postTextContent", postContent);
            posts.put("authorUsername", userName);
            posts.put("timestamp", timeStamp);

            // Check if an image is available
            if (addMediaUri != null) {
                // If image is available, upload it to storage and then upload post data to Firestore
                uploadImageToStorage(addMediaUri, posts, storageReference, db);
            } else {
                // If no image is available, directly upload post data to Firestore
                uploadPostToFirestore(posts, db);
            }
        } else {
            // Show error message if post content is empty
            Toast.makeText(AddPostActivity.this, "Post cannot be empty", Toast.LENGTH_SHORT).show();
            hideLoadingIndicator();
        }
    }


    /**
     * Uploads the post data to Firestore.
     *
     * @param posts The map containing post data.
     * @param db    The instance of FirebaseFirestore.
     */
    private void uploadPostToFirestore(Map<String, Object> posts, FirebaseFirestore db) {
        db.collection("posts").add(posts).addOnSuccessListener(documentReference -> {

            // Clear input fields or reset UI if needed
              binding.postContentInput.setText("");
              binding.mediaPreview.setImageURI(null);
              binding.mediaPreview.setVisibility(View.GONE);

            //SHOW A ALERT DIALOG AFTER Post added successfully
            showPostSuccessDialog();
            // HIDE LOADING SPINNER
            hideLoadingIndicator();

        }).addOnFailureListener(e -> {
            // Failed to upload post
            Toast.makeText(getApplicationContext(), "Post upload failed", Toast.LENGTH_SHORT).show();
            hideLoadingIndicator();
        });
    }


    /**
     * Uploads an image to Firebase Storage and adds its download URL to the post data.
     * If no image is provided, proceeds to upload the post data directly to Firestore.
     *
     * @param addMediaUri      The URI of the image to upload.
     * @param posts            The map containing post data.
     * @param storageReference The reference to the Firebase Storage location.
     * @param db               The instance of FirebaseFirestore.
     */
    private void uploadImageToStorage(Uri addMediaUri, Map<String, Object> posts, StorageReference storageReference, FirebaseFirestore db) {
        if (addMediaUri == null) {
            // No image to upload
            uploadPostToFirestore(posts, db);
            return;
        }

        showLoadingIndicator();

        // Get bitmap from URI
        Bitmap bitmap = getBitmapFromUri(addMediaUri);
        if (bitmap == null) {
            // Unable to retrieve bitmap
            hideLoadingIndicator();
            Toast.makeText(getApplicationContext(), "Failed to process image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Compress and resize the bitmap
        Uri compressedUri = getImageUri(getApplicationContext(), bitmap);

        // Generate a unique filename
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(compressedUri.toString());
        String uniqueId = UUID.randomUUID().toString();
        String fileName = System.currentTimeMillis() + "_" + uniqueId + "." + fileExtension;

        // Upload compressed image to storage
        storageReference.child(fileName).putFile(compressedUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL for the uploaded image
                    storageReference.child(fileName).getDownloadUrl().addOnSuccessListener(uri -> {
                        // Add media URL to post data
                        posts.put("postMedia", uri.toString());
                        // Upload post data to Firestore
                        uploadPostToFirestore(posts, db);
                        hideLoadingIndicator();
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
     * Resizes and compresses the given bitmap to fit within specified maximum dimensions.
     *
     * @param context The context used to access resources.
     * @param bitmap  The bitmap image to be resized and compressed.
     * @return The URI of the resized and compressed image.
     */
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
     * Handles the image selection result using Activity Result API.
     * Sets the selected image URI to addMediaUri, updates the image preview, and makes it visible.
     */
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                addMediaUri = uri; // Set the selected image URI
                binding.mediaPreview.setImageURI(uri); // Set the image for preview
                binding.mediaPreview.setVisibility(View.VISIBLE); // Make the preview image visible
            });

    /**
     * Retrieves the username from SharedPreferences.
     *
     * @return The username stored in SharedPreferences, or an empty string if not found.
     */
    private String getUsernameFromSharedPreferences() {
        return sharedPreferences.getString("username", "");
    }


    /**
     * Shows the loading indicator and disables touch events on the window.
     */
    private void showLoadingIndicator() {
        // Make the spinner visible
        binding.spinner.setVisibility(View.VISIBLE);
        // Disable touch events on the window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * Hides the loading indicator and enables touch events on the window.
     */
    private void hideLoadingIndicator() {
        // Hide the spinner
        binding.spinner.setVisibility(View.GONE);
        // Enable touch events on the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    /**
     * Displays an AlertDialog to prompt the user for further action after successfully adding a post.
     * The dialog offers options to either go back or add another post.
     */
    private void showPostSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post Added Successfully");
        builder.setMessage("What would you like to do next?");

        int backgroundColor = ContextCompat.getColor(this, R.color.primaryColor);

        builder.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "Go Back" action
                dialog.dismiss(); // Dismiss the dialog
                getOnBackPressedDispatcher().onBackPressed(); // Navigate back to the previous screen
            }
        });
        builder.setNegativeButton("Add Another Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "Add Another Post" action
                dialog.dismiss(); // Dismiss the dialog
                // Perform any action needed to add another post, such as clearing input fields
                binding.postContentInput.setText(""); // Clear post content input
                binding.mediaPreview.setImageURI(null); // Clear media preview
                binding.mediaPreview.setVisibility(View.GONE); // Hide media preview
            }
        });
        builder.show(); // Show the AlertDialog
    }




    /**
     * Called when the activity is resumed. Hides the image preview if no image is selected.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Hide the image view if no image is selected
        if (addMediaUri == null) {
            binding.mediaPreview.setVisibility(View.GONE);
        }
    }


}
package com.hasan.storyvibrance.BottomNav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hasan.storyvibrance.Profile.UpdateProfileActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment {

    FragmentProfileBinding binding;
    FirebaseFirestore db;



    Uri profileUri;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = binding.getRoot();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String userName = getUsernameFromSharedPreferences();
        db = FirebaseFirestore.getInstance();

        binding.spinner.setVisibility(View.VISIBLE);
        db.collection("userdata").document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists() && isAdded()) {
                        String personName = documentSnapshot.getString("name");
                        String profileImgUrl = documentSnapshot.getString("ProfileImg");
                        if (profileImgUrl != null) {
                            Picasso.get().load(profileImgUrl).into(binding.profileImg);
                        } else {
                            Picasso.get().load(R.drawable.person).into(binding.profileImg);
                        }
                        binding.setPersonName(personName);
                        binding.setUsername(userName);
                        binding.spinner.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(requireContext(), "Data Not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.goToUpdateProfile.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), UpdateProfileActivity.class));
        });

        binding.profileImgEdit.setOnClickListener(v -> mGetContent.launch("image/*"));

        return view;
    }

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                profileUri = uri;
                binding.profileImg.setImageURI(uri);
                uploadImageToFirestore(uri);
            });

    private void uploadImageToFirestore(Uri uri) {
        if (uri == null) {
            return;
        }

        Bitmap bitmap = getBitmapFromUri(uri);
        if (bitmap == null) {
            return;
        }

        uri = getImageUri(requireContext(), bitmap);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profileImg");
        StorageReference imgRef = storageRef.child(System.currentTimeMillis() + ".jpg");
        showLoadingIndicator();//Start Loader when image upload started
        imgRef.putFile(uri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        imgRef.getDownloadUrl()
                                .addOnSuccessListener(downloadUri -> {
                                    String username = getUsernameFromSharedPreferences();
                                    if (username != null) {
                                        uploadDataToFirestore(username, downloadUri.toString());
                                    } else {
                                        Toast.makeText(requireActivity(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to get download URL
                                });
                    } else {
                        Toast.makeText(requireActivity(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void uploadDataToFirestore(String username, String imageDownloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("userdata")
                .document(username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) updateProfileImageUrl(username, imageDownloadUrl);
                        else createNewUserData(username, imageDownloadUrl);
                    } else Toast.makeText(requireActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfileImageUrl(String username, String imageDownloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("ProfileImg", imageDownloadUrl);
        db.collection("userdata")
                .document(username)
                .set(profileData, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(requireActivity(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        hideLoadingIndicator();
                    }
                    else {
                        if (isAdded()) Toast.makeText(requireActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createNewUserData(String username, String imageDownloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("ProfileImg", imageDownloadUrl);
        db.collection("userdata")
                .document(username)
                .set(profileData)
                .addOnCompleteListener(task -> {
                    //Data Uploaded Successfully
                });
    }

    private String getUsernameFromSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return sharedPreferences.getString("username", "");
    }


    // Method to show the progress bar
    private void showLoadingIndicator() {
        binding.spinner.setVisibility(View.VISIBLE);
//        binding.mainFrame.setVisibility(View.GONE);
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    // Method to hide the loading indicator and enable user interaction
    private void hideLoadingIndicator() {
        binding.spinner.setVisibility(View.GONE);
//        binding.mainFrame.setVisibility(View.VISIBLE);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }





}
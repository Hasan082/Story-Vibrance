package com.hasan.storyvibrance.BottomNav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = binding.getRoot();

        //Get user name from sharedPreferences and pass it to database and set it to view
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String userName = getUsernameFromSharedPreferences();
        //Instance for database===================
        db = FirebaseFirestore.getInstance();

        //Spinner activate until data load=========
        binding.spinner.setVisibility(View.VISIBLE);
        db.collection("userdata").document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists() && isAdded()) {
                        String personName = documentSnapshot.getString("name");
                        String profileImgUrl = documentSnapshot.getString("ProfileImg");
                        // Check if profileImgUrl is not null
                        if (profileImgUrl != null) {
                            // Load the profile image URL into the ImageView
                            Picasso.get().load(profileImgUrl).into(binding.profileImg);
                        } else {
                            // If profileImgUrl is null, load a default image
                            Picasso.get().load(R.drawable.person).into(binding.profileImg); // Replace default_image with your drawable resource
                        }
                        // Set personName and username directly
                        binding.setPersonName(personName);
                        binding.setUsername(userName);
                        //Hide loader======
                        binding.spinner.setVisibility(View.GONE);
                    } else {
                        //if no data available in database
                        Toast.makeText(requireContext(), "Data Not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle failures
                    Toast.makeText(requireContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Go to edit profile
        binding.goToUpdateProfile.setOnClickListener(v->{
            startActivity(new Intent(requireActivity(), UpdateProfileActivity.class));
        });
        //Upload Image and set the image when image changed
        binding.profileImgEdit.setOnClickListener(v-> mGetContent.launch("image/*"));


        return view;
    }

    // GetContent creates an ActivityResultLauncher<String> to let you pass
// in the mime type you want to let the user select
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    profileUri = uri;
                    binding.profileImg.setImageURI(uri);
                    uploadImageToFirestore(uri);
                }
            });

    //SAVE THE IMAGE URL TO DATABASE
    private void uploadImageToFirestore(Uri uri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profileImg");
        StorageReference imgRef = storageRef.child(System.currentTimeMillis() + ".jpg");
        imgRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Save downloadUri to Firestore along with the username
                            String username = getUsernameFromSharedPreferences();
                            if (username != null) {
                                uploadDataToFirestore(username, uri.toString());
                            } else {
                                Toast.makeText(requireActivity(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    private void uploadDataToFirestore(String username, String imageDownloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if the document exists in Firestore
        db.collection("userdata")
                .document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) updateProfileImageUrl(username, imageDownloadUrl);
                            else createNewUserData(username, imageDownloadUrl);
                        } else Toast.makeText(requireActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProfileImageUrl(String username, String imageDownloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a map to update the profile image URL
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("ProfileImg", imageDownloadUrl);
        // Update the existing document with the new profile image URL
        db.collection("userdata")
                .document(username)
                .set(profileData, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Toast.makeText(requireActivity(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        else {
                            // Check if the fragment is attached to an activity before showing the toast
                            if (isAdded()) Toast.makeText(requireActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //  If AFirst time added the profile image
    private void createNewUserData(String username, String imageDownloadUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new document with the profile image URL
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("ProfileImg", imageDownloadUrl);
        db.collection("userdata")
                .document(username)
                .set(profileData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Data Uploaded Successfully
                    }
                });
    }

    // Retrieve the username from SharedPreferences
    private String getUsernameFromSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return sharedPreferences.getString("username", "");
    }


}
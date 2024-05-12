package com.hasan.storyvibrance.BottomNav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hasan.storyvibrance.Controller.ProfilePostAdapter;
import com.hasan.storyvibrance.Model.ProfilePostModel;
import com.hasan.storyvibrance.Profile.UpdateProfileActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentProfile extends Fragment {

    FragmentProfileBinding binding;
    FirebaseFirestore db;

    ProfilePostModel profilePostModel;
    ArrayList<ProfilePostModel> profilePostImg;
    ProfilePostAdapter profilePostAdapter;
    Uri profileUri;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = binding.getRoot();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String userName = GetUserName.getUsernameFromSharedPreferences(requireContext());
        db = FirebaseFirestore.getInstance();

        binding.spinner.setVisibility(View.VISIBLE);

        db.collection("userdata").document(userName)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        handleFirestoreError(e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists() && isAdded()) {
                        handleUserData(documentSnapshot);
                    } else {
                        handleNoDataAvailable();
                    }
                });

        binding.goToUpdateProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), UpdateProfileActivity.class)));
        binding.profileImgEdit.setOnClickListener(v -> mGetContent.launch("image/*"));



        //Self Post recyclerview


        profilePostImg = new ArrayList<>();
        profilePostAdapter = new ProfilePostAdapter(profilePostImg, requireContext());
        binding.ownPostRecyclerView.setAdapter(profilePostAdapter);
        profilePostAdapter.notifyDataSetChanged();


        profilePostImg.add(new ProfilePostModel("https://picsum.photos/seed/picsum/200/300"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/400/300.jpg"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/seed/picsum/500/250"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/425/300.jpg"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/seed/picsum/400/300"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/300/300.jpg"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/seed/picsum/220/300"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/200/300.jpg"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/seed/picsum/250/300"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/200/300.jpg"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/seed/picsum/100/300"));
        profilePostImg.add(new ProfilePostModel("https://picsum.photos/600/300.jpg"));

        profilePostAdapter.notifyDataSetChanged();




        return view;
    }

    private void handleFirestoreError(Exception e) {
        Toast.makeText(getContext(), "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        binding.spinner.setVisibility(View.GONE);
    }

    private void handleUserData(DocumentSnapshot documentSnapshot) {
        String personName = documentSnapshot.getString("name");
        String userBio = documentSnapshot.getString("bio");
        String profileImgUrl = documentSnapshot.getString("ProfileImg");

        binding.setPersonName(personName);
        binding.setUsername(documentSnapshot.getId());
        binding.setUserBio(userBio != null ? userBio : getString(R.string.about_me));

        if (profileImgUrl != null) {
            Picasso.get().load(profileImgUrl)
                    .placeholder(R.drawable.edit_person)
                    .error(R.drawable.edit_person)
                    .into(binding.profileImg);
        }

        binding.spinner.setVisibility(View.GONE);
    }

    private void handleNoDataAvailable() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Data Not Available", Toast.LENGTH_SHORT).show();
            binding.spinner.setVisibility(View.GONE);
        }
    }


    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
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
        showLoadingIndicator();
        imgRef.putFile(uri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        handleImageUploadSuccess(imgRef);
                    } else {
                        handleImageUploadFailure();
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

    private void handleImageUploadSuccess(StorageReference imgRef) {
        imgRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
            String username = GetUserName.getUsernameFromSharedPreferences(requireContext());
            uploadDataToFirestore(username, downloadUri.toString());
        });
    }

    private void handleImageUploadFailure() {
        Toast.makeText(requireContext(), "Image Upload Failed", Toast.LENGTH_SHORT).show();
        hideLoadingIndicator();
    }

    private void uploadDataToFirestore(String username, String imageDownloadUrl) {
        db.collection("userdata").document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    updateProfileImageUrl(username, imageDownloadUrl);
                } else {
                    createNewUserData(username, imageDownloadUrl);
                }
            } else {
                Toast.makeText(getActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileImageUrl(String username, String imageDownloadUrl) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("ProfileImg", imageDownloadUrl);
        db.collection("userdata").document(username)
                .set(profileData, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        hideLoadingIndicator();
                    } else {
                        Toast.makeText(getActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                        hideLoadingIndicator();
                    }
                });
    }

    private void createNewUserData(String username, String imageDownloadUrl) {
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("ProfileImg", imageDownloadUrl);
        db.collection("userdata").document(username)
                .set(profileData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("UserData", "create NewUser Data: ");
                    } else {
                        Toast.makeText(getActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void showLoadingIndicator() {
        binding.spinner.setVisibility(View.VISIBLE);
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideLoadingIndicator() {
        binding.spinner.setVisibility(View.GONE);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
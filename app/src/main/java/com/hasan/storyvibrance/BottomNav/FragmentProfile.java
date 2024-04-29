package com.hasan.storyvibrance.BottomNav;

import android.content.SharedPreferences;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.FragmentProfileBinding;

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
        db.collection("userdata").document(userName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String personName = documentSnapshot.getString("name");
                binding.setPersonName(personName);
                binding.setUsername(userName);
                binding.spinner.setVisibility(View.GONE);
            }
        });


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

    private void uploadImageToFirestore(Uri uri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profileImg");
        StorageReference imgRef = storageRef.child(System.currentTimeMillis() + ".jpg");
        imgRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Profile Image Updated successfully!", Toast.LENGTH_SHORT).show();
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
        db = FirebaseFirestore.getInstance();
        Map<String, String> profileData = new HashMap<>();
        profileData.put("username", username);
        profileData.put("ProfileImg", imageDownloadUrl);

        db.collection("userdata").document(username).set(profileData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(requireActivity(), "Database Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    //ToDo firebase permission deny need to fix
    private String getUsernameFromSharedPreferences() {
        // Retrieve the username from SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return sharedPreferences.getString("username", "");
    }


}
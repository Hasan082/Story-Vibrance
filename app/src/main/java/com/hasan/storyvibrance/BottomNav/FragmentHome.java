package com.hasan.storyvibrance.BottomNav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hasan.storyvibrance.Controller.PostAdapter;
import com.hasan.storyvibrance.Controller.StoryAdapter;
import com.hasan.storyvibrance.Messenger.MessengerActivity;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.Model.StoryModel;
import com.hasan.storyvibrance.Posts.ActivityFriendRequests;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.Utility.GetUserName;
import com.hasan.storyvibrance.Utility.PostSorter;
import com.hasan.storyvibrance.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentHome extends Fragment {

    FragmentHomeBinding binding;

    FirebaseFirestore db;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        // Get the root view of the inflated layout
        View view = binding.getRoot();
        // Set the lifecycle owner for observing LiveData
        binding.setLifecycleOwner(this);

        //CREATE FIREBASE INSTANCES==========
        db = FirebaseFirestore.getInstance();
        //CREATE sharedPreferences INSTANCES==========
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String username = GetUserName.getUsernameFromSharedPreferences(requireContext());
        // Set up click listener for opening drawer on app bar icon click
        binding.customAppBar.findViewById(R.id.appbarImg).setOnClickListener(v -> {
            // Call the openDrawer method of NavigationActivity
            ((NavigationActivity) requireActivity()).openDrawer();
        });

        // Set up click listener for navigating to NotificationActivity on notification icon click
//        binding.customAppBar.findViewById(R.id.notificationIcon).setOnClickListener(v -> {
//            // Start the NotificationActivity
//            startActivity(new Intent(getActivity(), NotificationActivity.class));
//        });

        // Set up click listener for navigating to NotificationActivity on notification icon click
        binding.customAppBar.findViewById(R.id.notificationIcon).setOnClickListener(v -> {
            // Start the NotificationActivity
            startActivity(new Intent(getActivity(), ActivityFriendRequests.class));
        });






        // Set up click listener for navigating to MessengerActivity on messenger icon click
        binding.customAppBar.findViewById(R.id.messengerIcon).setOnClickListener(v -> {
            // Start the MessengerActivity
            startActivity(new Intent(getActivity(), MessengerActivity.class));
        });


        // Set up story RecyclerView
        setupStoryRecyclerView();
        // Set up post RecyclerView
        setupPostRecyclerView(db);

        return view;

    }
    // Method to set up RecyclerView for posts

    private void setupPostRecyclerView(FirebaseFirestore db) {
        // Create a map to associate document IDs with PostModel objects
        Map<String, PostModel> postMap = new HashMap<>();

        // Create a new instance of the PostAdapter
        PostAdapter postAdapter = new PostAdapter(getContext(), new ArrayList<>());
        // Set the adapter to the post RecyclerView
        binding.postRecyclerview.setAdapter(postAdapter);

        // Set up a real-time listener on the entire "posts" collection
        db.collection("posts").addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("hello", "Listen failed.", error);
                        return;
                    }
                    // Clear the postMap before adding new posts
                    postMap.clear();

                    assert querySnapshot != null;
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        if (doc.exists()) {
                            String postId = doc.getId();
                            PostModel post = doc.toObject(PostModel.class);
                            post.setPostId(postId);
                            postMap.put(postId, post);
                        } else {
                            Log.d("postError", "No such document");
                        }
                    }

                    // Create a list of PostModel objects from the map
                    ArrayList<PostModel> postModels = new ArrayList<>(postMap.values());

                    // Sort the postModels list
                    PostSorter.sortByTimestampDescending(postModels);

                    // Update the data in the adapter
                    postAdapter.updateData(postModels);
                    postAdapter.notifyDataSetChanged();
                }
        );
    }


    // Method to set up RecyclerView for stories
    private void setupStoryRecyclerView() {
        // Create a list of story models
        ArrayList<StoryModel> storyModels = createStoryModels();
        // Create a StoryAdapter with the story models
        StoryAdapter storyAdapter = new StoryAdapter(getContext(), storyModels);
        // Set the adapter to the story RecyclerView
        binding.storyRecyclerview.setAdapter(storyAdapter);
    }

    // Method to create a list of story models
    private ArrayList<StoryModel> createStoryModels() {
        ArrayList<StoryModel> storyModels = new ArrayList<>();
        // Add story models to the list
        storyModels.add(new StoryModel("Md Hasan", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Sadman Safin Neloy", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Mahmood Moon", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Mollika Mukta", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Nusrat Sara", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Faria Maahi", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Abd Razzak", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Rozina Parvin", R.drawable.hasan, R.drawable.hasan));
        storyModels.add(new StoryModel("Hafizul Islam", R.drawable.hasan, R.drawable.hasan));
        return storyModels;
    }


}
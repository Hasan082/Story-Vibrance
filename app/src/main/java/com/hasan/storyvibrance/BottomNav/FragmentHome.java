package com.hasan.storyvibrance.BottomNav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Controller.PostAdapter;
import com.hasan.storyvibrance.Controller.StoryAdapter;
import com.hasan.storyvibrance.Messenger.MessengerActivity;
import com.hasan.storyvibrance.Model.PostModel;
import com.hasan.storyvibrance.Model.StoryModel;
import com.hasan.storyvibrance.Notification.NotificationActivity;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    FragmentHomeBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        // Get the root view of the inflated layout
        View view = binding.getRoot();
        // Set the lifecycle owner for observing LiveData
        binding.setLifecycleOwner(this);

        // Set up click listener for opening drawer on app bar icon click
        binding.customAppBar.findViewById(R.id.appbarImg).setOnClickListener(v -> {
            // Call the openDrawer method of NavigationActivity
            ((NavigationActivity) requireActivity()).openDrawer();
        });

        // Set up click listener for navigating to NotificationActivity on notification icon click
        binding.customAppBar.findViewById(R.id.notificationIcon).setOnClickListener(v -> {
            // Start the NotificationActivity
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        });

        // Set up click listener for navigating to MessengerActivity on messenger icon click
        binding.customAppBar.findViewById(R.id.messengerIcon).setOnClickListener(v -> {
            // Start the MessengerActivity
            startActivity(new Intent(getActivity(), MessengerActivity.class));
        });

        // Set up story RecyclerView
        setupStoryRecyclerView();
        // Set up post RecyclerView
        setupPostRecyclerView();

        return view;

    }
    // Method to set up RecyclerView for posts
    private void setupPostRecyclerView() {
        // Create a list of post models
        ArrayList<PostModel> postModels = createPostModels();
        // Create a PostAdapter with the post models
        PostAdapter postAdapter = new PostAdapter(getContext(), postModels);
        // Set the adapter to the post RecyclerView
        binding.postRecyclerview.setAdapter(postAdapter);
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

    // Method to create a list of post models
    private ArrayList<PostModel> createPostModels() {
        ArrayList<PostModel> postModels = new ArrayList<>();
        // Add post models to the list
        postModels.add(new PostModel("Pronob", "pronob03", "Lorem ipsum dolor", R.drawable.hasan, R.drawable.posts_img_sample, 30, 10));
        postModels.add(new PostModel("Pronob", "pronob03", "Lorem ipsum dolor", R.drawable.hasan, R.drawable.posts_img_sample, 30, 10));
        return postModels;
    }
}
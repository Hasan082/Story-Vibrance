package com.hasan.storyvibrance.BottomNav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Controller.StoryAdapter;
import com.hasan.storyvibrance.Model.StoryModel;
import com.hasan.storyvibrance.R;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    RecyclerView story_recyclerview;
    StoryAdapter storyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        story_recyclerview = view.findViewById(R.id.story_recyclerview);
        ArrayList<StoryModel> storyModels = createStoryModels();
        storyAdapter = new StoryAdapter(getContext(), storyModels);
        story_recyclerview.setAdapter(storyAdapter);

        return view;
    }


    private ArrayList<StoryModel> createStoryModels() {
        ArrayList<StoryModel> storyModels = new ArrayList<>();
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
package com.hasan.storyvibrance.Feed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.storyvibrance.Controller.StoryAdapter;
import com.hasan.storyvibrance.Model.StoryModel;
import com.hasan.storyvibrance.R;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    RecyclerView story_recyclerview;
    StoryModel storyModel;
    StoryAdapter storyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        story_recyclerview = findViewById(R.id.story_recyclerview);
        ArrayList<StoryModel> storyModels = createStoryModels();
        storyAdapter = new StoryAdapter(this, storyModels);
        story_recyclerview.setAdapter(storyAdapter);
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
package com.hasan.storyvibrance.Posts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivitySearchPostBinding;

public class SearchPostActivity extends AppCompatActivity {

    ActivitySearchPostBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_post);
    }
}
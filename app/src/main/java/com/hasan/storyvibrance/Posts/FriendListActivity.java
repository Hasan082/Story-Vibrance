package com.hasan.storyvibrance.Posts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityFriendListBinding;

public class FriendListActivity extends AppCompatActivity {

    ActivityFriendListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_list);
        //back to home
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });




    }
}
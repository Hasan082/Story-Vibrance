package com.hasan.storyvibrance.Notification;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityNotificationBinding;

import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        //Activate Appbar==========================================
        toolbar = binding.appbarBackInclude.findViewById(R.id.appbarBack);
        setSupportActionBar(toolbar);
        // Enable back button=====================================
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Automatically set title to the name of the activity
        setTitle(getClass().getSimpleName().split("Activity")[0]);

    }



}
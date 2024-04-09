package com.hasan.storyvibrance.Messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityMessengerBinding;
import com.hasan.storyvibrance.databinding.ActivityNotificationBinding;

public class MessengerActivity extends AppCompatActivity {

    ActivityMessengerBinding binding;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_messenger);
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
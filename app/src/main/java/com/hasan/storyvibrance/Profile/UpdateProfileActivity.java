package com.hasan.storyvibrance.Profile;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityUpdateProfileBinding;


public class UpdateProfileActivity extends AppCompatActivity {

    //    ActivityUpdateProfileBinding binding;
    ActivityUpdateProfileBinding binding;
    SharedPreferences sPref;
    SharedPreferences.Editor sPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_profile);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
    }

}
package com.hasan.storyvibrance.OnBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.auth.WelcomeActivity;

public class OnBoardThree extends Fragment {

    Button getStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_board_three, container, false);
        getStarted = view.findViewById(R.id.getStarted);

        getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            startActivity(intent);
            // When onboarding is completed
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("onBoardDone", true);
            editor.apply();

            requireActivity().finish();
        });


        return view;
    }
}
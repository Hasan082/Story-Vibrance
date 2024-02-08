package com.hasan.storyvibrance.OnBoard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hasan.storyvibrance.R;

public class OnBoardOne extends Fragment {
    Button nextOnboardBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_board_one, container, false);
        nextOnboardBtn = view.findViewById(R.id.nextOnboardBtn);

        nextOnboardBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, new OnBoardTwo());
            fragmentTransaction.commit();
        });


        return view;
    }
}
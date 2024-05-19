package com.hasan.storyvibrance.OnBoard;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityOnBoardBinding;

import java.util.ArrayList;

public class OnBoardActivity extends AppCompatActivity {
    ActivityOnBoardBinding binding;
    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board);

        viewPager2 = binding.viewpager;

        try {

            String[] heading = {
                    getString(R.string.onBoard1_title),
                    getString(R.string.onBoard2_title),
                    getString(R.string.onBoard3_title)
            };

            String[] desc = {
                    getString(R.string.onboard1_desc),
                    getString(R.string.onboard2_desc),
                    getString(R.string.onboard3_desc)
            };

            viewPagerItemArrayList = new ArrayList<>();

            for (int i = 0; i < heading.length; i++) {
                if (heading[i] != null && desc[i] != null) {
                    ViewPagerItem viewPagerItem = new ViewPagerItem(heading[i], desc[i]);
                    viewPagerItemArrayList.add(viewPagerItem);
                }
            }

            VPAdapter vpAdapter = new VPAdapter(this, viewPagerItemArrayList);
            viewPager2.setAdapter(vpAdapter);
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                // This method is triggered when there is any scrolling activity for the current page
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                // triggered when you select a new page
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                }

                // triggered when there is
                // scroll state will be changed
                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                }
            });

        } catch (Exception e) {
            Log.e("viewpager error 1", "onCreate: ", e);
            System.out.println("viewpager error 2" + e.getMessage());
            System.out.println("viewpager error 2" + e.getCause());
            e.printStackTrace();
        }
    }

}

package com.hasan.storyvibrance.Utility;

import android.view.View;

public class FadeAnimator {

    public static void showElement(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(null);
    }
}


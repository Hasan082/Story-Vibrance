package com.hasan.storyvibrance.Utility;

import android.content.Context;

public class dpToPx {
    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}

package com.hasan.storyvibrance.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class GetUserName {
    /**
     * Retrieves the username from SharedPreferences.
     *
     * @return The username stored in SharedPreferences.
     */
    public static String getUsernameFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("username", "");
    }

}

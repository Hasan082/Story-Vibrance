package com.hasan.storyvibrance.Utility;

import android.content.SharedPreferences;

public class GetUserName {
    /**
     * Retrieves the username from SharedPreferences.
     *
     * @return The username stored in SharedPreferences.
     */
    public static String getUsernameFromSharedPreferences(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("username", "");
    }

}

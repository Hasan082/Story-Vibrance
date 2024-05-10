package com.hasan.storyvibrance.Utility;

import android.content.Context;
import android.widget.Toast;

public class DataBaseError {
    /**
     * Shows a toast message indicating a database error.
     */
    public static void showDatabaseErrorMessage(Context context) {
        System.out.println("Check this");//ToDo have to delete
        Toast.makeText(context, "Database Error!", Toast.LENGTH_SHORT).show();
    }
}

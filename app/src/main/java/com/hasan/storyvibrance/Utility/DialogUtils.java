package com.hasan.storyvibrance.Utility;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    public static void showConfirmationDialog(Context context, String title, String message,
                                              String positiveButtonText, String negativeButtonText,
                                              int iconResource,
                                              DialogInterface.OnClickListener positiveClickListener,
                                              DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set dialog title and message
        builder.setTitle(title)
                .setMessage(message);

        // Set positive and negative buttons with listeners
        builder.setPositiveButton(positiveButtonText, positiveClickListener)
                .setNegativeButton(negativeButtonText, negativeClickListener);

        // Set dialog icon
        if (iconResource != 0) {
            builder.setIcon(iconResource);
        }

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

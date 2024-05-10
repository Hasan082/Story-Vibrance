package com.hasan.storyvibrance.Utility;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    public static void showConfirmationDialog(Context context, String title, String message,
                                              String positiveButtonText, String negativeButtonText,
                                              DialogInterface.OnClickListener positiveClickListener,
                                              DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveClickListener)
                .setNegativeButton(negativeButtonText, negativeClickListener)
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

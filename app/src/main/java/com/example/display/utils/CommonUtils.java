package com.example.display.utils;

import android.content.Context;
import android.widget.Toast;

public class CommonUtils {

    public static Toast showToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}

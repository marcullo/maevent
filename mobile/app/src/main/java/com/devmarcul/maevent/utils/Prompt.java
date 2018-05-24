package com.devmarcul.maevent.utils;

import android.content.Context;
import android.widget.Toast;

import com.devmarcul.maevent.R;

public class Prompt {
    public static void displayTodo(Context context) {
        String message = context.getString(R.string.text_todo);
        displayShort(message, context);
    }

    public static void displayShort(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void displayLong(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}

package com.devmarcul.maevent.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Tools {

    public static void hideSoftKeyboard(Activity activity, View focus) {
        InputMethodManager imm =(InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (focus == null) {
            return;
        }
        imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity, View focus) {
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.SHOW_FORCED,0);
    }
}

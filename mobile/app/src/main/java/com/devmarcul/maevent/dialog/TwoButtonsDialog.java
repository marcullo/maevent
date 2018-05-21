package com.devmarcul.maevent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.devmarcul.maevent.interfaces.TwoButtonsDialogListener;

public class TwoButtonsDialog {
    private Dialog dialog;
    private String title;
    private String subtitle;
    private String confirmTitle;
    private String cancelTitle;
    private int confirmButtonColor;
    private int cancelButtonColor;
    private int icon;

    private TwoButtonsDialog(TwoButtonsDialog.Builder builder) {
        this.dialog = builder.dialog;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.confirmTitle = builder.confirmTitle;
        this.cancelTitle = builder.cancelTitle;
        this.confirmButtonColor = builder.confirmButtonColor;
        this.cancelButtonColor = builder.cancelButtonColor;
        this.icon = builder.icon;
    }

    public static class Builder {
        private Dialog dialog;
        private AlertDialog.Builder builder;
        private Context context;
        private String title;
        private String subtitle;
        private String confirmTitle;
        private String cancelTitle;
        private int confirmButtonColor;
        private int cancelButtonColor;
        private int icon;
        private TwoButtonsDialogListener confirmListener;
        private TwoButtonsDialogListener cancelListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder setConfirmButton(String confirmTitle, TwoButtonsDialogListener listener) {
            this.confirmTitle = confirmTitle;
            this.confirmListener = listener;
            return this;
        }

        public Builder setCancelButton(String cancelTitle, TwoButtonsDialogListener listener) {
            this.cancelTitle = cancelTitle;
            this.cancelListener = listener;
            return this;
        }

        public Builder setConfirmButtonColor(int color) {
            String colorStr = "#" + Integer.toHexString(color);
            this.confirmButtonColor = Color.parseColor(colorStr);
            return this;
        }

        public Builder setCancelButtonColor(int color) {
            String colorStr = "#" + Integer.toHexString(color);
            this.cancelButtonColor = Color.parseColor(colorStr);
            return this;
        }

        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public TwoButtonsDialog build() {
            builder = new AlertDialog.Builder(context);
            builder.setTitle(this.title != null ? this.title : "");
            builder.setMessage(this.title != null ? this.subtitle : "");
            builder.setCancelable(true);
            builder.setIcon(icon);
            builder.setPositiveButton(confirmTitle, confirmListener);
            builder.setNegativeButton(cancelTitle, cancelListener);
            dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button confirmButton = ((AlertDialog)dialog).getButton(-1);
                    Button cancelButton = ((AlertDialog)dialog).getButton(-2);

                    confirmButton.setTextColor(confirmButtonColor);

                    cancelButton.setTextColor(cancelButtonColor);
                    cancelButton.setBackgroundColor(confirmButtonColor);
                }
            });

            return new TwoButtonsDialog(this);
        }
    }

    public void show() {
        dialog.show();
    }
}

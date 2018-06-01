package com.devmarcul.maevent.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class DetailsDialog {

    protected Dialog dialog;
    protected View detailsView;

    protected DetailsDialog(DetailsDialog.Builder builder) {
        this.dialog = builder.dialog;
        this.detailsView = builder.detailsView;
    }

    public static class Builder {
        private Dialog dialog;
        private AlertDialog.Builder builder;
        private Context context;
        private View detailsView;

        public Builder(Context context, View detailsView) {
            this.context = context;
            this.detailsView = detailsView;
        }

        public DetailsDialog build(boolean transparentBackground) {
            builder = new AlertDialog.Builder(context);
            if(detailsView.getParent() != null)
                ((ViewGroup)detailsView.getParent()).removeView(detailsView);
            builder.setView(detailsView);
            dialog = builder.create();

            if (transparentBackground) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            return new DetailsDialog(this);
        }
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
    }

    public void cancel() {
        dialog.cancel();
    }
}

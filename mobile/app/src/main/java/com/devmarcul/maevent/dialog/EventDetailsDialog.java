package com.devmarcul.maevent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.devmarcul.maevent.event.EventDetailsViewHolder;

public class EventDetailsDialog {

    private Dialog dialog;
    private EventDetailsViewHolder detailsViewHolder;

    private EventDetailsDialog(EventDetailsDialog.Builder builder) {
        this.dialog = builder.dialog;
        this.detailsViewHolder = builder.detailsViewHolder;
    }

    public static class Builder {
        private Dialog dialog;
        private AlertDialog.Builder builder;
        private Context context;
        private EventDetailsViewHolder detailsViewHolder;

        public Builder(Context context, EventDetailsViewHolder holder) {
            this.context = context;
            this.detailsViewHolder = holder;
        }

        public EventDetailsDialog build() {
            View view = detailsViewHolder.getView();

            builder = new AlertDialog.Builder(context);
            builder.setView(view);
            dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    detailsViewHolder.bindOnClickListeners();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    detailsViewHolder.unbindOnClickListeners();
                }
            });

            return new EventDetailsDialog(this);
        }
    }

    public void show() {
        dialog.show();
    }
}

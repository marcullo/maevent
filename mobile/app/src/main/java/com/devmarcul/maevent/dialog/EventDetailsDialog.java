package com.devmarcul.maevent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.devmarcul.maevent.utils.tools.Prompt;

public class EventDetailsDialog {

    private Dialog dialog;
    private View detailsView;

    private EventDetailsDialog(EventDetailsDialog.Builder builder) {
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

        public EventDetailsDialog build() {
            builder = new AlertDialog.Builder(context);
            builder.setView(detailsView);
            dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return new EventDetailsDialog(this);
        }
    }

    public void show() {
        Prompt.displayTodo(detailsView.getContext());
        dialog.show();
    }
}

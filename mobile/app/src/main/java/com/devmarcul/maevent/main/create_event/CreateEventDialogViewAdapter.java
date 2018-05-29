package com.devmarcul.maevent.main.create_event;

import android.view.View;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.devmarcul.maevent.common.ContentListener;

import java.util.Calendar;

public abstract class CreateEventDialogViewAdapter implements
        ContentListener {

    CreateEventDialogViewHolder mViewHolder;
    private Handler mHandler;

    public interface Handler
    {
        void onEventTimeSelected(Calendar startDate, Calendar endDate);
        void onEventTimeCancelled();
    }

    public CreateEventDialogViewAdapter(Handler handler, View viewHolderView) {
        mHandler = handler;
        mViewHolder = new CreateEventDialogViewHolder(viewHolderView);
    }

    @Override
    public void bindListeners() {
        mViewHolder.mSelectTimeView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                mHandler.onEventTimeSelected(startDate, endDate);
            }

            @Override
            public void onCancel() {
                mHandler.onEventTimeCancelled();
            }
        });
    }

    @Override
    public void unbindListeners() {
        mViewHolder.mSelectTimeView.setCalendarListener(null);
    }

    public abstract void adaptContentForSelectingTime();
    public abstract void adaptContentForPickingPlace();
    public abstract void adaptContentForCreatingEvent();
    public abstract void displayError(String message);
}

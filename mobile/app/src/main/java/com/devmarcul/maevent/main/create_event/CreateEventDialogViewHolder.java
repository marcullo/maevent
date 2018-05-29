package com.devmarcul.maevent.main.create_event;

import android.view.View;
import android.widget.ProgressBar;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.devmarcul.maevent.R;

public class CreateEventDialogViewHolder {

    private View view;

    public ProgressBar mLoadingIndicatorProgressBar;
    public DateRangeCalendarView mSelectTimeView;

    public CreateEventDialogViewHolder(View itemView) {
        view = itemView;

        mLoadingIndicatorProgressBar = view.findViewById(R.id.pb_create_event_loading_indicator);
        mSelectTimeView = view.findViewById(R.id.dr_create_event_select_time);
    }
}

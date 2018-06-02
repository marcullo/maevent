package com.devmarcul.maevent.main.create_event;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.main.common.EventDetailsViewHolder;

public class CreateEventViewHolder {

    public static final String TIME_FORMAT = EventDetailsViewHolder.TIME_FORMAT;

    private View view;

    public TextView mSelectedNameView;
    public View mSelectNameView;
        public EditText mWriteNameView;
        public ImageView mNameValidIndicator;
        public TextView mNameValidLabel;

    public EditText mSelectedTimeBuffer;
    public TextView mSelectedTimeView;
    public View mSelectTimeView;
        public ImageView mTimeValidIndicator;
        public TextView mTimeValidLabel;

    public TextView mSelectedPlaceView;
    public View mSelectPlaceView;
        public ImageView mPlaceValidIndicator;
        public TextView mPlaceValidLabel;

    public TextView mSelectedRsvpView;
    public View mSelectRsvpView;
        public CheckBox mCheckRsvpView;

    public FloatingActionButton mCreateEventButton;
    public ProgressBar mCreateEventProgressBar;

    public CreateEventViewHolder(View itemView) {
        view = itemView;
        mSelectedNameView = view.findViewById(R.id.tv_create_event_selected_name);
        mSelectNameView = view.findViewById(R.id.create_event_select_name);
        mWriteNameView = view.findViewById(R.id.et_create_event_write_name);
        mNameValidIndicator = view.findViewById(R.id.iv_create_event_name_valid_indicator);
        mNameValidLabel = view.findViewById(R.id.tv_create_event_name_valid_label);
        mSelectedTimeBuffer = view.findViewById(R.id.tv_create_event_selected_time_buffer);
        mSelectedTimeView = view.findViewById(R.id.tv_create_event_selected_time);
        mSelectTimeView = view.findViewById(R.id.create_event_select_time);
        mTimeValidIndicator = view.findViewById(R.id.iv_create_event_time_valid_indicator);
        mTimeValidLabel = view.findViewById(R.id.iv_create_event_time_valid_label);
        mSelectedPlaceView = view.findViewById(R.id.tv_create_event_selected_place);
        mSelectPlaceView = view.findViewById(R.id.create_event_select_place);
        mPlaceValidIndicator = view.findViewById(R.id.iv_create_event_place_valid_indicator);
        mPlaceValidLabel = view.findViewById(R.id.tv_create_event_place_valid_label);
        mSelectedRsvpView = view.findViewById(R.id.tv_create_event_selected_rsvp);
        mSelectRsvpView = view.findViewById(R.id.create_event_select_rsvp);
        mCheckRsvpView = view.findViewById(R.id.cb_create_event_check_rsvp);
        mCreateEventButton = view.findViewById(R.id.btn_create_event);
        mCreateEventProgressBar = view.findViewById(R.id.pb_create_event_loading);
    }
}

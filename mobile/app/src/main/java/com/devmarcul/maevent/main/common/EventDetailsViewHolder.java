package com.devmarcul.maevent.main.common;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devmarcul.maevent.R;

public class EventDetailsViewHolder {
    private View view;

    public ImageButton mLocationButton;
    public ImageButton mCallButton;
    public ImageButton mCalendarButton;
    public Button mJoinButton;
    public TextView mNameView;
    public TextView mHostsView;
    public TextView mPlaceView;
    public TextView mStreetView;
    public TextView mPostalCodeView;
    public TextView mTimeView;
    public TextView mDurationView;
    public TextView mInviteesNumberView;

    public EventDetailsViewHolder(View view) {
        this.view = view;
        mLocationButton = view.findViewById(R.id.btn_main_event_details_location);
        mCallButton = view.findViewById(R.id.btn_main_event_details_call);
        mCalendarButton = view.findViewById(R.id.btn_main_event_details_calendar);
        mJoinButton = view.findViewById(R.id.btn_main_event_details_join);
        mNameView = view.findViewById(R.id.tv_event_details_name);
        mHostsView = view.findViewById(R.id.tv_event_details_hosts);
        mPlaceView = view.findViewById(R.id.tv_event_details_place);
        mStreetView = view.findViewById(R.id.tv_event_details_street);
        mPostalCodeView = view.findViewById(R.id.tv_event_details_postal_code);
        mTimeView = view.findViewById(R.id.tv_event_details_time);
        mDurationView = view.findViewById(R.id.tv_event_details_duration);
        mInviteesNumberView = view.findViewById(R.id.tv_event_details_invitees_number);

    }
}

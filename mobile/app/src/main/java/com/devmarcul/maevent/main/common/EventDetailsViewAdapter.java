package com.devmarcul.maevent.main.common;

import android.view.View;

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;

public class EventDetailsViewAdapter
        implements ContentAdapter<Maevent, EventDetailsViewHolder>, View.OnClickListener {

    private EventDetailsViewHolder viewHolder;
    private OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClickCall();
        void onClickLocation();
        void onClickCalendar();
        void onClickJoin();
    }

    public EventDetailsViewAdapter(OnClickHandler handler, View viewHolderView) {
        mClickHandler = handler;
        viewHolder = new EventDetailsViewHolder(viewHolderView);
    }

    @Override
    public void adaptContent(Maevent event) {
        MaeventParams params = event.getParams();
        String eventName = params.name;
        String hosts = "Michael Block";
        String place = params.place;
        String street = params.addressStreet;
        String postCode = params.addressPostCode;
        String startTime = params.startTime;
        String stopTime = params.stopTime;
        String inviteesNumber = String.valueOf(0);
        String time = startTime + " - " + stopTime;
        //TODO Add duration calculation
        String duration = "6 Hrs";

        viewHolder.mNameView.setText(eventName);
        viewHolder.mHostsView.setText(hosts);
        viewHolder.mPlaceView.setText(place);
        viewHolder.mStreetView.setText(street);
        viewHolder.mPostalCodeView.setText(postCode);
        viewHolder.mTimeView.setText(time);
        viewHolder.mDurationView.setText(duration);
        viewHolder.mInviteesNumberView.setText(inviteesNumber);
    }

    public void adaptJoinButton(boolean visible) {
        if (visible) {
            viewHolder.mJoinButton.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.mJoinButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindOnClickListeners() {
        viewHolder.mLocationButton.setOnClickListener(this);
        viewHolder.mCallButton.setOnClickListener(this);
        viewHolder.mCalendarButton.setOnClickListener(this);
        viewHolder.mJoinButton.setOnClickListener(this);
    }

    @Override
    public void unbindOnClickListeners() {
        viewHolder.mLocationButton.setOnClickListener(null);
        viewHolder.mCallButton.setOnClickListener(null);
        viewHolder.mCalendarButton.setOnClickListener(null);
        viewHolder.mJoinButton.setOnClickListener(null);
    }

    @Override
    public EventDetailsViewHolder getViewHolder() {
        return viewHolder;
    }

    @Override
    public void onClick(View v) {
        if (v == viewHolder.mCallButton) {
            mClickHandler.onClickCall();
        }
        else if (v == viewHolder.mLocationButton) {
            mClickHandler.onClickLocation();
        }
        else if (v == viewHolder.mCalendarButton) {
            mClickHandler.onClickCalendar();
        }
        else if (v == viewHolder.mJoinButton) {
            mClickHandler.onClickJoin();
        }
    }
}

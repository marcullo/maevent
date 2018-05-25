package com.devmarcul.maevent.main.common;

import android.view.View;

import com.devmarcul.maevent.business_logic.MaeventSteward;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.utils.Utils;

import java.util.Calendar;

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
        int hostId = event.getHostId();

        String eventName = params.name;
        String host = MaeventSteward.getHostName(hostId);
        String place = params.place;
        String street = params.addressStreet;
        String postCode = params.addressPostCode;
        String time = getTimeSting(params.beginTime, params.endTime);
        String usersNumber = String.valueOf(event.getAttendeesNr());
        //TODO Consider replacing with remaining time
        String duration = getDurationString(params.beginTime, params.endTime);

        viewHolder.mNameView.setText(eventName);
        viewHolder.mHostView.setText(host);
        viewHolder.mPlaceView.setText(place);
        viewHolder.mStreetView.setText(street);
        viewHolder.mPostalCodeView.setText(postCode);
        viewHolder.mTimeView.setText(time);
        viewHolder.mDurationView.setText(duration);
        viewHolder.mUsersNumberView.setText(usersNumber);
    }

    public void adaptUsersNumber(Maevent event, boolean usersAreAttendees) {
        String usersNumber;
        if (usersAreAttendees) {
            usersNumber = String.valueOf(event.getAttendeesNr());
        }
        else {
            usersNumber = String.valueOf(((Invitation)event).getInviteesNr());
        }
        viewHolder.mUsersNumberView.setText(usersNumber);
    }

    public void adaptJoinButton(boolean visible) {
        if (visible) {
            viewHolder.mJoinButton.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.mJoinButton.setVisibility(View.GONE);
        }
    }

    private String getTimeSting(String start, String end) {
        String startTime = Utils.convertTimeStringToOtherFormat(start,
                MaeventParams.TIME_FORMAT, EventDetailsViewHolder.TIME_FORMAT);
        String stopTime = Utils.convertTimeStringToOtherFormat(end,
                MaeventParams.TIME_FORMAT, EventDetailsViewHolder.TIME_FORMAT);
        return Utils.getTimeStringFromStringDuration(startTime, stopTime, EventDetailsViewHolder.TIME_FORMAT);
    }

    private String getDurationString(String start, String end) {
        Calendar startDate = Utils.getCalendarFromString(start, MaeventParams.TIME_FORMAT);
        Calendar endDate = Utils.getCalendarFromString(end, MaeventParams.TIME_FORMAT);
        return Utils.getStringDurationFromCalendarDuration(startDate, endDate);
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

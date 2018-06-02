package com.devmarcul.maevent.main.common;

import android.view.View;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.common.ContentAdapter;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.utils.StringUtils;
import com.devmarcul.maevent.utils.TimeUtils;

import java.util.Calendar;

public class EventDetailsViewAdapter implements
        ContentAdapter<Maevent, EventDetailsViewHolder>,
        View.OnClickListener {

    private EventDetailsViewHolder mViewHolder;
    private OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClickCall();
        void onClickLocation();
        void onClickCalendar();
        void onClickJoin();
    }

    public EventDetailsViewAdapter(OnClickHandler handler, View viewHolderView) {
        mClickHandler = handler;
        mViewHolder = new EventDetailsViewHolder(viewHolderView);
    }

    @Override
    public void adaptContent(Maevent event) {
        //TODO get bundle with all necessary data instead of only event
        MaeventParams params = event.getParams();
        String eventName = params.name;
        //TODO Host !
        StringBuilder builder = new StringBuilder();
        String hostFirstName = event.getHost().getProfile().firstName;
        String hostLastName = event.getHost().getProfile().lastName;
        builder.append(hostFirstName).append(StringUtils.getNewLine()).append(hostLastName);
        String host = builder.toString();
        String place = params.place;
        String street = params.addressStreet;
        String postCode = params.addressPostCode;
        String time = getTimeSting(params.beginTime, params.endTime);
        //TODO Add parsing attendees ids count
        String usersNumber = String.valueOf("0");
        //TODO Consider replacing with remaining time
        String duration = getDurationString(params.beginTime, params.endTime);

        mViewHolder.mNameView.setText(eventName);
        mViewHolder.mHostView.setText(host);
        mViewHolder.mPlaceView.setText(place);
        mViewHolder.mStreetView.setText(street);
        mViewHolder.mPostalCodeView.setText(postCode);
        mViewHolder.mTimeView.setText(time);
        mViewHolder.mDurationView.setText(duration);
        mViewHolder.mUsersNumberView.setText(usersNumber);
    }

    public void adaptUsersNumber(Maevent event, boolean usersAreAttendees) {
        String usersNumber;
        int usersNumberIconRes;

        if (usersAreAttendees) {
            //TODO Add parsing attendees ids count
            usersNumber = String.valueOf("0");
            usersNumberIconRes = R.drawable.ic_people;
        }
        else {
            usersNumber = String.valueOf(((Invitation)event).getInviteesNr());
            usersNumberIconRes = R.drawable.ic_confusion;
        }

        mViewHolder.mUsersNumberView.setText(usersNumber);
        mViewHolder.mUsersNumberIcon.setBackgroundResource(usersNumberIconRes);
    }

    public void adaptJoinButton(boolean visible) {
        if (visible) {
            mViewHolder.mJoinButton.setVisibility(View.VISIBLE);
        }
        else {
            mViewHolder.mJoinButton.setVisibility(View.GONE);
        }
    }

    private String getTimeSting(String start, String end) {
        String startTime = TimeUtils.convertTimeStringToOtherFormat(start,
                MaeventParams.TIME_FORMAT, EventDetailsViewHolder.TIME_FORMAT);
        String stopTime = TimeUtils.convertTimeStringToOtherFormat(end,
                MaeventParams.TIME_FORMAT, EventDetailsViewHolder.TIME_FORMAT);
        return TimeUtils.getTimeStringFromStringDuration(startTime, stopTime, EventDetailsViewHolder.TIME_FORMAT);
    }

    private String getDurationString(String start, String end) {
        Calendar startDate = TimeUtils.getCalendarFromString(start, MaeventParams.TIME_FORMAT);
        Calendar endDate = TimeUtils.getCalendarFromString(end, MaeventParams.TIME_FORMAT);
        return TimeUtils.getStringDurationFromCalendarDuration(startDate, endDate);
    }

    @Override
    public void bindListeners() {
        mViewHolder.mLocationButton.setOnClickListener(this);
        mViewHolder.mCallButton.setOnClickListener(this);
        mViewHolder.mCalendarButton.setOnClickListener(this);
        mViewHolder.mJoinButton.setOnClickListener(this);
    }

    @Override
    public void unbindListeners() {
        mViewHolder.mLocationButton.setOnClickListener(null);
        mViewHolder.mCallButton.setOnClickListener(null);
        mViewHolder.mCalendarButton.setOnClickListener(null);
        mViewHolder.mJoinButton.setOnClickListener(null);
    }

    @Override
    public EventDetailsViewHolder getViewHolder() {
        return mViewHolder;
    }

    @Override
    public void onClick(View v) {
        if (v == mViewHolder.mCallButton) {
            mClickHandler.onClickCall();
        }
        else if (v == mViewHolder.mLocationButton) {
            mClickHandler.onClickLocation();
        }
        else if (v == mViewHolder.mCalendarButton) {
            mClickHandler.onClickCalendar();
        }
        else if (v == mViewHolder.mJoinButton) {
            mClickHandler.onClickJoin();
        }
    }
}

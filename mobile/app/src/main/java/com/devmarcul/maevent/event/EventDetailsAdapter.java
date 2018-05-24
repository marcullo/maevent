package com.devmarcul.maevent.event;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;

public class EventDetailsAdapter implements View.OnClickListener {

    private ViewHolder viewHolder;
    private OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClickCall();
        void onClickLocation();
        void onClickCalendar();
        void onClickJoin();
    }

    public EventDetailsAdapter(OnClickHandler handler, View itemView) {
        mClickHandler = handler;
        viewHolder = new ViewHolder(itemView);
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

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        private View view;
        private final ImageButton mLocationButton;
        private final ImageButton mCallButton;
        private final ImageButton mCalendarButton;
        private final Button mJoinButton;
        private final TextView mNameView;
        private final TextView mHostsView;
        private final TextView mPlaceView;
        private final TextView mStreetView;
        private final TextView mPostalCodeView;
        private final TextView mTimeView;
        private final TextView mDurationView;
        private final TextView mInviteesNumberView;

        public ViewHolder(View itemView) {
            view = itemView;
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

        public Button getJoinButton() {
            return mJoinButton;
        }
    }

    public void adaptContent(Maevent event) {
        //TODO Replace with Content Provider / etc.
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

    public void bindOnClickListeners() {
        viewHolder.mLocationButton.setOnClickListener(this);
        viewHolder.mCallButton.setOnClickListener(this);
        viewHolder.mCalendarButton.setOnClickListener(this);
        viewHolder.mJoinButton.setOnClickListener(this);
    }

    public void unbindOnClickListeners() {
        viewHolder.mLocationButton.setOnClickListener(null);
        viewHolder.mCallButton.setOnClickListener(null);
        viewHolder.mCalendarButton.setOnClickListener(null);
        viewHolder.mJoinButton.setOnClickListener(null);

    }
}

package com.devmarcul.maevent.event;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devmarcul.maevent.R;

public class EventDetailsAdapter implements View.OnClickListener {

    private ViewHolder viewHolder;
    private OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClickCall();
        void onClickLocation();
        void onClickCalendar();
    }

    public EventDetailsAdapter(OnClickHandler handler, View itemView) {
        mClickHandler = handler;
        viewHolder = new ViewHolder(itemView);
    }

    @Override
    public void onClick(View v) {
        if (v == viewHolder.mCallView) {
            mClickHandler.onClickCall();
        }
        else if (v == viewHolder.mLocationView) {
            mClickHandler.onClickLocation();
        }
        else if (v == viewHolder.mCalendarView) {
            mClickHandler.onClickCalendar();
        }
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public class ViewHolder {
        private View view;
        private final ImageButton mLocationView;
        private final ImageButton mCallView;
        private final ImageButton mCalendarView;
        private final TextView mNameView;
        private final TextView mHostsView;
        private final TextView mPlaceView;
        private final TextView mStreetView;
        private final TextView mPostalCodeView;
        private final TextView mTimeView;

        public ViewHolder(View itemView) {
            view = itemView;
            mLocationView = view.findViewById(R.id.btn_main_event_details_location);
            mCallView = view.findViewById(R.id.btn_main_event_details_call);
            mCalendarView = view.findViewById(R.id.btn_main_event_details_calendar);
            mNameView = view.findViewById(R.id.tv_event_details_name);
            mHostsView = view.findViewById(R.id.tv_event_details_hosts);
            mPlaceView = view.findViewById(R.id.tv_event_details_place);
            mStreetView = view.findViewById(R.id.tv_event_details_street);
            mPostalCodeView = view.findViewById(R.id.tv_event_details_postal_code);
            mTimeView = view.findViewById(R.id.tv_event_details_time);
        }
    }

    public void adaptContent(Maevent event) {
        //TODO Replace with Content Provider / etc.

        String eventName = event.getName();
        String hosts = event.getHosts();
        String place = event.getPlace();
        String street = event.getAddressStreet();
        String postCode = event.getAddressPostCode();
        String startTime = event.getStartTime();
        String stopTime = event.getStopTime();

        String time = startTime + " - " + stopTime;

        viewHolder.mNameView.setText(eventName);
        viewHolder.mHostsView.setText(hosts);
        viewHolder.mPlaceView.setText(place);
        viewHolder.mStreetView.setText(street);
        viewHolder.mPostalCodeView.setText(postCode);
        viewHolder.mTimeView.setText(time);
    }

    public void bindOnClickListeners() {
        viewHolder.mLocationView.setOnClickListener(this);
        viewHolder.mCallView.setOnClickListener(this);
        viewHolder.mCalendarView.setOnClickListener(this);
    }

    public void unbindOnClickListeners() {
        viewHolder.mLocationView.setOnClickListener(null);
        viewHolder.mCallView.setOnClickListener(null);
        viewHolder.mCalendarView.setOnClickListener(null);

    }
}

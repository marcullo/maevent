package com.devmarcul.maevent.event;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devmarcul.maevent.R;

public class EventDetailsViewHolder implements View.OnClickListener{
    private View view;
    private final ImageButton mLocationView;
    private final ImageButton mCallView;
    private final TextView mNameView;
    private final TextView mPlaceView;
    private final TextView mStreetView;
    private final TextView mPostalCodeView;
    private final TextView mHostsView;

    private final OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClickCall();
        void onClickLocation();
    }

    public EventDetailsViewHolder(OnClickHandler handler, View itemView) {
        mClickHandler = handler;
        view = itemView;
        mLocationView = view.findViewById(R.id.btn_main_event_details_location);
        mCallView = view.findViewById(R.id.btn_main_event_details_call);
        mNameView = view.findViewById(R.id.tv_event_details_name);
        mPlaceView = view.findViewById(R.id.tv_event_details_place);
        mStreetView = view.findViewById(R.id.tv_event_details_street);
        mPostalCodeView = view.findViewById(R.id.tv_event_details_postal_code);
        mHostsView = view.findViewById(R.id.tv_event_details_hosts);
    }

    @Override
    public void onClick(View v) {
        if (v == mCallView) {
            mClickHandler.onClickCall();
        }
        else if (v == mLocationView) {
            mClickHandler.onClickLocation();
        }
    }

    public View getView() {
        return view;
    }

    public void bindOnClickListeners() {
        mLocationView.setOnClickListener(this);
        mCallView.setOnClickListener(this);
    }

    public void unbindOnClickListeners() {
        mLocationView.setOnClickListener(null);
        mCallView.setOnClickListener(null);
    }

}

package com.devmarcul.maevent.invite;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.main.common.EventDetailsViewAdapter;

public class SimplifiedEventDetailsAdapter {

    private EventDetailsViewHolder mViewHolder;

    public SimplifiedEventDetailsAdapter(View viewHolderView) {
        mViewHolder = new EventDetailsViewHolder(viewHolderView);
    }

    public void adaptContent(Maevent event) {
        if (event == null) {
            return;
        }

        MaeventParams params = event.getParams();
        if (params == null) {
            return;
        }

        if (event.getHost() == null
                || event.getHost().getProfile() == null) {
            return;
        }

        String name = params.name;
        //TODO Refactor hierarchy
        String time = EventDetailsViewAdapter.getTimeSting(params.beginTime, params.endTime);
        String attendeesNr = String.valueOf(event.getAttendeesNumber());

        mViewHolder.mNameView.setText(name);
        mViewHolder.mTimeView.setText(time);
        mViewHolder.mAttendeesNrView.setText(attendeesNr);
    }

    public class EventDetailsViewHolder {

        final View view;
        public TextView mNameView;
        public TextView mTimeView;
        public TextView mAttendeesNrView;
        public ImageButton mRsvpView;

        public EventDetailsViewHolder(View itemView) {
            view = itemView;
            mNameView = view.findViewById(R.id.tv_event_details_name);
            mTimeView = view.findViewById(R.id.tv_event_details_time);
            mAttendeesNrView = view.findViewById(R.id.tv_event_details_users_number);
            mRsvpView = view.findViewById(R.id.btn_main_incoming_event_rsvp);
        }
    }
}

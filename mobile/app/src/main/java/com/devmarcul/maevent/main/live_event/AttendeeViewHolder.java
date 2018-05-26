package com.devmarcul.maevent.main.live_event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmarcul.maevent.R;

public abstract class AttendeeViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private View view;

    public ImageView mAttendeePhotoView;
    public TextView mAttendeeFirstName;
    public TextView mAttendeeLastName;
    public TextView mAttendeeLocationView;
    public TextView mAttendeeHeadlineView;


    public AttendeeViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        mAttendeePhotoView = view.findViewById(R.id.iv_person_photo);
        mAttendeeFirstName = view.findViewById(R.id.tv_person_first_name);
        mAttendeeLastName = view.findViewById(R.id.tv_person_last_name);
        mAttendeeLocationView = view.findViewById(R.id.tv_person_location);
        mAttendeeHeadlineView = view.findViewById(R.id.tv_person_headline);

        view.setOnClickListener(this);
    }
}

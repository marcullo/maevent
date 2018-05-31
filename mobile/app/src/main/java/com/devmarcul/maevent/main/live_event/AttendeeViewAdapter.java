package com.devmarcul.maevent.main.live_event;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;

public class AttendeeViewAdapter extends RecyclerView.Adapter<AttendeeViewHolder> {

    //TODO Replace with Content Provider / etc.
    private Attendees mAttendees;

    private OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClick(User attendee);
    }

    public AttendeeViewAdapter(OnClickHandler handler) {
        this.mClickHandler = handler;
    }

    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.main_profile;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new AttendeeViewHolder(view) {
            @Override
            public void onClick(View v) {
                int adapterPosition = getAdapterPosition();
                User attendee = mAttendees.get(adapterPosition);
                mClickHandler.onClick(attendee);
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position) {
        User attendee = mAttendees.get(position);
        adaptContent(holder, attendee);
    }

    @Override
    public int getItemCount() {
        if (mAttendees == null) {
            return 0;
        }
        return mAttendees.size();
    }

    public void setAttendeesData(Attendees attendees) {
        mAttendees = attendees;
        notifyDataSetChanged();
    }

    public void adaptContent(AttendeeViewHolder holder, User attendee) {
        UserProfile profile = attendee.getProfile();

        //TODO add user image
        holder.mAttendeePhotoView.setImageResource(R.mipmap.maevent_logo);
        holder.mAttendeeFirstName.setText(profile.firstName);
        holder.mAttendeeLastName.setText(profile.lastName);
        holder.mAttendeeLocationView.setText(profile.location);
        holder.mAttendeeHeadlineView.setText(profile.headline);
    }
}

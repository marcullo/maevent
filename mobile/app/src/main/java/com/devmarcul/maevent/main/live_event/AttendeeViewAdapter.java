package com.devmarcul.maevent.main.live_event;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserDummy;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.data.Users;

public class AttendeeViewAdapter extends RecyclerView.Adapter<AttendeeViewHolder> {

    // Watch out! attendees data has always evem number of elems.
    // In case of odd number of attendees one dummy elem is added.
    private Users mAttendees;
    private int mDummyPosition;

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

                if (attendee instanceof UserDummy) {
                    return;
                }

                mClickHandler.onClick(attendee);
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position) {
        User attendee = mAttendees.get(position);

        if (attendee instanceof UserDummy) {
            adaptDummyContent(holder);
        }
        else {
            adaptContent(holder, attendee);
        }
    }

    @Override
    public int getItemCount() {
        if (mAttendees == null) {
            return 0;
        }
        int size = mAttendees.size();
        /*if (mAttendees.getLast() instanceof UserDummy) {
            size--;
        }*/
        return size;
    }

    public void setAttendeesData(Users attendees) {
        mAttendees = attendees;

        final int size = attendees.size();
        final boolean dummyShouldBeUsed = size % 2 == 1;

        if (dummyShouldBeUsed) {
            // For better user experience
            mAttendees.add(new UserDummy());
        }

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

    public void adaptDummyContent(AttendeeViewHolder holder) {
        holder.mAttendeePhotoView.setImageResource(R.drawable.ic_refresh_smaller);
    }
}

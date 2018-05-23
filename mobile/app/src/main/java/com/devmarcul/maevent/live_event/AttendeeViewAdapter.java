package com.devmarcul.maevent.live_event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmarcul.maevent.R;

public class AttendeeViewAdapter extends RecyclerView.Adapter<AttendeeViewHolder>
        implements View.OnClickListener {

    //TODO Replace with Content Provider / etc.
    private Attendees mAttendees;
    private Context context;

    private OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClick();
    }

    public AttendeeViewAdapter(OnClickHandler handler, Context context, Attendees attendees) {
        this.mClickHandler = handler;
        this.context = context;
        this.mAttendees = attendees;
    }

    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_person, null);
        AttendeeViewHolder gvh = new AttendeeViewHolder(layoutView);
        layoutView.setOnClickListener(this);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position) {
        //TODO Replace with real profile after making it non-static
        holder.mAttendeeFirstName.setText("Andrew");
        holder.mAttendeeLastName.setText("Block");
        holder.mAttendeeLocationView.setText("Warsaw");
        holder.mAttendeeHeadlineView.setText("A person who never made a mistake never tried anything new.");
    }

    @Override
    public int getItemCount() {
        if (mAttendees == null) {
            return 0;
        }
        return mAttendees.size();
    }

    @Override
    public void onClick(View v) {
        mClickHandler.onClick();
    }
}

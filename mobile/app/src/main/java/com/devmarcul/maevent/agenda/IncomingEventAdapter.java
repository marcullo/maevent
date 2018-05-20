package com.devmarcul.maevent.agenda;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.event.Maevent;
import com.devmarcul.maevent.event.Maevents;

public class IncomingEventAdapter extends RecyclerView.Adapter<IncomingEventAdapter.IncomingEventAdapterViewHolder> {
    //TODO Replace with Content Provider / etc.
    private Maevents mIncomingEvents;
    private final IncomingEventAdapterOnClickHandler mClickHandler;

    public interface IncomingEventAdapterOnClickHandler {
        void onClick(Maevent eventData);
    }

    public IncomingEventAdapter(IncomingEventAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public IncomingEventAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.main_agenda_incoming_event;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, viewGroup, false);
        return new IncomingEventAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomingEventAdapterViewHolder holder, int position) {
        Maevent event = mIncomingEvents.get(position);
        adaptContent(holder, event);
    }

    @Override
    public int getItemCount() {
        if (mIncomingEvents == null) {
            return 0;
        }
        return mIncomingEvents.size();
    }

    public void setIncomingEventsData(Maevents incomingEventsData) {
        mIncomingEvents = incomingEventsData;
        notifyDataSetChanged();
    }

    public class IncomingEventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageButton rsvpView;
        final ImageButton locationView;
        final TextView eventNameView;
        final TextView eventPlaceView;
        final TextView eventTimeView;

        IncomingEventAdapterViewHolder(View itemView) {
            super(itemView);
            rsvpView = itemView.findViewById(R.id.btn_main_incoming_event_rsvp);
            locationView = itemView.findViewById(R.id.btn_main_incoming_event_location);
            eventNameView = itemView.findViewById(R.id.tv_main_incoming_event_name);
            eventPlaceView = itemView.findViewById(R.id.tv_main_incoming_event_place);
            eventTimeView = itemView.findViewById(R.id.tv_main_incoming_event_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Maevent incomingEventData = mIncomingEvents.get(adapterPosition);
            mClickHandler.onClick(incomingEventData);
        }
    }

    private void adaptContent(IncomingEventAdapterViewHolder holder, Maevent event) {
        String eventName = getEventName(event);
        boolean rsvp = event.hasRsvp();
        boolean confirmed = event.wasConfirmed();
        String place = event.getPlace();
        String time = getEventTime(event);

        holder.eventNameView.setText(eventName);
        holder.eventPlaceView.setText(place);
        holder.eventTimeView.setText(time);

        if (rsvp && !confirmed) {
            holder.rsvpView.setVisibility(View.VISIBLE);
        }
        else {
            holder.rsvpView.setVisibility(View.GONE);
        }
    }

    private String getEventName(Maevent event) {
        return event.getName();
    }

    private String getEventTime(Maevent event) {
        //TODO Get event time
        return "IN 2 HOURS";
    }

}
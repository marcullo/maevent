package com.devmarcul.maevent.agenda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.event.Maevent;
import com.devmarcul.maevent.event.Maevents;

public class IncomingEventAdapter extends RecyclerView.Adapter<IncomingEventAdapter.IncomingEventAdapterViewHolder> {

    //TODO Replace with Content Provider / etc.
    private Maevents mIncomingEvents;
    private final IncomingEventAdapterOnClickHandler mClickHandler;

    public interface IncomingEventAdapterOnClickHandler {
        void onClick(Maevent eventData);
        Maevent onClickRsvp(Maevent eventData);
        void onClickCall(Maevent eventData);
        void onClickLocation(Maevent eventData);
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

    public Maevents updateIncomingEventsData(int pos, Maevent event) {
        mIncomingEvents.set(pos, event);
        notifyDataSetChanged();
        return mIncomingEvents;
    }

    public class IncomingEventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final View view;
        final ImageButton rsvpView;
        final ImageButton locationView;
        final ImageButton callView;
        final TextView eventNameView;
        final TextView eventPlaceView;
        final TextView eventTimeView;

        IncomingEventAdapterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            rsvpView = view.findViewById(R.id.btn_main_incoming_event_rsvp);
            callView = view.findViewById(R.id.btn_main_incoming_event_call);
            locationView = view.findViewById(R.id.btn_main_incoming_event_location);
            eventNameView = view.findViewById(R.id.tv_main_incoming_event_name);
            eventPlaceView = view.findViewById(R.id.tv_main_incoming_event_place);
            eventTimeView = view.findViewById(R.id.tv_main_incoming_event_time);

            view.setOnClickListener(this);
            rsvpView.setOnClickListener(this);
            callView.setOnClickListener(this);
            locationView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Maevent incomingEventData = mIncomingEvents.get(adapterPosition);

            if (v == rsvpView) {
                Maevent event = mClickHandler.onClickRsvp(incomingEventData);
                mIncomingEvents = updateIncomingEventsData(adapterPosition, event);
            }
            else if (v == callView) {
                mClickHandler.onClickCall(incomingEventData);
            }
            else if (v == locationView) {
                mClickHandler.onClickLocation(incomingEventData);
            }
            else {
                mClickHandler.onClick(incomingEventData);
            }
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

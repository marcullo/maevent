package com.devmarcul.maevent.main.agenda;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.Maevents;

public class IncomingEventAdapter
        extends RecyclerView.Adapter<IncomingEventAdapter.IncomingEventAdapterViewHolder> {

    //TODO Replace with Content Provider / etc.
    private Maevents mIncomingEvents;
    private final IncomingEventAdapterOnClickHandler mClickHandler;

    public interface IncomingEventAdapterOnClickHandler {
        void onClick(Maevent eventData);
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
        //TODO refactor
        adaptPendingEvent(holder,
                MainActivity.pendingEvent != null && MainActivity.pendingEvent.getId() == event.getId());
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

    public Maevents appendIncomingEventsData(Maevent event) {
        mIncomingEvents.add(event);
        notifyDataSetChanged();
        return mIncomingEvents;
    }

    public Maevents updateIncomingEventsData(int pos, Maevent event) {
        mIncomingEvents.set(pos, event);
        notifyDataSetChanged();
        return mIncomingEvents;
    }

    public class IncomingEventAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final View view;
        final View pendingEventView;
        final ImageButton locationView;
        final ImageButton callView;
        final TextView eventNameView;
        final TextView eventPlaceView;
        final TextView eventTimeView;

        IncomingEventAdapterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            pendingEventView = view.findViewById(R.id.v_agenda_pending_event);
            callView = view.findViewById(R.id.btn_main_incoming_event_call);
            locationView = view.findViewById(R.id.btn_main_incoming_event_location);
            eventNameView = view.findViewById(R.id.tv_main_incoming_event_name);
            eventPlaceView = view.findViewById(R.id.tv_main_incoming_event_place);
            eventTimeView = view.findViewById(R.id.tv_main_incoming_event_time);

            view.setOnClickListener(this);
            callView.setOnClickListener(this);
            locationView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Maevent event = mIncomingEvents.get(adapterPosition);

            if (v == callView) {
                mClickHandler.onClickCall(event);
            }
            else if (v == locationView) {
                mClickHandler.onClickLocation(event);
            }
            else {
                mClickHandler.onClick(event);
            }
        }
    }

    private void adaptContent(IncomingEventAdapterViewHolder holder, Maevent event) {
        String eventName = getEventName(event);
        String place = event.getParams().place;
        String time = getEventTime(event);

        holder.eventNameView.setText(eventName);
        holder.eventPlaceView.setText(place);
        holder.eventTimeView.setText(time);
    }

    public void adaptPendingEvent(IncomingEventAdapterViewHolder holder, boolean pendingEvent) {
        int pendingColor = ContextCompat.getColor(holder.view.getContext(), R.color.colorAccent);
        int color = pendingEvent ? pendingColor : Color.TRANSPARENT;
        holder.pendingEventView.setBackgroundColor(color);
    }

    private String getEventName(Maevent event) {
        return event.getParams().name;
    }

    private String getEventTime(Maevent event) {
        //TODO Get event time
        return "IN 2 HOURS";
    }
}

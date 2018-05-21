package com.devmarcul.maevent.agenda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.event.Invitation;
import com.devmarcul.maevent.event.Invitations;

public class InvitationAdapter
        extends RecyclerView.Adapter<InvitationAdapter.InvitationAdapterViewHolder> {

    //TODO Replace with Content Provider / etc.
    private Invitations mInvitations;

    private final InvitationAdapter.InvitationAdapterOnClickHandler mClickHandler;

    public interface InvitationAdapterOnClickHandler {
        void onClick(Invitation invitationData);
    }

    public InvitationAdapter(InvitationAdapter.InvitationAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public InvitationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.main_agenda_invitation;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, viewGroup, false);
        return new InvitationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationAdapterViewHolder holder, int position) {
        Invitation invitation = mInvitations.get(position);
        adaptContent(holder, invitation);
    }

    @Override
    public int getItemCount() {
        if (mInvitations == null) {
            return 0;
        }
        return mInvitations.size();
    }

    public void setIncomingEventsData(Invitations invitationsData) {
        mInvitations = invitationsData;
        notifyDataSetChanged();
    }

    public Invitations updateInvitationsData(int pos, Invitation invitation) {
        mInvitations.set(pos, invitation);
        notifyDataSetChanged();
        return mInvitations;
    }

    public Invitations removeIntivation(int pos) {
        mInvitations.remove(pos);
        notifyItemRemoved(pos);
        return mInvitations;
    }

    public class InvitationAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final View view;
        final TextView eventNameView;
        final TextView eventPlaceView;
        final TextView eventHostView;
        final TextView eventTimeView;

        public InvitationAdapterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            eventNameView = view.findViewById(R.id.tv_main_invitation_event_name);
            eventPlaceView = view.findViewById(R.id.tv_main_invitation_event_place);
            eventHostView =  view.findViewById(R.id.tv_main_invitation_host);
            eventTimeView = view.findViewById(R.id.tv_main_invitation_event_time);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Invitation invitation = mInvitations.get(adapterPosition);
            mClickHandler.onClick(invitation);
        }
    }

    private void adaptContent(InvitationAdapterViewHolder holder, Invitation invitation) {
        String eventName = invitation.getEventName();
        //TODO Replace with array/list
        String place = invitation.getEventPlace();
        String hosts = invitation.getEventHosts();
        String time = invitation.getEventStartTime();

        holder.eventNameView.setText(eventName);
        holder.eventPlaceView.setText(place);
        holder.eventHostView.setText(hosts);
        holder.eventTimeView.setText(time);
    }
}

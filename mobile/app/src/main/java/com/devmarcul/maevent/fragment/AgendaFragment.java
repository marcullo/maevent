package com.devmarcul.maevent.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.agenda.IncomingEventAdapter;
import com.devmarcul.maevent.agenda.InvitationAdapter;
import com.devmarcul.maevent.agenda.ItemViewHolder;
import com.devmarcul.maevent.dialog.EventDetailsDialog;
import com.devmarcul.maevent.event.EventDetailsAdapter;
import com.devmarcul.maevent.event.Invitation;
import com.devmarcul.maevent.event.Invitations;
import com.devmarcul.maevent.event.Maevent;
import com.devmarcul.maevent.event.Maevents;
import com.devmarcul.maevent.interfaces.ViewScroller;
import com.devmarcul.maevent.helper.SwipeAcceptDeleteCallback;
import com.devmarcul.maevent.utils.tools.Prompt;

public class AgendaFragment extends Fragment implements
        IncomingEventAdapter.IncomingEventAdapterOnClickHandler,
        InvitationAdapter.InvitationAdapterOnClickHandler,
        ViewScroller {

    private View view;
    private Activity parent;

    private Maevent mFocusedEvent;
    private Invitation mFocusedInvitation;

    private Maevents mIncomingEventsData;
    private Handler mIncomingEventsHandler;
    private RecyclerView mIncomingEventsRecyclerView;
    private IncomingEventAdapter mIncomingEventAdapter;
    private ItemViewHolder mIncomingEventsLabel;

    private Invitations mInvitationsData;
    private Handler mInvitationsHandler;
    private RecyclerView mInvitationsRecyclerView;
    private InvitationAdapter mInvitationAdapter;
    private ItemViewHolder mInvitationsLabel;

    private View mEventDetailsView;
    private EventDetailsDialog mEventDetailsDialog;
    private EventDetailsAdapter mEventDetailsAdapter;
    private EventDetailsAdapter.ViewHolder mEventDetailsViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_agenda, container, false);
        mEventDetailsView = inflater.inflate(R.layout.main_event_details, container, false);
        parent = getActivity();

        initIncomingEvents();
        initInvitations();
        initEventDetailsDialog();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIncomingEventsHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(Maevent event) {
        mFocusedEvent = event;
        mFocusedInvitation = null;
        mEventDetailsAdapter.adaptContent(event);
        mEventDetailsAdapter.bindOnClickListeners();
        mEventDetailsDialog.show();
    }

    @Override
    public void onClick(Invitation invitationData) {
        Maevent event = invitationData.getEvent();
        mFocusedEvent = null;
        mFocusedInvitation = invitationData;
        mEventDetailsAdapter.adaptContent(event);
        mEventDetailsAdapter.bindOnClickListeners();
        mEventDetailsDialog.show();
    }

    @Override
    public Maevent onClickRsvp(Maevent eventData) {
        Maevent data = eventData;
        Prompt.displayShort("TODO Presence confirmed", parent);
        data.confirm(true);
        return data;
    }

    @Override
    public void onClickCall(Maevent eventData) {
        Prompt.displayShort("TODO Add call organizer", parent);
    }

    @Override
    public void onClickLocation(Maevent eventData) {
        Prompt.displayShort("TODO Add show on map", parent);
    }

    public NestedScrollView getScrollView() {
        NestedScrollView view = this.view.findViewById(R.id.sv_main_agenda);
        return view;
    }

    private void initIncomingEvents() {
        //TODO Load content
        mIncomingEventsData = new Maevents();
        Maevent otherEvent = new Maevent();
        otherEvent.updateContent();
        otherEvent.setName("Kate's birthday");
        mIncomingEventsData.add(otherEvent);
        for (int i = 1; i < 10; i++) {
            mIncomingEventsData.add(new Maevent());
            mIncomingEventsData.get(i).updateContent();
        }

        mIncomingEventsRecyclerView = view.findViewById(R.id.rv_incoming_events);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false);
        mIncomingEventsRecyclerView.setLayoutManager(layoutManager);
        mIncomingEventsRecyclerView.setHasFixedSize(false);

        mIncomingEventAdapter = new IncomingEventAdapter(this);
        mIncomingEventsRecyclerView.setAdapter(mIncomingEventAdapter);

        final View incomingEventsLabelView = view.findViewById(R.id.main_incoming_events_label);
        mIncomingEventsLabel = new ItemViewHolder(incomingEventsLabelView, mIncomingEventsRecyclerView);
        incomingEventsLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomingEventsLabel.toggle();
            }
        });

        mIncomingEventsHandler = new Handler();
        mIncomingEventsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar pb = view.findViewById(R.id.pb_incoming_events_number_loading_indicator);
                pb.setVisibility(View.VISIBLE);
                mIncomingEventAdapter.setIncomingEventsData(mIncomingEventsData);
                pb.setVisibility(View.GONE);

                String size = String.valueOf(mIncomingEventsData.size());
                TextView incomingEventsNumberView = view.findViewById(R.id.tv_incoming_events_number);
                incomingEventsNumberView.setText(size);
                incomingEventsNumberView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private void initInvitations() {
        //TODO Load content
        mInvitationsData = new Invitations();
        for (int i = 0; i < 10; i++) {
            mInvitationsData.add(new Invitation());
            mInvitationsData.get(i).updateContent();
        }

        mInvitationsRecyclerView = view.findViewById(R.id.rv_invitations);
        mInvitationsRecyclerView.addItemDecoration(new DividerItemDecoration(parent, DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false);
        mInvitationsRecyclerView.setLayoutManager(layoutManager);
        mInvitationsRecyclerView.setHasFixedSize(false);

        SwipeAcceptDeleteCallback swipeHandler = new SwipeAcceptDeleteCallback(parent, new SwipeAcceptDeleteCallback.SwipedCallback() {
            @Override
            public void onAccept(RecyclerView.ViewHolder viewHolder) {
                acceptInvitation(viewHolder);
            }

            @Override
            public void onDelete(RecyclerView.ViewHolder viewHolder) {
                InvitationAdapter adapter = (InvitationAdapter) mInvitationsRecyclerView.getAdapter();
                int pos = viewHolder.getAdapterPosition();
                mInvitationsData = adapter.removeIntivation(pos);

                String size = String.valueOf(mInvitationsData.size());
                TextView invitationsNumberView = view.findViewById(R.id.tv_invitations_number);
                invitationsNumberView.setText(size);
                invitationsNumberView.setVisibility(View.VISIBLE);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHandler);
        itemTouchHelper.attachToRecyclerView(mInvitationsRecyclerView);

        mInvitationAdapter = new InvitationAdapter(this);
        mInvitationsRecyclerView.setAdapter(mInvitationAdapter);

        final View invitationsLabelView = view.findViewById(R.id.main_invitations_label);
        mInvitationsLabel = new ItemViewHolder(invitationsLabelView, mInvitationsRecyclerView);
        invitationsLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInvitationsLabel.toggle();
            }
        });

        mInvitationsHandler = new Handler();
        mInvitationsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar pb = view.findViewById(R.id.pb_invitations_number_loading_indicator);
                pb.setVisibility(View.VISIBLE);
                mInvitationAdapter.setIncomingEventsData(mInvitationsData);
                pb.setVisibility(View.GONE);

                String size = String.valueOf(mInvitationsData.size());
                TextView invitationsNumberView = view.findViewById(R.id.tv_invitations_number);
                invitationsNumberView.setText(size);
                invitationsNumberView.setVisibility(View.VISIBLE);

            }
        }, 1000);

        mInvitationsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void initEventDetailsDialog() {
        View eventDetailsView = mEventDetailsView.findViewById(R.id.main_event_details);
        EventDetailsAdapter.OnClickHandler onClickHandler =  new EventDetailsAdapter.OnClickHandler() {
            @Override
            public void onClickCall() {
                Prompt.displayTodo(parent);
            }

            @Override
            public void onClickLocation() {
                Prompt.displayTodo(parent);
            }

            @Override
            public void onClickCalendar() {
                Prompt.displayTodo(parent);
            }

            @Override
            public void onClickJoin() {
                EventDetailsAdapter.ViewHolder holder = mEventDetailsAdapter.getViewHolder();
                Button joinButton = holder.getJoinButton();
                joinButton.setVisibility(View.GONE);
                if (mFocusedInvitation != null) {
                    acceptInvitation(mFocusedInvitation);
                }
                //TODO Refactor
                MainActivity.pendingEvent = mFocusedEvent;
            }
        };
        mEventDetailsAdapter = new EventDetailsAdapter(onClickHandler, eventDetailsView);
        mEventDetailsViewHolder = mEventDetailsAdapter.getViewHolder();
        EventDetailsDialog.Builder builder = new EventDetailsDialog.Builder(parent, eventDetailsView);
        mEventDetailsDialog = builder.build();
    }

    private void acceptInvitation(RecyclerView.ViewHolder viewHolder) {
        InvitationAdapter invitationAdapter = (InvitationAdapter) mInvitationsRecyclerView.getAdapter();
        int pos = viewHolder.getAdapterPosition();

        Invitation invitation = mInvitationsData.get(pos);
        Maevent event = invitation.getEvent();


        IncomingEventAdapter incomingEventAdapter = (IncomingEventAdapter) mIncomingEventsRecyclerView.getAdapter();
        mIncomingEventsData = incomingEventAdapter.appendIncomingEventsData(event);

        String size = String.valueOf(mIncomingEventsData.size());
        TextView incomingEventsNumberView = view.findViewById(R.id.tv_incoming_events_number);
        incomingEventsNumberView.setText(size);
        incomingEventsNumberView.setVisibility(View.VISIBLE);


        mInvitationsData = invitationAdapter.removeIntivation(pos);

        size = String.valueOf(mInvitationsData.size());
        TextView invitationsNumberView = view.findViewById(R.id.tv_invitations_number);
        invitationsNumberView.setText(size);
        invitationsNumberView.setVisibility(View.VISIBLE);
    }

    private void acceptInvitation(Invitation invitation) {
        InvitationAdapter invitationAdapter = (InvitationAdapter) mInvitationsRecyclerView.getAdapter();

        int pos = 0;
        for (Invitation inv:
                mInvitationsData
             ) {
            if (inv == mInvitationsData.get(pos)) {
                break;
            }
            pos++;
        }
        if (pos == mInvitationsData.size()) {
            return;
        }

        Maevent event = invitation.getEvent();


        IncomingEventAdapter incomingEventAdapter = (IncomingEventAdapter) mIncomingEventsRecyclerView.getAdapter();
        mIncomingEventsData = incomingEventAdapter.appendIncomingEventsData(event);

        String size = String.valueOf(mIncomingEventsData.size());
        TextView incomingEventsNumberView = view.findViewById(R.id.tv_incoming_events_number);
        incomingEventsNumberView.setText(size);
        incomingEventsNumberView.setVisibility(View.VISIBLE);


        mInvitationsData = invitationAdapter.removeIntivation(pos);

        size = String.valueOf(mInvitationsData.size());
        TextView invitationsNumberView = view.findViewById(R.id.tv_invitations_number);
        invitationsNumberView.setText(size);
        invitationsNumberView.setVisibility(View.VISIBLE);
    }
}

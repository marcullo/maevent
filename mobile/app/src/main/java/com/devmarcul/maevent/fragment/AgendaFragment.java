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
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.devmarcul.maevent.utils.SwipeAcceptDeleteCallback;
import com.devmarcul.maevent.utils.tools.Prompt;

public class AgendaFragment extends Fragment implements
        IncomingEventAdapter.IncomingEventAdapterOnClickHandler,
        InvitationAdapter.InvitationAdapterOnClickHandler,
        ViewScroller {

    private View view;
    private Activity parent;

    private Maevents mIncomingEventsData;
    private Handler mIncomingEventsHandler;
    private RecyclerView mIncomingEventsRecyclerView;
    private IncomingEventAdapter mIncomingEventAdapter;
    private ItemViewHolder mIncomingEventsLabel;

    private Invitations mInvitationsData;
    private Handler mInvitationsHandler;
    private RecyclerView mInvitationsRecyclerView;
    private InvitationAdapter mInvitationAdapter;

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
        mEventDetailsView = inflater.inflate(R.layout.main_agenda_event_details, container, false);
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
        mEventDetailsAdapter.adaptContent(event);
        mEventDetailsAdapter.bindOnClickListeners();
        mEventDetailsDialog.show();
    }

    @Override
    public void onClick(Invitation invitationData) {
        Prompt.displayTodo(parent);
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
        for (int i = 0; i < 10; i++) {
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
        };
        mEventDetailsAdapter = new EventDetailsAdapter(onClickHandler, eventDetailsView);
        mEventDetailsViewHolder = mEventDetailsAdapter.getViewHolder();
        EventDetailsDialog.Builder builder = new EventDetailsDialog.Builder(parent, eventDetailsView);
        mEventDetailsDialog = builder.build();
    }
}

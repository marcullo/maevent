package com.devmarcul.maevent.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.business_logic.MaeventSteward;
import com.devmarcul.maevent.content_providers.hardcoded.InvitationBuilder;
import com.devmarcul.maevent.content_providers.hardcoded.MaeventBuilder;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Invitations;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.Maevents;
import com.devmarcul.maevent.main.agenda.IncomingEventAdapter;
import com.devmarcul.maevent.main.agenda.InvitationAdapter;
import com.devmarcul.maevent.main.agenda.ItemViewHolder;
import com.devmarcul.maevent.main.common.EventDetailsHandler;
import com.devmarcul.maevent.main.common.EventDetailsViewAdapter;
import com.devmarcul.maevent.utils.CustomTittleSetter;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.devmarcul.maevent.utils.bottom_navig.ViewScroller;
import com.devmarcul.maevent.utils.SwipeAcceptDeleteCallback;

public class AgendaFragment extends Fragment implements
        IncomingEventAdapter.IncomingEventAdapterOnClickHandler,
        InvitationAdapter.InvitationAdapterOnClickHandler,
        ViewScroller,
        CustomTittleSetter {

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
    private ItemViewHolder mInvitationsLabel;

    private View mEventDetailsView;
    private View mEventDetailsContentView;
    private DetailsDialog mEventDetailsDialog;
    private EventDetailsViewAdapter mEventDetailsAdapter;
    private EventDetailsHandler mEventDetailsHandler;
    private ProgressBar mEventDetailsLoading;

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
    public void onStart() {
        super.onStart();
        setTitle();
    }

    @Override
    public void setTitle() {
        ActionBar bar = ((AppCompatActivity)parent).getSupportActionBar();
        if (bar != null) {
            int titleRes = R.string.toolbar_title_agenda;
            bar.setTitle(titleRes);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIncomingEventsHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(Maevent event) {
        boolean isPendingEvent = MainActivity.pendingEvent != null
                    && MainActivity.pendingEvent.getId() == event.getId();

        mEventDetailsHandler.focus(event, null);
        mEventDetailsLoading.setVisibility(View.VISIBLE);
        mEventDetailsContentView.setVisibility(View.INVISIBLE);
        mEventDetailsDialog.show();

        mEventDetailsAdapter.adaptContent(event);
        mEventDetailsAdapter.adaptUsersNumber(event, true);
        mEventDetailsAdapter.adaptJoinButton(!isPendingEvent);
        mEventDetailsAdapter.bindListeners();

        mEventDetailsLoading.setVisibility(View.INVISIBLE);
        mEventDetailsContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Invitation invitation) {
        mEventDetailsHandler.focus(null, invitation);
        mEventDetailsLoading.setVisibility(View.VISIBLE);
        mEventDetailsContentView.setVisibility(View.INVISIBLE);
        mEventDetailsDialog.show();

        mEventDetailsAdapter.adaptContent(invitation);
        mEventDetailsAdapter.adaptUsersNumber(invitation, false);
        mEventDetailsAdapter.adaptJoinButton(false);
        mEventDetailsAdapter.bindListeners();

        mEventDetailsLoading.setVisibility(View.INVISIBLE);
        mEventDetailsContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickCall(Maevent event) {
        //TODO Replace with bundle with all necessary data instead of event
        int id = event.getHostId();
        //TODO Host phone!
        MaeventSteward.callHost("+48123456789", parent);
    }

    @Override
    public void onClickLocation(Maevent event) {
        MaeventSteward.openEventLocation(event, parent);
    }

    public NestedScrollView getScrollView() {
        NestedScrollView view = this.view.findViewById(R.id.sv_main_agenda);
        return view;
    }

    private void initIncomingEvents() {
        //TODO Load content
        mIncomingEventsData = new Maevents();
        for (int i = 0; i < 3; i++) {
            Maevent maevent = MaeventBuilder.build();
            mIncomingEventsData.add(maevent);
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
        for (int i = 0; i < 2; i++) {
            Invitation invitation = InvitationBuilder.build();
            mInvitationsData.add(invitation);
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

        mEventDetailsHandler = MainActivity.eventDetailsHandler;

        DetailsDialog.Builder builder = new DetailsDialog.Builder(parent, eventDetailsView);
        mEventDetailsDialog = builder.build(true);

        mEventDetailsAdapter = new EventDetailsViewAdapter(mEventDetailsHandler, eventDetailsView);

        mEventDetailsHandler.focus(parent);
        mEventDetailsHandler.setOnClickJoinHandler(new EventDetailsHandler.OnClickJoinHandler() {
            @Override
            public void onClickJoin() {
                //TODO Refactor
                mEventDetailsAdapter.adaptJoinButton(false);
                mEventDetailsDialog.hide();
                MainActivity.pendingEvent = mEventDetailsHandler.getFocus();
                ((MainActivity)parent).loadLiveEventFragment();
            }
        });

        mEventDetailsLoading = eventDetailsView.findViewById(R.id.pb_event_details_loading);
        mEventDetailsContentView = eventDetailsView.findViewById(R.id.event_details);
    }

    private void acceptInvitation(RecyclerView.ViewHolder viewHolder) {
        InvitationAdapter invitationAdapter = (InvitationAdapter) mInvitationsRecyclerView.getAdapter();
        int pos = viewHolder.getAdapterPosition();

        Invitation invitation = mInvitationsData.get(pos);
        Maevent event = invitation;

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

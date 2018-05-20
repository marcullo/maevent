package com.devmarcul.maevent.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.agenda.IncomingEventAdapter;
import com.devmarcul.maevent.agenda.ItemViewHolder;
import com.devmarcul.maevent.event.Maevent;
import com.devmarcul.maevent.event.Maevents;
import com.devmarcul.maevent.interfaces.ViewScroller;
import com.devmarcul.maevent.utils.tools.Prompt;

public class AgendaFragment extends Fragment implements
        IncomingEventAdapter.IncomingEventAdapterOnClickHandler,
        ViewScroller {

    private View view;
    private Activity parent;
    private Maevents mIncomingEventsData;
    private Handler mIncomingEventsLoader;

    private RecyclerView mRecyclerView;
    private IncomingEventAdapter mIncomingEventAdapter;
    private ItemViewHolder mIncomingEventsLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_agenda, container, false);
        parent = getActivity();

        //TODO Load content
        mIncomingEventsData = new Maevents();
        for (int i = 0; i < 20; i++) {
            mIncomingEventsData.add(new Maevent());
            mIncomingEventsData.get(i).updateContent();
        }

        mRecyclerView = view.findViewById(R.id.rv_incoming_events);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        mIncomingEventAdapter = new IncomingEventAdapter(this);
        mRecyclerView.setAdapter(mIncomingEventAdapter);

        View incomingEventsLabelView = view.findViewById(R.id.main_incoming_events_label);
        mIncomingEventsLabel = new ItemViewHolder(incomingEventsLabelView, mRecyclerView);
        incomingEventsLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomingEventsLabel.toggle();
            }
        });

        mIncomingEventsLoader = new Handler();
        mIncomingEventsLoader.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar pb = view.findViewById(R.id.pb_incoming_events_number_loading_indicator);
                pb.setVisibility(View.VISIBLE);
                mIncomingEventAdapter.setIncomingEventsData(mIncomingEventsData);
                pb.setVisibility(View.GONE);
            }
        }, 1000);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIncomingEventsLoader.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(Maevent event) {
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
}

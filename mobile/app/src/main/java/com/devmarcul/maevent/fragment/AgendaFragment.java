package com.devmarcul.maevent.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.agenda.IncomingEventAdapter;
import com.devmarcul.maevent.event.Maevent;
import com.devmarcul.maevent.event.Maevents;
import com.devmarcul.maevent.utils.tools.Prompt;

import java.util.LinkedList;
import java.util.List;

public class AgendaFragment extends Fragment implements
        IncomingEventAdapter.IncomingEventAdapterOnClickHandler {

    private Activity parent;

    private Maevents mIncomingEventsData;

    private RecyclerView mRecyclerView;
    private IncomingEventAdapter mIncomingEventAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_agenda, container, false);
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

        mIncomingEventAdapter.setIncomingEventsData(mIncomingEventsData);

        return view;
    }

    @Override
    public void onClick(Maevent event) {
        mIncomingEventsData.get(0).confirm(true);
        mIncomingEventAdapter.setIncomingEventsData(mIncomingEventsData);
        Prompt.displayTodo(parent);
    }
}

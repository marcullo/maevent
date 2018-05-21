package com.devmarcul.maevent.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.event.EventDetailsAdapter;
import com.devmarcul.maevent.interfaces.ViewScroller;
import com.devmarcul.maevent.utils.tools.Prompt;

public class LiveEventFragment extends Fragment implements ViewScroller {

    private View view;
    private Activity parent;

    private View mEventDetailsView;
    private EventDetailsAdapter mEventDetailsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_live_event, container, false);
        parent = getActivity();

        initEventDetails();

        return view;
    }

    @Override
    public NestedScrollView getScrollView() {
        NestedScrollView view = this.view.findViewById(R.id.sv_main_live_event);
        return view;
    }

    //TODO combine with @AgendaFragment.initEventDetailsDialog
    private void initEventDetails() {
        mEventDetailsView = view.findViewById(R.id.main_event_details);
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
            }

        };
        mEventDetailsAdapter = new EventDetailsAdapter(onClickHandler, mEventDetailsView);
        mEventDetailsAdapter.adaptContent(MainActivity.pendingEvent);
        mEventDetailsAdapter.bindOnClickListeners();

        EventDetailsAdapter.ViewHolder holder = mEventDetailsAdapter.getViewHolder();
        Button joinButton = holder.getJoinButton();
        joinButton.setVisibility(View.GONE);
    }
}

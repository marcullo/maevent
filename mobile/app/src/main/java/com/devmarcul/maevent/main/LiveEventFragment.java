package com.devmarcul.maevent.main;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.business_logic.MaeventSteward;
import com.devmarcul.maevent.content_provider.hardcoded.UserBuilder;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.main.common.EventDetailsViewAdapter;
import com.devmarcul.maevent.main.common.EventDetailsViewHolder;
import com.devmarcul.maevent.utils.CustomTittleSetter;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.devmarcul.maevent.utils.bottom_navig.ViewScroller;
import com.devmarcul.maevent.main.live_event.AttendeeViewAdapter;
import com.devmarcul.maevent.main.live_event.Attendees;
import com.devmarcul.maevent.utils.Prompt;

import java.util.Calendar;

public class LiveEventFragment extends Fragment implements
        ViewScroller,
        CustomTittleSetter {

    private View view;
    private Activity parent;

    private View mEventDetailsView;
    private EventDetailsViewAdapter mEventDetailsAdapter;

    private Attendees mAttendeesData;
    private RecyclerView mAttendeeRecyclerView;
    private AttendeeViewAdapter mAttendeeViewAdapter;

    private View mAttendeeDetailsView;
    private DetailsDialog mAttendeeDetailsDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_live_event, container, false);
        mAttendeeDetailsView = inflater.inflate(R.layout.main_person_details, container, false);
        parent = getActivity();

        initEventDetails();
        initAttendees();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle();
    }

    @Override
    public void setTitle() {
        ActionBar bar = ((AppCompatActivity) parent).getSupportActionBar();
        if (bar != null) {
            int titleRes = R.string.toolbar_title_live_event;
            bar.setTitle(titleRes);
        }
    }

    @Override
    public NestedScrollView getScrollView() {
        NestedScrollView view = this.view.findViewById(R.id.sv_main_live_event);
        return view;
    }

    //TODO combine with @AgendaFragment.initEventDetailsDialog
    private void initEventDetails() {
        mEventDetailsView = view.findViewById(R.id.main_event_details);
        EventDetailsViewAdapter.OnClickHandler onClickHandler = new EventDetailsViewAdapter.OnClickHandler() {
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

        View progressBarView = mEventDetailsView.findViewById(R.id.pb_event_details_loading);
        progressBarView.setVisibility(View.INVISIBLE);

        View eventdetailsContentView = mEventDetailsView.findViewById(R.id.event_details);
        eventdetailsContentView.setVisibility(View.VISIBLE);

        mEventDetailsAdapter = new EventDetailsViewAdapter(onClickHandler, mEventDetailsView);
        mEventDetailsAdapter.adaptContent(MainActivity.pendingEvent);
        mEventDetailsAdapter.adaptJoinButton(false);
        mEventDetailsAdapter.bindOnClickListeners();
    }

    private void initAttendees() {
        View attendeeDetailsView = mAttendeeDetailsView.findViewById(R.id.main_person_details);
        //TODO Load content
        mAttendeesData = new Attendees();
        for (int i = 0; i < 10; i++) {
            User user = UserBuilder.build();
            mAttendeesData.add(user);
        }

        GridLayoutManager attendeeGridLayoutManager = new GridLayoutManager(parent, 2);
        mAttendeeRecyclerView = view.findViewById(R.id.rv_attendees);
        mAttendeeRecyclerView.setHasFixedSize(false);
        mAttendeeRecyclerView.setLayoutManager(attendeeGridLayoutManager);

        AttendeeViewAdapter.OnClickHandler onClickHandler =  new AttendeeViewAdapter.OnClickHandler() {
            @Override
            public void onClick() {
                Prompt.displayShort("TODO Add user details dialog", parent);
                mAttendeeDetailsDialog.show();
            }
        };
        mAttendeeViewAdapter = new AttendeeViewAdapter(onClickHandler, parent, mAttendeesData);
        mAttendeeRecyclerView.setAdapter(mAttendeeViewAdapter);

        DetailsDialog.Builder builder = new DetailsDialog.Builder(parent, attendeeDetailsView);
        mAttendeeDetailsDialog = builder.build(true);
    }
}

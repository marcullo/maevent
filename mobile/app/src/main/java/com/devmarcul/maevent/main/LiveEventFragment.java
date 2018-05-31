package com.devmarcul.maevent.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.content_providers.hardcoded.UserBuilder;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.main.common.EventDetailsHandler;
import com.devmarcul.maevent.main.common.EventDetailsViewAdapter;
import com.devmarcul.maevent.common.UserDetailsViewAdapter;
import com.devmarcul.maevent.utils.CustomTittleSetter;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.devmarcul.maevent.utils.bottom_navig.ViewScroller;
import com.devmarcul.maevent.main.live_event.AttendeeViewAdapter;
import com.devmarcul.maevent.main.live_event.Attendees;

public class LiveEventFragment extends Fragment implements
        AttendeeViewAdapter.OnClickHandler,
        ViewScroller,
        CustomTittleSetter {

    private View view;
    private Activity parent;

    private View mEventDetailsView;
    private EventDetailsViewAdapter mEventDetailsAdapter;
    private EventDetailsHandler mEventDetailsHandler;

    private Attendees mAttendeesData;
    private RecyclerView mAttendeeRecyclerView;
    private AttendeeViewAdapter mAttendeeViewAdapter;
    private Handler mAttendeesHandler;

    private View mAttendeeDetailsView;
    private View mAttendeeDetailsContentView;
    private DetailsDialog mAttendeeDetailsDialog;
    private UserDetailsViewAdapter mAttendeeDetailsAdapter;
    private ProgressBar mAttendeeDetailsLoading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_live_event, container, false);
        mAttendeeDetailsView = inflater.inflate(R.layout.main_profile_details, container, false);
        parent = getActivity();

        initEventDetails();
        initAttendees();
        initAttendeeDetailsDialog();

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

    @Override
    public void onClick(User attendee) {
        mAttendeeDetailsLoading.setVisibility(View.VISIBLE);
        mAttendeeDetailsContentView.setVisibility(View.INVISIBLE);

        mAttendeeDetailsDialog.show();
        mAttendeeDetailsAdapter.adaptContent(attendee);

        mAttendeeDetailsLoading.setVisibility(View.INVISIBLE);
        mAttendeeDetailsContentView.setVisibility(View.VISIBLE);
    }

    private void initEventDetails() {
        mEventDetailsView = view.findViewById(R.id.main_event_details);

        mEventDetailsHandler = MainActivity.eventDetailsHandler;
        mEventDetailsHandler.focus(parent);
        mEventDetailsHandler.focus(MainActivity.pendingEvent, null);

        View progressBarView = mEventDetailsView.findViewById(R.id.pb_event_details_loading);
        progressBarView.setVisibility(View.INVISIBLE);

        View eventdetailsContentView = mEventDetailsView.findViewById(R.id.event_details);
        eventdetailsContentView.setVisibility(View.VISIBLE);

        mEventDetailsAdapter = new EventDetailsViewAdapter(mEventDetailsHandler, mEventDetailsView);
        mEventDetailsAdapter.adaptContent(MainActivity.pendingEvent);
        mEventDetailsAdapter.adaptJoinButton(false);
        mEventDetailsAdapter.bindListeners();
    }

    private void initAttendees() {
        //TODO Load content
        mAttendeesData = new Attendees();
        for (int i = 0; i < 10; i++) {
            User user = UserBuilder.build();
            mAttendeesData.add(user);
        }

        GridLayoutManager attendeeGridLayoutManager = new GridLayoutManager(parent, 2);
        mAttendeeRecyclerView = view.findViewById(R.id.rv_attendees);
        mAttendeeRecyclerView.setLayoutManager(attendeeGridLayoutManager);
        mAttendeeRecyclerView.setHasFixedSize(false);

        mAttendeeViewAdapter = new AttendeeViewAdapter(this);
        mAttendeeRecyclerView.setAdapter(mAttendeeViewAdapter);

        mAttendeesHandler = new Handler();
        mAttendeesHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar pb = view.findViewById(R.id.pb_attendees_loading);
                pb.setVisibility(View.VISIBLE);
                mAttendeeViewAdapter.setAttendeesData(mAttendeesData);
                pb.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void initAttendeeDetailsDialog() {
        View attendeeDetailsView = mAttendeeDetailsView.findViewById(R.id.main_profile_details);
        DetailsDialog.Builder builder = new DetailsDialog.Builder(parent, attendeeDetailsView);
        mAttendeeDetailsDialog = builder.build(true);

        mAttendeeDetailsContentView = attendeeDetailsView.findViewById(R.id.person_details);
        mAttendeeDetailsLoading = attendeeDetailsView.findViewById(R.id.pb_person_details_loading);

        View editTagsView = mAttendeeDetailsContentView.findViewById(R.id.edit_tags);
        mAttendeeDetailsAdapter = new UserDetailsViewAdapter(
                attendeeDetailsView,
                new TagsViewAdapter(editTagsView, R.id.et_tags));
    }
}

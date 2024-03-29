package com.devmarcul.maevent.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.ClientError;
import com.android.volley.ServerError;
import com.devmarcul.maevent.InviteActivity;
import com.devmarcul.maevent.MainActivity;
import com.devmarcul.maevent.R;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.business_logic.MaeventUserManager;
import com.devmarcul.maevent.business_logic.ThisUser;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserDummy;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.data.Users;
import com.devmarcul.maevent.main.common.EventDetailsHandler;
import com.devmarcul.maevent.main.common.EventDetailsViewAdapter;
import com.devmarcul.maevent.common.UserDetailsViewAdapter;
import com.devmarcul.maevent.utils.CustomTittleSetter;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.devmarcul.maevent.utils.bottom_navig.ViewScroller;
import com.devmarcul.maevent.main.live_event.AttendeeViewAdapter;

import java.util.List;

import me.originqiu.library.EditTag;

public class LiveEventFragment extends Fragment implements
        AttendeeViewAdapter.OnClickHandler,
        ViewScroller,
        CustomTittleSetter {

    private static final String LOG_TAG = "LiveEventFragment";

    private View view;
    private Activity parent;

    private View mEventDetailsView;
    private EventDetailsViewAdapter mEventDetailsAdapter;
    private EventDetailsHandler mEventDetailsHandler;

    private Users mAttendeesData;
    private Users mAttendeesDataBackup;

    private ProgressBar mAttendeesProgressBar;
    private RecyclerView mAttendeeRecyclerView;
    private AttendeeViewAdapter mAttendeeViewAdapter;

    private ProgressBar mAttendeeDetailsProgressBar;
    private View mAttendeeDetailsView;
    private View mAttendeeDetailsContentView;
    private DetailsDialog mAttendeeDetailsDialog;
    private UserDetailsViewAdapter mAttendeeDetailsAdapter;

    private View mLiveEventToolbar;

    private EditTag mFilterTags;

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
        initTagsFilter();
        initAttendeeDetailsDialog();

        updateUsers();

        return view;
    }

    public void refresh() {
        mAttendeesProgressBar.setVisibility(View.VISIBLE);
        mAttendeeRecyclerView.setVisibility(View.GONE);
        updateUsers();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        if (attendee instanceof UserDummy) {
            return;
        }

        mAttendeeDetailsProgressBar.setVisibility(View.VISIBLE);
        mAttendeeDetailsContentView.setVisibility(View.INVISIBLE);

        mAttendeeDetailsDialog.show();
        mAttendeeDetailsAdapter.adaptContent(attendee);

        mAttendeeDetailsProgressBar.setVisibility(View.INVISIBLE);
        mAttendeeDetailsContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickDummy() {
        refresh();
    }

    private void initEventDetails() {
        mLiveEventToolbar = view.findViewById(R.id.main_live_event_toolbar);
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

        Maevent pendingEvent = mEventDetailsHandler.getFocus();
        UserProfile hostProfile = pendingEvent.getHost().getProfile();
        ThisUser.updateContent(parent);
        UserProfile thisUserProfile = ThisUser.getProfile();
        if (hostProfile.id == thisUserProfile.id) {
            mEventDetailsAdapter.adaptAddAttendeeButton(true);
        }
        else {
            mEventDetailsAdapter.adaptAddAttendeeButton(false);
        }

        mEventDetailsHandler.setOnClickHandlers(new EventDetailsHandler.OnClickJoinHandler() {
            @Override
            public void onClickJoin() {
            }
        }, new EventDetailsHandler.OnClickAddAttendeeHandler() {
            @Override
            public void onClickAddAttendee() {
                Maevent event = MainActivity.pendingEvent;
                if (event != null) {
                    startInviteActivity(event);
                }
            }
        });
        mEventDetailsAdapter.bindListeners();
    }

    private void initAttendees() {
        mAttendeesData = new Users();

        GridLayoutManager attendeeGridLayoutManager = new GridLayoutManager(parent, 2);
        mAttendeeRecyclerView = view.findViewById(R.id.rv_attendees);
        mAttendeeRecyclerView.setLayoutManager(attendeeGridLayoutManager);
        mAttendeeRecyclerView.setHasFixedSize(false);

        mAttendeeViewAdapter = new AttendeeViewAdapter(this);
        mAttendeeRecyclerView.setAdapter(mAttendeeViewAdapter);

        mAttendeesProgressBar = view.findViewById(R.id.pb_attendees_loading);
    }

    private void initTagsFilter() {
        mFilterTags = mLiveEventToolbar.findViewById(R.id.main_live_event_filter);
        mFilterTags.setEditable(true);
        mFilterTags.setTagAddCallBack(new EditTag.TagAddCallback() {
            @Override
            public boolean onTagAdd(String s) {
                updateFilters(s, true);
                return true;
            }
        });
        mFilterTags.setTagDeletedCallback(new EditTag.TagDeletedCallback() {
            @Override
            public void onTagDelete(String s) {
                updateFilters(s, false);
            }
        });
    }

    private void updateFilters(String antotherTag, boolean added) {
        if (mAttendeesDataBackup == null) {
            mAttendeesDataBackup = new Users();
        }
        mAttendeesDataBackup.clear();

        List<String> tags = mFilterTags.getTagList();
        if (added) {
            tags.add(antotherTag);
        }

        for (User user :
                mAttendeesData) {
            int tagsCnt = 0;

            if (user.getProfile() == null) {
                continue;
            }
            if (user.getProfile().tags == null) {
                continue;
            }

            for (String tagAttendee:
                    user.getProfile().tags) {
                for (String tag:
                        tags) {
                    if (tagAttendee.toLowerCase().contains(tag.toLowerCase())) {
                        tagsCnt++;
                        break;
                    }
                }
            }
            if (tagsCnt >= tags.size()) {
                mAttendeesDataBackup.add(user);
            }
        }
        mAttendeeViewAdapter.setAttendeesData(mAttendeesDataBackup);
    }

    private void initAttendeeDetailsDialog() {
        View attendeeDetailsView = mAttendeeDetailsView.findViewById(R.id.main_profile_details);
        DetailsDialog.Builder builder = new DetailsDialog.Builder(parent, attendeeDetailsView);
        mAttendeeDetailsDialog = builder.build(true);

        mAttendeeDetailsContentView = attendeeDetailsView.findViewById(R.id.person_details);
        mAttendeeDetailsProgressBar = attendeeDetailsView.findViewById(R.id.pb_person_details_loading);

        View editTagsView = mAttendeeDetailsContentView.findViewById(R.id.edit_tags);
        mAttendeeDetailsAdapter = new UserDetailsViewAdapter(
                attendeeDetailsView,
                new TagsViewAdapter(editTagsView, R.id.et_tags));
    }

    private void updateUsers() {
        final Context context = getContext();

        MaeventUserManager.getInstance().getAllAttendees(parent, MainActivity.pendingEvent, new NetworkReceiver.Callback<Users>() {
            @Override
            public void onSuccess(Users users) {
                mAttendeesData.clear();
                mAttendeesData = users;
                mAttendeeViewAdapter.setAttendeesData(mAttendeesData);

                mLiveEventToolbar.setVisibility(View.VISIBLE);
                mAttendeesProgressBar.setVisibility(View.GONE);
                mAttendeeRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception exception) {
                if (exception instanceof ClientError) {
                    Prompt.displayShort("Your profile is invalid - probably name exists! Contact with support.", context);
                }
                else if (exception instanceof ServerError) {
                    Prompt.displayShort("No connection with server.", context);
                }
                else {
                    Prompt.displayShort("Internal error.", context);
                }
                mAttendeesProgressBar.setVisibility(View.GONE);
                mAttendeeRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void startInviteActivity(Maevent event) {
        if (event == null) {
            return;
        }
        if (!event.isValid()) {
            return;
        }

        MaeventModel focusedModel = new MaeventModel(event);

        Log.d(LOG_TAG, "Setting next activity.");
        Intent intent = new Intent(parent, InviteActivity.class);
        intent.putExtra(InviteActivity.KEY_PARCEL_FOCUSED_EVENT_MODEL, focusedModel);
        startActivity(intent);
    }
}

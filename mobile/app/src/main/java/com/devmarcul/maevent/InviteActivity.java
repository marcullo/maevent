package com.devmarcul.maevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.business_logic.MaeventUserManager;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.content_providers.hardcoded.UserBuilder;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.Users;
import com.devmarcul.maevent.invite.InviteesAdapter;
import com.devmarcul.maevent.invite.SearchingUsersAdapter;
import com.devmarcul.maevent.invite.SimplifiedEventDetailsAdapter;
import com.devmarcul.maevent.utils.CustomTittleSetter;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.SwipeDeleteCallback;
import com.devmarcul.maevent.utils.Tools;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import java.util.function.Predicate;

public class InviteActivity extends AppCompatActivity implements CustomTittleSetter {

    public static String KEY_PARCEL_FOCUSED_EVENT_MODEL = "focused-event-model";

    Maevent mFocusedEvent;

    private Users mInviteesData;
    private View mEventDetailsView;
    private SimplifiedEventDetailsAdapter mEventDetailsAdapter;

    private RecyclerView mInviteeRecyclerView;
    private InviteesAdapter mInviteesAdapter;
    private ItemTouchHelper mInviteesTouchHelper;

    private Users mFoundUsersData;
    MaterialSearchView mSearchView;
    private ProgressBar mFoundUsersLoadingBar;
    private TextView mSearchResultsLabel;
    private TextView mSearchResultsDescription;
    private DetailsDialog mSearchResultsDialog;
    private RecyclerView mFoundUsersRecyclerView;
    private SearchingUsersAdapter mFoundUsersAdapter;
    private View mSearchResultsView;

    private Button mInviteButton;
    private View mInviteLoadingBar;

    private boolean mRequestFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        mSearchResultsView = LayoutInflater.from(this).inflate(R.layout.invite_search_results, null);

        loadData();

        initInvitees();
        initInvite();
        initEventDetails();
        initSearch();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invite, menu);
        MenuItem item = menu.findItem(R.id.invite_action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem addUserItem = menu.findItem(R.id.invite_action_search);
        MenuItem clearAllItem = menu.findItem(R.id.invite_action_clear);
        addUserItem.setEnabled(mRequestFinished);
        clearAllItem.setEnabled(mRequestFinished);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.invite_action_clear) {
            clearInvitees();
            updateInviteesCounter();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRequestFinished = true;
    }

    @Override
    public void setTitle() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            int titleRes = R.string.toolbar_title_invite_users;
            bar.setTitle(titleRes);
            updateInviteesCounter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle();
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        else {
            super.onBackPressed();
        }
    }

    private void updateInviteesCounter() {
        int inviteesNr = mInviteesAdapter.getInviteesNumber();
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            String newInviteesText = getString(R.string.invite_new_invitees_text);
            StringBuilder builder = new StringBuilder();
            builder.append(newInviteesText).append(" ").append(inviteesNr);
            bar.setSubtitle(builder.toString());
        }

        adaptInviteButton(inviteesNr > 0);
    }

    private void initSearch() {
        final Context context = this;

        mFoundUsersData = new Users();
        mSearchView = findViewById(R.id.sv_user_search);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null) {
                    return false;
                }
                int len = query.length();
                if (len < 2 || len > 35) {
                    Prompt.displayShort("Query should have from 5 to 35 chars", context);
                    return true;
                }
                searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.showSearch();

        mSearchResultsLabel = mSearchResultsView.findViewById(R.id.tv_search_label);
        mSearchResultsDescription = mSearchResultsView.findViewById(R.id.tv_found_description);

        View searchResultsView = mSearchResultsView.findViewById(R.id.invite_search_results);
        DetailsDialog.Builder builder = new DetailsDialog.Builder(this, searchResultsView);
        mSearchResultsDialog = builder.build(true);

        GridLayoutManager foundUsersGridLayoutManager = new GridLayoutManager(searchResultsView.getContext(), 1);
        View searchResultsContentView = mSearchResultsView.findViewById(R.id.search_results_content);
        mFoundUsersRecyclerView = searchResultsContentView.findViewById(R.id.rv_found_users);
        mFoundUsersRecyclerView.setLayoutManager(foundUsersGridLayoutManager);

        mFoundUsersAdapter = new SearchingUsersAdapter(new SearchingUsersAdapter.OnClickHandler() {
            @Override
            public void onClick(User user) {
                if (user != null) {
                    updateSearchResult(user);
                    mFoundUsersData.clear();
                    mFoundUsersAdapter.setFoundUsersData(mFoundUsersData);
                }
            }
        });
        mFoundUsersRecyclerView.setAdapter(mFoundUsersAdapter);

        mFoundUsersLoadingBar = mSearchResultsView.findViewById(R.id.invite_found_users_loading);
    }

    private void loadData() {
        Intent starter = getIntent();
        if (starter == null) {
            return;
        }
        if (!starter.hasExtra(KEY_PARCEL_FOCUSED_EVENT_MODEL)) {
            return;
        }

        MaeventModel model = starter.getParcelableExtra(KEY_PARCEL_FOCUSED_EVENT_MODEL);
        mFocusedEvent = model.toMaevent();
    }

    private void initInvitees() {
        mInviteesData = new Users();

        GridLayoutManager attendeeGridLayoutManager = new GridLayoutManager(this, 1);
        mInviteeRecyclerView = findViewById(R.id.rv_added_users);
        mInviteeRecyclerView.setLayoutManager(attendeeGridLayoutManager);

        final Context context = this;
        SwipeDeleteCallback swipeHandler = new SwipeDeleteCallback(context, new SwipeDeleteCallback.SwipedCallback() {
            @Override
            public void onDelete(RecyclerView.ViewHolder viewHolder) {
                InviteesAdapter adapter = (InviteesAdapter) mInviteeRecyclerView.getAdapter();
                int pos = viewHolder.getAdapterPosition();
                adapter.removeInvitation(pos);
                updateInviteesCounter();
            }
        });
        mInviteesTouchHelper = new ItemTouchHelper(swipeHandler);
        adaptInviteesTouchHelper(mInviteeRecyclerView);

        mInviteesAdapter = new InviteesAdapter();
        mInviteeRecyclerView.setAdapter(mInviteesAdapter);

        mInviteesAdapter.setInviteesData(mInviteesData);
    }

    private void initInvite() {
        mInviteButton = findViewById(R.id.btn_invite);
        mInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteUsers();
            }
        });
        mInviteLoadingBar = findViewById(R.id.invite_loading_view);
        adaptInviteLoadingBar(false);
    }

    private void initEventDetails() {
        mEventDetailsView = findViewById(R.id.invite_event_details);

        View progressBarView = mEventDetailsView.findViewById(R.id.pb_event_details_loading);
        progressBarView.setVisibility(View.INVISIBLE);

        View eventDetailsContentView = mEventDetailsView.findViewById(R.id.event_details);
        eventDetailsContentView.setVisibility(View.VISIBLE);

        mEventDetailsAdapter = new SimplifiedEventDetailsAdapter(mEventDetailsView);

        mEventDetailsAdapter.adaptContent(mFocusedEvent);
    }

    private void initSearchLabel() {
        mSearchResultsLabel.setText(R.string.invite_search_label);
    }

    private void initSearchUserDescription() {
        mSearchResultsDescription.setText("");
    }

    private void adaptInviteButton(boolean active) {
        mInviteButton.setClickable(active);
        Drawable background = mInviteButton.getBackground();
        if (active) {
            background.setColorFilter(null);
        }
        else {
            background.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void adaptFoundUsersLoadingBar(boolean visible) {
        mFoundUsersLoadingBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private void adaptInviteLoadingBar(boolean visible) {
        mInviteLoadingBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void adaptInviteesList(boolean editable) {
        RecyclerView rv = editable ? mInviteeRecyclerView : null;
        adaptInviteesTouchHelper(rv);
    }

    private void adaptInviteesTouchHelper(RecyclerView rv) {
        mInviteesTouchHelper.attachToRecyclerView(rv);
    }

    private void adaptSearchResultsDescription(boolean found, String description) {
        int labelRes = found ? R.string.invite_search_label_found : R.string.invite_search_label_not_found;
        mSearchResultsLabel.setText(labelRes);
        if (description != null) {
            mSearchResultsDescription.setText(description);
        }
    }

    private void clearInvitees() {
        mInviteesAdapter.clearInvitations();
    }

    private void searchUser(String name) {
        final Context context = this;

        mFoundUsersData.clear();
        adaptFoundUsersLoadingBar(true);
        initSearchLabel();
        initSearchUserDescription();
        mSearchResultsDialog.show();

        MaeventUserManager.getInstance().getUsersByQuery(this, name, new NetworkReceiver.Callback<Users>() {
            @Override
            public void onSuccess(Users users) {
                updateUsersFound(users);
            }

            @Override
            public void onError(Exception exception) {
                adaptFoundUsersLoadingBar(false);
                Prompt.displayShort("Failed!", context);
            }
        });
    }

    public void updateUsersFound(Users users) {
        adaptFoundUsersLoadingBar(false);

        int foundUsersNr = users.size();
        if (foundUsersNr < 1) {
            adaptSearchResultsDescription(false, "User not found, sorry.");
            return;
        }

        List<Integer> ids = mFocusedEvent.getAttendeesIdList();

        boolean exists;
        for (User foundUser :
                users) {
            exists = false;
            for (User invitee:
                    mInviteesData) {
                if (invitee.getProfile().id == foundUser.getProfile().id) {
                    exists = true;
                    break;
                }
            }
            for (Integer id:
                    ids) {
                if (id == foundUser.getProfile().id) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                mFoundUsersData.add(foundUser);
            }
        }

        foundUsersNr = mFoundUsersData.size();
        if (foundUsersNr < 1) {
            adaptSearchResultsDescription(false, "I've got nothing for you, sorry!");

            return;
        }

        adaptFoundUsersLoadingBar(false);
        adaptSearchResultsDescription(true, null);
        mFoundUsersAdapter.setFoundUsersData(mFoundUsersData);
    }

    public void updateSearchResult(User user) {
        if (user == null) {
            return;
        }
        addUserToInvitationList(user);
        mSearchResultsDialog.hide();
        mFoundUsersAdapter.setFoundUsersData(new Users());
        mSearchView.showSearch();
        updateInviteesCounter();
        Tools.showSoftKeyboard(this, mSearchView);
    }

    private void addUserToInvitationList(User user) {
        mInviteesData.add(user);
        int lastIndex = mInviteesData.size() - 1;
        mInviteesAdapter.updateInviteesData(lastIndex, user);
    }

    private void inviteUsers() {
        Prompt.displayTodo(this);
        mRequestFinished = false;
        adaptInviteButton(false);
        adaptInviteesList(false);
        adaptFoundUsersLoadingBar(true);
        adaptInviteLoadingBar(true);
        invalidateOptionsMenu();

//        mRequestFinished = true;
    }
}

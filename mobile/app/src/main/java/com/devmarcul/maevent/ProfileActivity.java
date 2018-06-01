package com.devmarcul.maevent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.ClientError;
import com.android.volley.ServerError;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.business_logic.MaeventUserManager;
import com.devmarcul.maevent.business_logic.ThisUser;
import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.common.UserDetailsViewAdapter;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.utils.Prompt;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static String LOG_TAG = "ProfileActivity";

    private UserProfile mUserProfile;

    private FloatingActionButton mEditProfileButton;

    private View mPersonDetailsView;
    private View mPersonDetailsContentView;
    private UserDetailsViewAdapter mPersonDetailsAdapter;
    private ProgressBar mPersonDetailsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mPersonDetailsView = findViewById(R.id.main_profile_person_details);

        mPersonDetailsContentView = mPersonDetailsView.findViewById(R.id.person_details);
        mPersonDetailsLoading = mPersonDetailsView.findViewById(R.id.pb_person_details_loading);

        View editTagsView = mPersonDetailsView.findViewById(R.id.edit_tags);
        mPersonDetailsAdapter = new UserDetailsViewAdapter(
                mPersonDetailsView,
                new TagsViewAdapter(editTagsView, R.id.et_tags));

        mEditProfileButton = findViewById(R.id.btn_main_profile_edit);

        mUserProfile = null;
        updateContent();
    }

    @Override
    public void onClick(View v) {
        setConfigureProfileActivity();
    }

    private void updateContent() {
        ThisUser.updateContent(this);
        if (ThisUser.getProfile().id == 0) {
            Prompt.displayShort("Invalid user", getParent());
            return;
        }

        initUi();

        final Context context = this;

        ThisUser.updateContent(this);
        String identifier = String.valueOf(ThisUser.getProfile().id);
        MaeventUserManager.getInstance().getUser(this, identifier, new NetworkReceiver.Callback<String>() {
            @Override
            public void onSuccess(String data) {
                UserModel model;
                try {
                    Gson gson = new Gson();
                    model = gson.fromJson(data, UserModel.class);
                }
                catch (Exception ex) {
                    model = null;
                }

                if (model == null) {
                    Prompt.displayShort("Error during parsing received data.", context);
                    updateUi();
                    return;
                }

                mUserProfile = model.toUserProfile();
                updateUi();
                ThisUser.setProfile(mUserProfile);
                ThisUser.saveContent(context);
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
                updateUi();
            }
        });
    }

    private void initUi() {
        mPersonDetailsLoading.setVisibility(View.VISIBLE);
        mPersonDetailsContentView.setVisibility(View.GONE);
        mEditProfileButton.setVisibility(View.GONE);
    }

    private void updateUi() {
        if (mUserProfile != null) {
            mPersonDetailsAdapter.adaptContent(mUserProfile);
            mPersonDetailsContentView.setVisibility(View.VISIBLE);
            mPersonDetailsLoading.setVisibility(View.GONE);
            mEditProfileButton.setVisibility(View.VISIBLE);
            mEditProfileButton.setOnClickListener(this);
        }
        else {
            mPersonDetailsContentView.setVisibility(View.GONE);
            mPersonDetailsLoading.setVisibility(View.GONE);
            mEditProfileButton.setVisibility(View.GONE);
            mEditProfileButton.setOnClickListener(null);
        }
    }

    private void setConfigureProfileActivity() {
        User user = new User();
        ThisUser.updateContent(this);
        user.setProfile(ThisUser.getProfile());
        UserModel model = new UserModel(user);

        Log.d(LOG_TAG, "Setting configure profile activity from model bound to user:");
        Log.d(LOG_TAG, user.getContentForDebug());
        Intent intent = new Intent(this, ConfigureProfileActivity.class);
        intent.putExtra(MainActivity.KEY_CONFIG_PROFILE_REQUESTED, true);
        intent.putExtra(MainActivity.KEY_CONFIG_PROFILE_CONTENT,  model);
        startActivity(intent);
    }
}

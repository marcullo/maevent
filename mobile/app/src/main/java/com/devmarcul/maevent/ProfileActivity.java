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
import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.common.UserDetailsViewAdapter;
import com.devmarcul.maevent.data.ThisUser;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.receivers.NetworkReceiver;
import com.devmarcul.maevent.utils.Prompt;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static String KEY_CONFIG_PROFILE_REQUESTED = "config-profile-requested";
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
        initUi();

        final Context context = this;

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
            }

            @Override
            public void onError(Exception exception) {
                if (exception instanceof ClientError) {
                    Prompt.displayShort("Event name exists. Choose another one.", context);
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
        Log.d(LOG_TAG, "Setting configure profile activity.");
        Intent intent = new Intent(this, ConfigureProfileActivity.class);
        intent.putExtra(KEY_CONFIG_PROFILE_REQUESTED, true);
        startActivity(intent);
    }
}

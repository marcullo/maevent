package com.devmarcul.maevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.common.UserDetailsViewAdapter;
import com.devmarcul.maevent.data.ThisUser;

public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static String KEY_CONFIG_PROFILE_REQUESTED = "config-profile-requested";
    private static String LOG_TAG = "ProfileActivity";

    private FloatingActionButton mEditProfileButton;

    private View mPersonDetailsView;
    private View mPersonDetailsContentView;
    private UserDetailsViewAdapter mPersonDetailsAdapter;
    private ProgressBar mPersonDetailsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initEditProfileButton();
        initPersonDetailsView();
        updatePersonDetails();
    }

    @Override
    public void onClick(View v) {
        setConfigureProfileActivity();
    }

    private void initEditProfileButton() {
        mEditProfileButton = findViewById(R.id.btn_main_profile_edit);
        mEditProfileButton.setOnClickListener(this);
    }

    private void initPersonDetailsView() {
        mPersonDetailsView = findViewById(R.id.main_profile_person_details);

        mPersonDetailsContentView = mPersonDetailsView.findViewById(R.id.person_details);
        mPersonDetailsLoading = mPersonDetailsView.findViewById(R.id.pb_person_details_loading);

        View editTagsView = mPersonDetailsView.findViewById(R.id.edit_tags);
        mPersonDetailsAdapter = new UserDetailsViewAdapter(
                mPersonDetailsView,
                new TagsViewAdapter(editTagsView, R.id.et_tags));
    }

    private void updatePersonDetails() {
        mPersonDetailsLoading.setVisibility(View.VISIBLE);
        mPersonDetailsContentView.setVisibility(View.INVISIBLE);

        mPersonDetailsAdapter.adaptContent(ThisUser.getProfile());

        mPersonDetailsLoading.setVisibility(View.INVISIBLE);
        mPersonDetailsContentView.setVisibility(View.VISIBLE);
    }

    private void setConfigureProfileActivity() {
        Log.d(LOG_TAG, "Setting configure profile activity.");
        Intent intent = new Intent(this, ConfigureProfileActivity.class);
        intent.putExtra(KEY_CONFIG_PROFILE_REQUESTED, true);
        startActivity(intent);
    }
}

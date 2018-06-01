package com.devmarcul.maevent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.ClientError;
import com.android.volley.ServerError;
import com.devmarcul.maevent.business_logic.ThisUser;
import com.devmarcul.maevent.common.TagsViewHolder;
import com.devmarcul.maevent.configure_profile.ContactViewAdapter;
import com.devmarcul.maevent.configure_profile.ContactViewHolder;
import com.devmarcul.maevent.configure_profile.IntroductionViewAdapter;
import com.devmarcul.maevent.configure_profile.IntroductionViewHolder;
import com.devmarcul.maevent.configure_profile.TagsItemAdapter;
import com.devmarcul.maevent.data.Tags;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.utils.dialog.TwoButtonsDialog;
import com.devmarcul.maevent.business_logic.MaeventAccountManager;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.dialog.TwoButtonsDialogListener;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import java.util.List;

public class ConfigureProfileActivity extends AppCompatActivity implements Validator.ValidationListener {

    private static String LOG_TAG = "ConfigureProfileActivity";

    private View mContentView;

    private View introductionLabelView;
    private View contactLabelView;
    private View tagsLabelView;

    private IntroductionViewAdapter mIntroductionViewAdapter;
    private ContactViewAdapter mContactViewAdapter;
    private TagsItemAdapter mTagsItemAdapter;
    private ProgressBar mLoadingView;

    private TwoButtonsDialog mCancelDialog;
    private boolean mConfigProfileRequested;

    private Validator mValidator;

    // For validation only ----------------------------------------------------
    @NotEmpty
    @Pattern(caseSensitive = true, message = "Should be a name",
            regex = "^([a-zA-Z]{2,}\\s?([a-zA-Z]{0,})?)")
    EditText mFirstNameEditText;
    @NotEmpty
    @Pattern(caseSensitive = false, message = "Should be a name",
            regex = "^([a-zA-Z]{2,}\\s?([a-zA-Z]{0,})?)")
    EditText mLastNameEditText;
    @NotEmpty
    @Length(min = 4, max = 35)
    EditText mPoseEditText;
    @NotEmpty
    @Length(min = 4, max = 80)
    EditText mHeadlineEditText;
    @Select
    Spinner mTitleSpinner;

    @NotEmpty
    @Length(min = 9, max = 14)
    EditText mPhoneEditText;
    @NotEmpty
    @Email
    EditText mEmailEditText;
    //~For validation only ----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUi();

        // For validation only ----------------------------------------------------
        mFirstNameEditText = mIntroductionViewAdapter.getViewHolder().mFirstNameEditText;
        mLastNameEditText = mIntroductionViewAdapter.getViewHolder().mLastNameEditText;
        mPoseEditText = mIntroductionViewAdapter.getViewHolder().mPoseEditText;
        mHeadlineEditText = mIntroductionViewAdapter.getViewHolder().mHeadlineEditText;
        mTitleSpinner = mIntroductionViewAdapter.getViewHolder().mTitleSpinner;

        mPhoneEditText = mContactViewAdapter.getViewHolder().mPhoneEditText;
        mEmailEditText = mContactViewAdapter.getViewHolder().mEmailEditText;
        //~For validation only ----------------------------------------------------

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mConfigProfileRequested = isConfigureProfileRequested();
        mContentView = findViewById(R.id.activity_configure_profile_container);

        Log.d(LOG_TAG, "Created.");
    }

    @Override
    protected void onResume() {
        super.onResume();

        GoogleSignInAccount account = MaeventAccountManager.getLastSignedAccount(this);
        ThisUser.updateContent(account);

        if (ThisUser.hasCompleteProfile() && !mConfigProfileRequested) {
            setMainActivity();
            return;
        }
        else if (ThisUser.hasCompleteProfile()) {
            mLoadingView.setVisibility(View.GONE);
            adaptContent(null);
            enableContent();
            initCancelDialog();
            return;
        }

        adaptContent(null);
        enableContent();
        invalidateOptionsMenu();

        mContentView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTextIcons));
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIntroductionViewAdapter.unbindListeners();
        mContactViewAdapter.unbindListeners();
        mTagsItemAdapter.unbindListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_configure_profile, menu);
        MenuItem cancelItem = menu.findItem(R.id.configure_profile_action_cancel);
        cancelItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!mConfigProfileRequested) {
            MenuItem cancelItem = menu.findItem(R.id.configure_profile_action_cancel);
            cancelItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.configure_profile_action_save) {
            mValidator.validate();
            return true;
        }
        //TODO Move logout to the proper place
        if (id == R.id.configure_profile_action_logout) {
            logout();
            return true;
        }
        if (id == R.id.configure_profile_action_cancel) {
            displayCancelDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------//

    private void initUi() {
        setContentView(R.layout.activity_configure_profile);

        introductionLabelView = findViewById(R.id.configure_profile_introduction_label);
        final View introductionView = findViewById(R.id.configure_profile_introduction);

        contactLabelView = findViewById(R.id.configure_profile_contact_label);
        final View contactView = findViewById(R.id.configure_profile_contact);

        tagsLabelView = findViewById(R.id.configure_profile_tags_label);
        final View tagsView = findViewById(R.id.configure_profile_tags);

        mIntroductionViewAdapter = new IntroductionViewAdapter(this, introductionLabelView, introductionView);
        mContactViewAdapter = new ContactViewAdapter(this, contactLabelView, contactView);
        mTagsItemAdapter = new TagsItemAdapter(this, tagsLabelView, tagsView);
        mLoadingView = findViewById(R.id.configure_profile_loading);
    }

    private void initCancelDialog() {
        mCancelDialog = new TwoButtonsDialog.Builder(this)
                .setTitle(getString(R.string.configure_profile_cancel_dialog_title))
                .setSubtitle(getString(R.string.configure_profile_cancel_dialog_subtitle))
                .setConfirmButtonColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setCancelButtonColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setConfirmButton(getString(R.string.text_yes), new TwoButtonsDialogListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setMainActivity();
                    }
                })
                .setCancelButton(getString(R.string.text_no), new TwoButtonsDialogListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .build();
    }

    private void enableContent() {
        introductionLabelView.setVisibility(View.VISIBLE);
        contactLabelView.setVisibility(View.VISIBLE);
        tagsLabelView.setVisibility(View.VISIBLE);

        mIntroductionViewAdapter.expandContent();
        mContactViewAdapter.expandContent();
        mTagsItemAdapter.expandContent();

        mIntroductionViewAdapter.bindListeners();
        mContactViewAdapter.bindListeners();
        mTagsItemAdapter.bindListeners();
    }

    private void adaptContent(UserProfile profile) {
        if (profile == null) {
            return;
        }
        mIntroductionViewAdapter.adaptContent(profile);
        mContactViewAdapter.adaptContent(profile);
        mTagsItemAdapter.adaptContent(profile.tags);
    }

    private boolean isConfigureProfileRequested() {
        Intent starter = getIntent();
        if (starter != null) {
            final String KEY_CONFIG_PROFILE_REQUESTED = MainActivity.KEY_CONFIG_PROFILE_REQUESTED;
            if (starter.hasExtra(KEY_CONFIG_PROFILE_REQUESTED)) {
                return starter.getBooleanExtra(KEY_CONFIG_PROFILE_REQUESTED, false);
            }
        }
        return false;
    }

    private void saveConfiguration() {
        //TODO Add saving to storage while launching another activity
        Prompt.displayShort("TODO Add saving to storage", this);
    }

    private void setWelcomeActivity() {
        Log.d(LOG_TAG, "Setting welcome activity.");
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void setMainActivity() {
        Log.d(LOG_TAG, "Setting main activity.");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout() {
        MaeventAccountManager.signOut(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setWelcomeActivity();
            }
        });
    }

    private void displayCancelDialog() {
        mCancelDialog.show();
    }

    @Override
    public void onValidationSucceeded() {
        saveConfiguration();
        setMainActivity();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText)view).setError(message);
            }
            else {
                Prompt.displayLong(message, this);
            }
        }
    }
}

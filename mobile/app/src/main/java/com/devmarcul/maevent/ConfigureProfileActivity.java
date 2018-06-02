package com.devmarcul.maevent;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.ClientError;
import com.android.volley.ServerError;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.business_logic.MaeventUserManager;
import com.devmarcul.maevent.business_logic.ThisUser;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.common.TagsViewHolder;
import com.devmarcul.maevent.common.UserDetailsViewAdapter;
import com.devmarcul.maevent.configure_profile.ContactViewAdapter;
import com.devmarcul.maevent.configure_profile.ContactViewHolder;
import com.devmarcul.maevent.configure_profile.IntroductionViewAdapter;
import com.devmarcul.maevent.configure_profile.IntroductionViewHolder;
import com.devmarcul.maevent.configure_profile.TagsItemAdapter;
import com.devmarcul.maevent.data.Tags;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.utils.Tools;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
import com.devmarcul.maevent.utils.dialog.TwoButtonsDialog;
import com.devmarcul.maevent.business_logic.MaeventAccountManager;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.dialog.TwoButtonsDialogListener;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import java.util.List;

public class ConfigureProfileActivity extends AppCompatActivity implements
        Validator.ValidationListener,
        View.OnClickListener {

    private static String LOG_TAG = "ConfigureProfileActivity";

    private View mContentView;

    private View introductionLabelView;
    private View contactLabelView;
    private View tagsLabelView;

    private IntroductionViewAdapter mIntroductionViewAdapter;
    private ContactViewAdapter mContactViewAdapter;
    private TagsItemAdapter mTagsItemAdapter;
    private ProgressBar mLoadingLocationView;
    private ProgressBar mLoadingView;

    private TwoButtonsDialog mCancelDialog;
    private DetailsDialog mPreviewDialog;

    private View mUserDetailsView;
    private View mUserDetailsContentView;
    private UserDetailsViewAdapter mUserDetailsAdapter;
    private ProgressBar mUserDetailsLoading;

    private boolean mConfigProfileRequested;
    private boolean mRequestFinished;
    private int mRetriesNr;
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
    @Select(message = "Pick a rank (upper left corner)")
    Spinner mRankSpinner;

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

        mRetriesNr = 0;

        initUi();

        // For validation only ----------------------------------------------------
        mFirstNameEditText = mIntroductionViewAdapter.getViewHolder().mFirstNameEditText;
        mLastNameEditText = mIntroductionViewAdapter.getViewHolder().mLastNameEditText;
        mPoseEditText = mIntroductionViewAdapter.getViewHolder().mPoseEditText;
        mHeadlineEditText = mIntroductionViewAdapter.getViewHolder().mHeadlineEditText;
        mRankSpinner = mIntroductionViewAdapter.getViewHolder().mTitleSpinner;

        mPhoneEditText = mContactViewAdapter.getViewHolder().mPhoneEditText;
        mEmailEditText = mContactViewAdapter.getViewHolder().mEmailEditText;
        //~For validation only ----------------------------------------------------

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mConfigProfileRequested = checkConfigurationRequested();
        mContentView = findViewById(R.id.activity_configure_profile_container);

        Log.d(LOG_TAG, "Created.");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRequestFinished = true;

        if (!mConfigProfileRequested) {
            GoogleSignInAccount account = MaeventAccountManager.getLastSignedAccount(this);
            ThisUser.initializeContent(this, account);
        }

        if (ThisUser.hasCompleteProfile() && mConfigProfileRequested) {
            Log.i(LOG_TAG, "Let's go to configuring profile");
            mLoadingLocationView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.GONE);
            initCancelDialog();
            updateUi(ThisUser.getProfile());
            return;
        }
        else if (ThisUser.isRegistered()) {
            Prompt.displayShort("Fetching data", this);
            Log.i(LOG_TAG, "Profile exists but is incomplete. Let's fetch data");
            getUserProfile();
            return;
        }
        updateUi(null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mPreviewDialog != null) {
            mPreviewDialog.cancel();
        }

        mIntroductionViewAdapter.unbindListeners();
        mContactViewAdapter.unbindListeners();
        mTagsItemAdapter.unbindListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_configure_profile, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem saveItem = menu.findItem(R.id.configure_profile_action_save);
        MenuItem supportItem = menu.findItem(R.id.configure_profile_action_support);
        MenuItem previewItem = menu.findItem(R.id.configure_profile_action_preview);
        MenuItem cancelItem = menu.findItem(R.id.configure_profile_action_cancel);

        saveItem.setVisible(mRequestFinished);
        supportItem.setVisible(mRequestFinished);
        previewItem.setVisible(mRequestFinished);
        cancelItem.setVisible(mRequestFinished && mConfigProfileRequested);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.configure_profile_action_preview) {
            UserProfile profile = extractProfileFromViews();
            mPreviewDialog.show();
            mUserDetailsAdapter.adaptContent(profile);
            return false;
        }
        if (id == R.id.configure_profile_action_save) {
            mValidator.validate();
            return true;
        }
        if (id == R.id.configure_profile_action_logout) {
            logout();
            return true;
        }
        if (id == R.id.configure_profile_action_cancel) {
            displayCancelDialog();
            return true;
        }
        if (id == R.id.configure_profile_action_support) {
            Prompt.displayTodo(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {
        Tools.hideSoftKeyboard(this, getCurrentFocus());

        if (!mRequestFinished) {
            Prompt.displayShort("Wait for response from a server first", this);
            return;
        }

        hideContent();
        invalidateOptionsMenu();
        unbindListeners();
        mLoadingView.setVisibility(View.VISIBLE);
        mRequestFinished = false;

        UserProfile profile = extractProfileFromViews();
        saveConfiguration(profile);
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

    @Override
    public void onClick(View v) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), MainActivity.PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        mLoadingLocationView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Place place = null;
        if (requestCode == MainActivity.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
            }
        }

        mLoadingLocationView.setVisibility(View.GONE);
        mContactViewAdapter.updatePlaceViews(place);
    }

    @Override
    public void onBackPressed() {
        if (mCancelDialog != null) {
            mCancelDialog.show();
        }
    }

    //--------------------------------------------------------------------------------------------//

    private void initUi() {
        setContentView(R.layout.activity_configure_profile);
        LayoutInflater inflater = getLayoutInflater();
        mUserDetailsView = inflater.inflate(R.layout.main_profile_details, null);

        introductionLabelView = findViewById(R.id.configure_profile_introduction_label);
        final View introductionView = findViewById(R.id.configure_profile_introduction);

        contactLabelView = findViewById(R.id.configure_profile_contact_label);
        final View contactView = findViewById(R.id.configure_profile_contact);

        tagsLabelView = findViewById(R.id.configure_profile_tags_label);
        final View tagsView = findViewById(R.id.configure_profile_tags);

        mIntroductionViewAdapter = new IntroductionViewAdapter(this, introductionLabelView, introductionView);
        mContactViewAdapter = new ContactViewAdapter(this, contactLabelView, contactView, this);
        mTagsItemAdapter = new TagsItemAdapter(this, tagsLabelView, tagsView);
        mLoadingLocationView = findViewById(R.id.configure_profile_loading_contact);
        mLoadingView = findViewById(R.id.configure_profile_loading);

        mLoadingLocationView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
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

    private void initPreviewDialog() {
        View userDetailsView = mUserDetailsView.findViewById(R.id.main_profile_details);
        DetailsDialog.Builder builder = new DetailsDialog.Builder(this, userDetailsView);
        mPreviewDialog = builder.build(true);

        mUserDetailsContentView = userDetailsView.findViewById(R.id.person_details);
        mUserDetailsLoading = userDetailsView.findViewById(R.id.pb_person_details_loading);
        mUserDetailsLoading.setVisibility(View.GONE);

        View editTagsView = mUserDetailsContentView.findViewById(R.id.edit_tags);
        mUserDetailsAdapter = new UserDetailsViewAdapter(
                userDetailsView,
                new TagsViewAdapter(editTagsView, R.id.et_tags));

        mUserDetailsAdapter = new UserDetailsViewAdapter(
                userDetailsView,
                new TagsViewAdapter(editTagsView, R.id.et_tags));
    }

    private void bindListeners() {
        mIntroductionViewAdapter.bindListeners();
        mContactViewAdapter.bindListeners();
        mTagsItemAdapter.bindListeners();
    }

    private void unbindListeners() {
        mIntroductionViewAdapter.unbindListeners();
        mContactViewAdapter.unbindListeners();
        mTagsItemAdapter.unbindListeners();
    }

    private void enableContent() {
        introductionLabelView.setVisibility(View.VISIBLE);
        contactLabelView.setVisibility(View.VISIBLE);
        tagsLabelView.setVisibility(View.VISIBLE);

        mIntroductionViewAdapter.expandContent();
        mContactViewAdapter.expandContent();
        mTagsItemAdapter.expandContent();
    }

    private void hideContent() {
        mIntroductionViewAdapter.collapseContent();
        mContactViewAdapter.collapseContent();
        mTagsItemAdapter.collapseContent();

        mRequestFinished = false;
    }

    private void adaptContent(UserProfile profile) {
        if (profile == null) {
            return;
        }
        mIntroductionViewAdapter.adaptContent(profile);
        mContactViewAdapter.adaptContent(profile);
        mTagsItemAdapter.adaptContent(profile.tags);
    }

    private boolean checkConfigurationRequested() {
        Intent starter = getIntent();
        if (starter != null) {
            boolean res = false;
            final String KEY_CONFIG_PROFILE_REQUESTED = MainActivity.KEY_CONFIG_PROFILE_REQUESTED;
            if (starter.hasExtra(KEY_CONFIG_PROFILE_REQUESTED)) {
                res = starter.getBooleanExtra(KEY_CONFIG_PROFILE_REQUESTED, false);
            }
            final String KEY_CONFIG_PROFILE_CONTENT = MainActivity.KEY_CONFIG_PROFILE_CONTENT;
            if (starter.hasExtra(KEY_CONFIG_PROFILE_CONTENT)) {
                UserModel model = starter.getParcelableExtra(KEY_CONFIG_PROFILE_CONTENT);
                ThisUser.setProfile(model.toUserProfile());
            }
            return res;
        }
        return false;
    }

    private void saveConfiguration(final UserProfile profile) {
        ThisUser.updateContent(this);
        if (!User.isProfileValid(ThisUser.getProfile())) {
            createUser(profile);
        }
        else {
            updateExistingUser(profile);
        }
    }

    private void createUser(final UserProfile profile) {
        final Context context = this;
        MaeventUserManager.getInstance().createUser(context, profile, new NetworkReceiver.Callback<String>() {
            @Override
            public void onSuccess(String data) {
                profile.id = Integer.valueOf(data);
                ThisUser.setProfile(profile);
                ThisUser.saveContent(context);
                Log.i(LOG_TAG, "Success. Id: " + ThisUser.getProfile().id);
                Log.i(LOG_TAG, "Data: " + data);
                setMainActivity();
            }

            @Override
            public void onError(Exception exception) {
                if (exception instanceof ClientError) {
                    Prompt.displayShort("Your profile is invalid  - probably name exists! Contact with support.", context);
                }
                else if (exception instanceof ServerError) {
                    Prompt.displayShort("No connection with server.", context);
                }
                else {
                    Prompt.displayShort("Internal error.", context);
                }
                bindListeners();
                mRequestFinished = true;
                mLoadingView.setVisibility(View.GONE);
            }
        });
    }

    private void updateExistingUser(final UserProfile profile) {
        final Context context = this;
        MaeventUserManager.getInstance().updateUser(context, profile, new NetworkReceiver.Callback<String>() {
            @Override
            public void onSuccess(String data) {
                profile.id = Integer.valueOf(data);
                ThisUser.setProfile(profile);
                Log.i(LOG_TAG, "Success. Id: " + ThisUser.getProfile().id);
                Log.i(LOG_TAG, "Data: " + data);
                setMainActivity();
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
                bindListeners();
                mRequestFinished = true;
                mLoadingView.setVisibility(View.GONE);
            }
        });
    }

    private void getUserProfile() {
        ThisUser.updateContent(this);
        int profileId = ThisUser.getProfile().id;
        if (profileId == 0) {
            Prompt.displayShort("Invalid user", getParent());
            return;
        }

        if (!mRequestFinished) {
            return;
        }
        mRequestFinished = false;

        invalidateOptionsMenu();

        final Context context = this;

        String identifier = String.valueOf(profileId);
        MaeventUserManager.getInstance().getUser(context, identifier, new NetworkReceiver.Callback<String>() {
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
                    ThisUser.updateContent(context);
                    updateUi(ThisUser.getProfile());
                    mRequestFinished = true;
                    return;
                }

                UserProfile profile = model.toUserProfile();
                ThisUser.setProfile(profile);
                ThisUser.saveContent(context);

                if (ThisUser.hasCompleteProfile()) {
                    setMainActivity();
                    return;
                }

                updateUi(profile);
                mRequestFinished = true;
            }

            @Override
            public void onError(Exception exception) {
                if (exception instanceof ClientError) {
                    Prompt.displayShort("Your profile is invalid - probably name exists! Contact with support.", context);
                }
                else if (exception instanceof ServerError) {
                    Prompt.displayShort("No connection with server.", context);
                    logout();
                }
                else {
                    Prompt.displayShort("Internal error.", context);
                }
                mRequestFinished = true;
            }
        });
    }

    private void updateUi(UserProfile profile) {
        adaptContent(profile);
        enableContent();
        bindListeners();
        initPreviewDialog();
        invalidateOptionsMenu();

        mContentView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTextIcons));
        mLoadingLocationView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
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

    private UserProfile extractProfileFromViews() {
        IntroductionViewHolder ivh = mIntroductionViewAdapter.getViewHolder();
        ContactViewHolder cvh = mContactViewAdapter.getViewHolder();
        TagsViewHolder tvh = mTagsItemAdapter.getViewHolder();

        ThisUser.updateContent(this);
        int userProfileId = ThisUser.getProfile().id;

        UserProfile profile = new UserProfile();
        if (userProfileId != 0) {
            profile.id = userProfileId;
        }
        profile.tags = new Tags();
        profile.firstName = ivh.mFirstNameEditText.getText().toString();
        profile.lastName = ivh.mLastNameEditText.getText().toString();
        profile.title = mIntroductionViewAdapter.getTitle();
        profile.pose = ivh.mPoseEditText.getText().toString();
        profile.headline = ivh.mHeadlineEditText.getText().toString();
        profile.phone = cvh.mPhoneEditText.getText().toString();
        profile.email = cvh.mEmailEditText.getText().toString();
        profile.linkedin = cvh.mLinkedinAccountEditText.getText().toString();
        profile.location = cvh.mLocationEditTextBuff.getText().toString();
        profile.tags.addAll(tvh.mEditTagView.getTagList());

        return profile;
    }
}

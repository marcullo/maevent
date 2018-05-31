package com.devmarcul.maevent;

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

import com.devmarcul.maevent.business_logic.ThisUser;
import com.devmarcul.maevent.common.TagsViewAdapter;
import com.devmarcul.maevent.configure_profile.ContactViewAdapter;
import com.devmarcul.maevent.configure_profile.ContactViewHolder;
import com.devmarcul.maevent.configure_profile.IntroductionViewAdapter;
import com.devmarcul.maevent.configure_profile.IntroductionViewHolder;
import com.devmarcul.maevent.configure_profile.ItemViewHolder;
import com.devmarcul.maevent.configure_profile.TagsItemAdapter;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.utils.dialog.TwoButtonsDialog;
import com.devmarcul.maevent.business_logic.MaeventAccountManager;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.dialog.TwoButtonsDialogListener;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ConfigureProfileActivity extends AppCompatActivity {

    private static String LOG_TAG = "ConfigureProfileActivity";

    private IntroductionViewAdapter mIntroductionViewAdapter;
    private ContactViewAdapter mContactViewAdapter;
    private TagsItemAdapter mTagsItemAdapter;

    private TwoButtonsDialog mCancelDialog;
    private boolean mCancelButtonVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInAccount account = MaeventAccountManager.getLastSignedAccount(this);
        ThisUser.updateContent(account);
        if (ThisUser.isRegistered() && !isConfigureProfileRequested()) {
            setMainActivity();
        }

        final UserProfile profile = ThisUser.getProfile();
        initUi(profile);
        mCancelButtonVisible = ThisUser.isRegistered();
        if (mCancelButtonVisible) {
            initCancelDialog();
        }

        Log.d(LOG_TAG, "Created.");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIntroductionViewAdapter.expandContent();
        mContactViewAdapter.expandContent();
        mTagsItemAdapter.expandContent();

        mIntroductionViewAdapter.bindListeners();
        mContactViewAdapter.bindListeners();
        mTagsItemAdapter.bindListeners();
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
        if (mCancelButtonVisible) {
            MenuItem cancelItem = menu.findItem(R.id.configure_profile_action_cancel);
            cancelItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.configure_profile_action_save) {
            saveConfiguration();
            setMainActivity();
            return true;
        }
        //TODO Move logout to the proper place
        if (id == R.id.configure_profile_action_logout) {
            MaeventAccountManager.signOut(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    setWelcomeActivity();
                }
            });
            return true;
        }
        if (id == R.id.configure_profile_action_cancel) {
            displayCancelDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------//

    private void initUi(UserProfile profile) {
        setContentView(R.layout.activity_configure_profile);

        final View introductionLabelView = findViewById(R.id.configure_profile_introduction_label);
        final View introductionView = findViewById(R.id.configure_profile_introduction);

        final View contactLabelView = findViewById(R.id.configure_profile_contact_label);
        final View contactView = findViewById(R.id.configure_profile_contact);

        final View tagsLabelView = findViewById(R.id.configure_profile_tags_label);
        final View tagsView = findViewById(R.id.configure_profile_tags);

        mIntroductionViewAdapter = new IntroductionViewAdapter(this, introductionLabelView, introductionView);
        mIntroductionViewAdapter.adaptContent(profile);

        mContactViewAdapter = new ContactViewAdapter(this, contactLabelView, contactView);
        mContactViewAdapter.adaptContent(profile);

        mTagsItemAdapter = new TagsItemAdapter(this, tagsLabelView, tagsView);
        mTagsItemAdapter.adaptContent(profile.tags);
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

    private void displayCancelDialog() {
        mCancelDialog.show();
    }
}

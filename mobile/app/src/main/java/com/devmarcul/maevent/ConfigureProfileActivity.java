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

import com.devmarcul.maevent.configure_profile.ContactViewHolder;
import com.devmarcul.maevent.configure_profile.IntroductionViewHolder;
import com.devmarcul.maevent.configure_profile.ItemViewHolder;
import com.devmarcul.maevent.configure_profile.TagsViewHolder;
import com.devmarcul.maevent.dialog.TwoButtonsDialog;
import com.devmarcul.maevent.dialog.TwoButtonsDialogListener;
import com.devmarcul.maevent.profile.MaeventAccountManager;
import com.devmarcul.maevent.profile.Profile;
import com.devmarcul.maevent.static_data.ConfigureProfileStaticData;
import com.devmarcul.maevent.static_data.MainActivityStaticData;
import com.devmarcul.maevent.utils.tools.Prompt;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ConfigureProfileActivity extends AppCompatActivity
        implements ConfigureProfileStaticData {

    private ItemViewHolder mIntroductionLabel;
    private IntroductionViewHolder mIntroductionViewHolder;
    private ItemViewHolder mContactLabel;
    private ContactViewHolder mContactViewHolder;
    private ItemViewHolder mTagsLabel;
    private TagsViewHolder mTagsViewHolder;

    private TwoButtonsDialog mCancelDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean configProfileRequested = false;
        Intent starter = getIntent();
        if (starter != null) {
            final String configProfileRequestedKey = MainActivityStaticData.KEY_CONFIG_PROFILE_REQUESTED;
            if (starter.hasExtra(configProfileRequestedKey)) {
                configProfileRequested = starter.getBooleanExtra(configProfileRequestedKey, false);
            }
        }

        GoogleSignInAccount account = MaeventAccountManager.getLastSignedAccount(this);
        Profile.updateContent(account);
        if (Profile.isValid() && !configProfileRequested) {
            setMainActivity();
        }

        setContentView(R.layout.activity_configure_profile);

        final View introductionLabelView = findViewById(R.id.configure_profile_introduction_label);
        final View introductionView = findViewById(R.id.configure_profile_introduction);
        final View contactLabelView = findViewById(R.id.configure_profile_contact_label);
        final View contactView = findViewById(R.id.configure_profile_contact);
        final View tagsLabelView = findViewById(R.id.configure_profile_tags_label);
        final View tagsView = findViewById(R.id.configure_profile_tags);

        final String introductionLabel = getString(R.string.configure_profile_introduction_label);
        final String contactLabel = getString(R.string.configure_profile_contact_label);
        final String tagsPickerLabel = getString(R.string.configure_profile_tags_picker_label);

        final int introductionIconResource = R.drawable.ic_configure_profile_introduction;
        final int contactIconResource = R.drawable.ic_configure_profile_contact;
        final int tagsPickerResource = R.mipmap.butterfly_tie;

        mIntroductionLabel = new ItemViewHolder(introductionLabelView, introductionView, introductionLabel, introductionIconResource, true);
        mContactLabel = new ItemViewHolder(contactLabelView, contactView, contactLabel, contactIconResource, true);
        mTagsLabel = new ItemViewHolder(tagsLabelView, tagsView, tagsPickerLabel, tagsPickerResource, true);

        mIntroductionViewHolder = new IntroductionViewHolder(this, introductionView);
        mContactViewHolder = new ContactViewHolder(this, contactView);
        mTagsViewHolder = new TagsViewHolder(this, tagsView);

        introductionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntroductionLabel.toggle();
            }
        });

        contactLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactLabel.toggle();
            }
        });

        tagsLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagsLabel.toggle();
            }
        });

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

        Log.d(LOG_TAG, "Created.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_configure_profile, menu);
        //TODO Refactor cancel item
        if (Profile.isValid()) {
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

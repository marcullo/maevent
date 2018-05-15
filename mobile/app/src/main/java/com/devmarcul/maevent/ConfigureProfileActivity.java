package com.devmarcul.maevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.devmarcul.maevent.profile.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ConfigureProfileActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SW/CONFIG_PROFILE";

    private ItemViewHolder mIntroductionLabel;
    private IntroductionViewHolder mIntroductionViewHolder;
    private ItemViewHolder mContactLabel;
    private ContactViewHolder mContactViewHolder;
    private ItemViewHolder mTagsLabel;
    private TagsViewHolder mTagsViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_profile);

        Profile.updateContent(this);

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

        Log.d(LOG_TAG, "Created.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.configure_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //TODO Move logout to the proper place
        if (id == R.id.action_logout) {
            signOut();
            return true;
        }
        if (id == R.id.action_collapse_all) {
            collapseAllItems();
            return true;
        }
        if (id == R.id.action_expand_all) {
            expandAllItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void collapseAllItems() {
        if (!mIntroductionLabel.isCollapsed()) {
            mIntroductionLabel.collapse();
        }
        if (!mContactLabel.isCollapsed()) {
            mContactLabel.collapse();
        }
        if (!mTagsLabel.isCollapsed()) {
            mTagsLabel.collapse();
        }
    }

    private void expandAllItems() {
        if (mIntroductionLabel.isCollapsed()) {
            mIntroductionLabel.expand();
        }
        if (mContactLabel.isCollapsed()) {
            mContactLabel.expand();
        }
        if (mTagsLabel.isCollapsed()) {
            mTagsLabel.expand();
        }
    }

    private void signOut() {
        WelcomeActivity.mSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setWelcomeActivity();
                    }
                });
    }

    private void setWelcomeActivity() {
        Log.d(LOG_TAG, "Setting welcome activity.");
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}

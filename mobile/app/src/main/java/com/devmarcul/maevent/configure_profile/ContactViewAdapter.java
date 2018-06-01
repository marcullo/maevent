package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.view.View;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.common.ContentAdapter;
import com.devmarcul.maevent.data.UserProfile;

public class ContactViewAdapter implements
        ContentAdapter<UserProfile, ContactViewHolder>,
        View.OnClickListener {

    private ContactViewHolder mViewHolder;
    ItemViewHolder mLabelViewHolder;

    public ContactViewAdapter(Context context, View labelView, View contentView) {
        mViewHolder = new ContactViewHolder(contentView);

        final String labelText = context.getString(R.string.configure_profile_contact_label);
        final int iconRes = R.drawable.ic_configure_profile_contact;
        mLabelViewHolder = new ItemViewHolder(labelView, contentView, labelText, iconRes, true);
    }

    @Override
    public void adaptContent(UserProfile profile) {
        mViewHolder.mPhoneEditText.setText(profile.phone);
        mViewHolder.mEmailEditText.setText(profile.email);
        mViewHolder.mLinkedinAccountEditText.setText(profile.linkedin);
        mViewHolder.mLocationButton.setText(profile.location);
    }

    @Override
    public void bindListeners() {
        mLabelViewHolder.view.setOnClickListener(this);
    }

    @Override
    public void unbindListeners() {
        mLabelViewHolder.view.setOnClickListener(null);
    }

    @Override
    public ContactViewHolder getViewHolder() {
        return mViewHolder;
    }

    @Override
    public void onClick(View v) {
        mLabelViewHolder.toggle();
    }

    public void expandContent() {
        mLabelViewHolder.expand();
    }

    public void collapseContent() {
        mLabelViewHolder.collapse();
    }
}

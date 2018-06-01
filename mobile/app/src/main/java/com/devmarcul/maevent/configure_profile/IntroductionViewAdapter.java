package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.common.ContentAdapter;
import com.devmarcul.maevent.data.UserProfile;

public class IntroductionViewAdapter implements
        ContentAdapter<UserProfile, IntroductionViewHolder>,
        View.OnClickListener {

    IntroductionViewHolder mViewHolder;
    ItemViewHolder mLabelViewHolder;

    public IntroductionViewAdapter(Context context, View labelView, View contentView) {
        mViewHolder = new IntroductionViewHolder(contentView);

        final String labelText = context.getString(R.string.configure_profile_introduction_label);
        final int iconRes = R.drawable.ic_configure_profile_introduction;
        mLabelViewHolder = new ItemViewHolder(labelView, contentView, labelText, iconRes, true);
    }

    @Override
    public void adaptContent(UserProfile profile) {
        int titleId = wrapTitleFromProfile(profile.title);

        mViewHolder.mFirstNameEditText.setText(profile.firstName);
        mViewHolder.mLastNameEditText.setText(profile.lastName);
        mViewHolder.mPoseEditText.setText(profile.pose);
        mViewHolder.mHeadlineEditText.setText(profile.headline);
        mViewHolder.mTitleSpinner.setSelection(titleId);
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
    public IntroductionViewHolder getViewHolder() {
        return mViewHolder;
    }

    @Override
    public void onClick(View v) {
        mLabelViewHolder.toggle();
    }

    public void adaptImage(Bitmap image) {
        if (image != null) {
            mViewHolder.mProfileImage.setImageBitmap(image);
        }
    }

    public void expandContent() {
        mLabelViewHolder.expand();
    }

    public void collapseContent() {
        mLabelViewHolder.collapse();
    }

    public String getTitle() {
        int i = mViewHolder.mTitleSpinner.getSelectedItemPosition();
        return mViewHolder.TITLES[i];
    }

    private int wrapTitleFromProfile(String profileTitle) {
        for (int i = 1; i < mViewHolder.TITLES.length; i++) {
            if (mViewHolder.TITLES[i].equals(profileTitle)) {
                return i;
            }
        }
        return 0;
    }
}

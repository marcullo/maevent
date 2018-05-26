package com.devmarcul.maevent.common;

import android.view.View;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.data.ThisUser;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;

public class UserDetailsViewAdapter implements
        ContentAdapter<User, UserDetailsViewHolder> {

    private UserDetailsViewHolder mViewHolder;
    private TagsViewAdapter mTagsViewAdapter;

    public UserDetailsViewAdapter(View viewHolderView, TagsViewAdapter adapter) {
        mViewHolder = new UserDetailsViewHolder(viewHolderView);
        mTagsViewAdapter = adapter;
    }

    @Override
    public void adaptContent(User user) {
        UserProfile profile = user.getProfile();
        adaptContent(profile);
    }

    public void adaptContent(UserProfile profile) {
        StringBuilder sb = new StringBuilder();
        sb.append(profile.firstName).append(" ").append(profile.lastName);
        String name = sb.toString();

        //TODO adapt photo
        mViewHolder.mPhotePhotoView.setImageResource(R.mipmap.maevent_logo);
        mViewHolder.mNameView.setText(name);
        mViewHolder.mLocationView.setText(profile.location);
        //TODO adapt rank
        mViewHolder.mRankView.setText("Professor");
        mViewHolder.mPoseView.setText(profile.pose);
        mViewHolder.mHeadlineView.setText(profile.headline);
        mViewHolder.mEmailView.setText(profile.email);
        mViewHolder.mLinkedinView.setText(profile.linkedin);

        mTagsViewAdapter.adaptContent(profile.tags);
        mTagsViewAdapter.adaptEditable(false);
    }

    @Override
    public void bindOnClickListeners() {
    }

    @Override
    public void unbindOnClickListeners() {
    }

    @Override
    public UserDetailsViewHolder getViewHolder() {
        return mViewHolder;
    }
}

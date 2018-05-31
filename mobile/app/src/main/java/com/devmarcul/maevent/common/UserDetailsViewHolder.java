package com.devmarcul.maevent.common;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmarcul.maevent.R;

public class UserDetailsViewHolder {

    private View view;

    public ImageView mPhotePhotoView;
    public TextView mNameView;
    public TextView mLocationView;
    public TextView mRankView;
    public TextView mPoseView;
    public TextView mHeadlineView;
    public TextView mEmailView;
    public TextView mLinkedinView;

    public UserDetailsViewHolder(View view) {
        this.view = view;
        mPhotePhotoView = view.findViewById(R.id.iv_person_photo);
        mNameView = view.findViewById(R.id.tv_person_details_name);
        mLocationView = view.findViewById(R.id.tv_person_location);
        mRankView = view.findViewById(R.id.tv_person_rank);
        mPoseView = view.findViewById(R.id.tv_person_pose);
        mHeadlineView = view.findViewById(R.id.tv_person_headline);
        mEmailView = view.findViewById(R.id.tv_person_email);
        mLinkedinView = view.findViewById(R.id.tv_person_in);
    }
}

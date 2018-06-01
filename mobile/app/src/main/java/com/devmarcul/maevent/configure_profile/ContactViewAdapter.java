package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.common.ContentAdapter;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.utils.StringUtils;
import com.google.android.gms.location.places.Place;

public class ContactViewAdapter implements
        ContentAdapter<UserProfile, ContactViewHolder>,
        View.OnClickListener {

    private ContactViewHolder mViewHolder;
    ItemViewHolder mLabelViewHolder;
    ImageButton.OnClickListener mLocationButtonListener;

    public ContactViewAdapter(Context context, View labelView, View contentView, ImageButton.OnClickListener locationButtonListener) {
        mViewHolder = new ContactViewHolder(contentView);
        mLocationButtonListener = locationButtonListener;

        final String labelText = context.getString(R.string.configure_profile_contact_label);
        final int iconRes = R.drawable.ic_configure_profile_contact;
        mLabelViewHolder = new ItemViewHolder(labelView, contentView, labelText, iconRes, true);
    }

    @Override
    public void adaptContent(UserProfile profile) {
        mViewHolder.mPhoneEditText.setText(profile.phone);
        mViewHolder.mEmailEditText.setText(profile.email);
        mViewHolder.mLinkedinAccountEditText.setText(profile.linkedin);
        mViewHolder.mLocationEditTextBuff.setText(profile.location);
    }

    @Override
    public void bindListeners() {
        mLabelViewHolder.view.setOnClickListener(this);
        mViewHolder.mLocationButton.setOnClickListener(mLocationButtonListener);
    }

    @Override
    public void unbindListeners() {
        mLabelViewHolder.view.setOnClickListener(null);
        mViewHolder.mLocationButton.setOnClickListener(null);
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

    public void updatePlaceViews(Place place) {
        if (place == null) {
            return;
        }
        if (place.getAddress() == null) {
            return;
        }

        mViewHolder.mLocationEditTextBuff.setText(place.getAddress());
        setPlaceSelected();
    }

    private void setPlaceSelected() {
        mViewHolder.mLocationButton.setImageDrawable(mLabelViewHolder.view.getContext().getDrawable(R.drawable.ic_done));
        mViewHolder.mLocationButton.setColorFilter(ContextCompat.getColor(mLabelViewHolder.view.getContext(), R.color.colorOk));
    }
}

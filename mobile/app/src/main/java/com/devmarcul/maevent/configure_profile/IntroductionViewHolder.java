package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.devmarcul.maevent.R;

public class IntroductionViewHolder {

    public static String TITLES[];

    private final View mContentView;
    public EditText mFirstNameEditText;
    public EditText mLastNameEditText;
    public EditText mPoseEditText;
    public EditText mHeadlineEditText;
    public ImageButton mProfileImage;
    public Spinner mTitleSpinner;

    public IntroductionViewHolder(View contentView) {
        mContentView = contentView;
        mFirstNameEditText = mContentView.findViewById(R.id.et_configure_profile_first_name);
        mLastNameEditText = mContentView.findViewById(R.id.et_configure_profile_last_name);
        mPoseEditText = mContentView.findViewById(R.id.et_configure_profile_pose);
        mHeadlineEditText = mContentView.findViewById(R.id.et_configure_profile_headline);
        mProfileImage = mContentView.findViewById(R.id.ibtn_configure_profile_image);
        mTitleSpinner = mContentView.findViewById(R.id.sp_configure_profile_title);

        initializeTitleSpinner();
    }

    private void initializeTitleSpinner() {
        Context context = mContentView.getContext();
        TITLES = context.getResources().getStringArray(R.array.configure_profile_ranks);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                context, R.array.configure_profile_ranks, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner.setAdapter(spinnerAdapter);
    }
}
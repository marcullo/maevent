package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.profile.Profile;

public class IntroductionViewHolder {
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mPoseEditText;
    private EditText mHeadlineEditText;
    private ImageButton mProfileImage;
    private Spinner mTitleSpinner;

    public IntroductionViewHolder(Context context, View itemView) {
        mFirstNameEditText = itemView.findViewById(R.id.et_configure_profile_first_name);
        mLastNameEditText = itemView.findViewById(R.id.et_configure_profile_last_name);
        mPoseEditText = itemView.findViewById(R.id.et_configure_profile_pose);
        mHeadlineEditText = itemView.findViewById(R.id.et_configure_profile_headline);
        mProfileImage = itemView.findViewById(R.id.ibtn_configure_profile_image);
        mTitleSpinner = itemView.findViewById(R.id.sp_configure_profile_title);

        initializeName();
        initializePhoto();
        initializeTitle(context);
        mPoseEditText.setText(Profile.getPose());
        mHeadlineEditText.setText(Profile.getHeadline());
    }

    private void initializeName() {
        mFirstNameEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mLastNameEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        String firstName = Profile.getFirstName();
        String lastName = Profile.getLastName();

        mFirstNameEditText.setText(firstName);
        mLastNameEditText.setText(lastName);
    }

    private void initializePhoto() {
        if (Profile.hasPhoto()) {
            Bitmap image = Profile.getPhoto();
            mProfileImage.setImageBitmap(image);
        }
    }

    private void initializeTitle(Context context) {
        int titleId = wrapTitleFromProfile(context);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                context, R.array.configure_profile_titles, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner.setAdapter(spinnerAdapter);
        mTitleSpinner.setSelection(titleId);
    }

    private int wrapTitleFromProfile(Context context) {
        String profileTitle = Profile.getTitle();
        String titles[] = context.getResources().getStringArray(R.array.configure_profile_titles);
        for (int i = 1; i < titles.length; i++) {
            if (titles[i].equals(profileTitle)) {
                return i;
            }
        }
        return 0;
    }


}
package com.devmarcul.maevent.configure_profile;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.devmarcul.maevent.R;

public class ContactViewHolder {

    private View view;
    public EditText mPhoneEditText;
    public EditText mEmailEditText;
    public EditText mLinkedinAccountEditText;
    public EditText mLocationEditTextBuff;
    public ImageButton mLocationButton;

    public ContactViewHolder(View itemView) {
        view = itemView;
        mPhoneEditText = view.findViewById(R.id.et_configure_project_phone);
        mEmailEditText = view.findViewById(R.id.et_configure_project_email);
        mLinkedinAccountEditText = view.findViewById(R.id.et_configure_project_linkedin);
        mLocationButton = view.findViewById(R.id.btn_configure_profile_location);
        mLocationEditTextBuff = view.findViewById(R.id.et_configure_project_location);
    }
}

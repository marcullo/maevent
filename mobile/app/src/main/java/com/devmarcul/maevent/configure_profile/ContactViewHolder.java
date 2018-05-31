package com.devmarcul.maevent.configure_profile;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devmarcul.maevent.R;
import com.devmarcul.maevent.business_logic.ThisUser;

public class ContactViewHolder {
    private View view;
    private EditText mPhoneEditText;
    private EditText mEmailEditText;
    private EditText mLinkedinAccountEditText;
    private Button mLocationButton;

    public ContactViewHolder(Context context, View itemView) {
        view = itemView;
        mPhoneEditText = view.findViewById(R.id.et_configure_project_phone);
        mEmailEditText = view.findViewById(R.id.et_configure_project_email);
        mLinkedinAccountEditText = view.findViewById(R.id.et_configure_project_linkedin);
        mLocationButton = view.findViewById(R.id.btn_configure_profile_location);

        mPhoneEditText.setText(ThisUser.getProfile().phone);
        mEmailEditText.setText(ThisUser.getProfile().email);
        mLinkedinAccountEditText.setText(ThisUser.getProfile().linkedin);
        mLocationButton.setText(ThisUser.getProfile().location);
    }
}

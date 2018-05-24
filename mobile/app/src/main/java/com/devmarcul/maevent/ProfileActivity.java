package com.devmarcul.maevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static String KEY_CONFIG_PROFILE_REQUESTED = "config-profile-requested";
    private static String LOG_TAG = "ProfileActivity";

    private FloatingActionButton editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editProfileButton = findViewById(R.id.btn_main_profile_edit);

        editProfileButton.setOnClickListener(this);
    }

    private void setConfigureProfileActivity() {
        Log.d(LOG_TAG, "Setting configure profile activity.");
        Intent intent = new Intent(this, ConfigureProfileActivity.class);
        intent.putExtra(KEY_CONFIG_PROFILE_REQUESTED, true);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        setConfigureProfileActivity();
    }
}

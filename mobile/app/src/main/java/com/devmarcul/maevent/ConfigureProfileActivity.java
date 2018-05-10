package com.devmarcul.maevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ConfigureProfileActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SW/CONFIG_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_profile);

        //TODO Move logout to the proper place
        FloatingActionButton logoutButton = findViewById(R.id.btn_configure_profile_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        Log.d(LOG_TAG, "Created.");
    }

    private void signOut() {
        WelcomeActivity.mSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setWelcomeActivity();
                    }
                });
    }

    private void setWelcomeActivity() {
        Log.d(LOG_TAG, "Setting welcome activity.");
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}

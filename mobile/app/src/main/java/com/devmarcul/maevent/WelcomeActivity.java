package com.devmarcul.maevent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devmarcul.maevent.profile.MaeventAccountManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class WelcomeActivity extends AppCompatActivity
        implements  GoogleApiClient.OnConnectionFailedListener,
                    Button.OnClickListener {

    private static String LOG_TAG = "WelcomeActivity";

    private int mScreenOrientation;
    private boolean mAnimationFinished;

    private ImageView mWelcomeImageView;
    private TextView mWelcomeTextView;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Hello to Maevent!");
        setContentView(R.layout.activity_welcome);

        MaeventAccountManager.setupClient(this);

        mAnimationFinished = false;

        mWelcomeImageView = findViewById(R.id.image_welcome);
        mWelcomeTextView = findViewById(R.id.message_welcome);
        mSignInButton = findViewById(R.id.btn_welcome_sign_in);

        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_welcome_sign_in) {
            if (hasInternetConnection()) {
                MaeventAccountManager.signInForResult(this);
            }
            else {
                showNoInternetPrompt();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = MaeventAccountManager.getLastSignedAccount(this);
        if (account != null) {
            setNextActivity();
        }

        if (!mAnimationFinished) {
            startAnimation();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Failed connection with Maevent account.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean result = MaeventAccountManager.handleSignInResult(requestCode, resultCode, data);
        if (result) {
            setNextActivity();
        }
    }

    //--------------------------------------------------------------------------------------------//

    private boolean hasInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    private void showNoInternetPrompt() {
        Toast toast = Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void startAnimation() {
        Context context = getApplicationContext();
        final int animId = R.anim.anim_welcome;
        Animation animation = AnimationUtils.loadAnimation(context, animId);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(LOG_TAG, "Welcome animation started.");
                mScreenOrientation = getRequestedOrientation();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(LOG_TAG, "Welcome animation finished.");
                mAnimationFinished = true;
                setRequestedOrientation(mScreenOrientation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mWelcomeImageView.startAnimation(animation);
        mWelcomeTextView.startAnimation(animation);
        mSignInButton.startAnimation(animation);
    }

    private void setNextActivity() {
        Log.d(LOG_TAG, "Setting next activity.");
        Intent intent = new Intent(this, ConfigureProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

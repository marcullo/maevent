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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class WelcomeActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "SW/WELCOME";
    private static final int RC_SIGN_IN = 9001;
    public static GoogleSignInClient mSignInClient;

    private int mScreenOrientation;

    private ImageView mWelcomeImageView;
    private TextView mWelcomeTextView;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Hello to Maevent!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(
          GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(this, signInOptions);

        mWelcomeImageView = findViewById(R.id.image_welcome);
        mWelcomeTextView = findViewById(R.id.message_welcome);
        mSignInButton = findViewById(R.id.btn_welcome_sign_in);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasInternetConnection()) {
                    signIn();
                }
                else {
                    showNoInternetPrompt();
                }
            }
        });
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        }
        return false;
    }

    private void showNoInternetPrompt() {
        Toast toast = Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            setNextActivity();
        }

        startAnimation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Failed connection with Google Auth.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "Getting back from Google sign in.");

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
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

    private void signIn() {
        Log.d(LOG_TAG, "Requesting Google sign in API.");
        Intent intent = mSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            setNextActivity();
        } catch (ApiException e) {
            Log.w(LOG_TAG, "sign in result failed. Code=" + e.getStatusCode());
        }
    }

    private void setNextActivity() {
        Log.d(LOG_TAG, "Setting next activity.");
        Intent intent = new Intent(this, ConfigureProfileActivity.class);
        startActivity(intent);
    }
}

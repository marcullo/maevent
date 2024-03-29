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

import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.business_logic.MaeventAccountManager;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.utils.dialog.DetailsDialog;
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

    private DetailsDialog mNoInternetConnectionDialog;
    private View mNoInternetConnectionView;


    private void startInviteActivity(Maevent event) {
        if (event == null) {
            return;
        }
        if (!event.isValid()) {
            return;
        }

        MaeventModel focusedModel = new MaeventModel(event);

        Log.d(LOG_TAG, "Setting next activity.");
        Intent intent = new Intent(this, InviteActivity.class);
        intent.putExtra(InviteActivity.KEY_PARCEL_FOCUSED_EVENT_MODEL, focusedModel);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Hello to Maevent!");
        setContentView(R.layout.activity_welcome);





/*
        Maevent event = new Maevent();

        MaeventParams params = new MaeventParams();
        params.name = "Hello, World!";
        params.place = "Belweder";
        params.addressStreet = "Belwederska 1";
        params.addressPostCode = "00-001 Warszawa";
        params.beginTime = "1000AM300518";
        params.endTime = "1000PM310518";
        params.rsvp = false;
        event.setParams(params);

        User host = new User();
        UserProfile profile = new UserProfile();
        profile.id = 8;
        profile.firstName = "Joseph";
        profile.lastName = "Pilsudski";
        host.setProfile(profile);
        event.setHost(host);

        event.setAttendeesIds(";8;");

        startInviteActivity(event);

*/





        MaeventAccountManager.setupClient(this);

        mAnimationFinished = false;

        mWelcomeImageView = findViewById(R.id.image_welcome);
        mWelcomeTextView = findViewById(R.id.message_welcome);
        mSignInButton = findViewById(R.id.btn_welcome_sign_in);
        mSignInButton.setOnClickListener(this);

        View  noInternetConnectionLayout = getLayoutInflater().inflate(R.layout.no_internet_connection, null);
        mNoInternetConnectionView = noInternetConnectionLayout.findViewById(R.id.no_internet_connection);

        DetailsDialog.Builder builder = new DetailsDialog.Builder(this, mNoInternetConnectionView);
        mNoInternetConnectionDialog = builder.build(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_welcome_sign_in) {
            if (hasInternetConnection()) {
                MaeventAccountManager.signInForResult(this);
            }
            else {
                mNoInternetConnectionDialog.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!hasInternetConnection()) {
            mNoInternetConnectionDialog.show();
            if (!mAnimationFinished) {
                startAnimation();
            }
            return;
        }

        GoogleSignInAccount account = MaeventAccountManager.getLastSignedAccount(this);
        if (account != null) {
            setNextActivity();
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

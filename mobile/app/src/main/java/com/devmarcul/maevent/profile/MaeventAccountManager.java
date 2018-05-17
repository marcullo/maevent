package com.devmarcul.maevent.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MaeventAccountManager {
    private static final String LOG_TAG = "SW/ACCOUNT";
    private static int RC_SIGN_IN = 9001;

    private static GoogleSignInClient mSignInClient;

    public static void setupClient(Context context) {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(context, signInOptions);
    }

    public static void signInForResult(FragmentActivity activity) {
        Intent intent = mSignInClient.getSignInIntent();
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    public static boolean handleSignInResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "Getting back from Google sign in. Result code: " + resultCode);

        boolean result = false;
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                result = true;
            } catch (ApiException e) {
                Log.w(LOG_TAG, "Sign in failed. Code: " + e.getStatusCode());
            }
        }
        return result;
    }

    public static GoogleSignInAccount getLastSignedAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public static void signOut(Activity activity, OnCompleteListener<Void> listener) {
        mSignInClient.signOut()
                .addOnCompleteListener(activity, listener);
    }
}

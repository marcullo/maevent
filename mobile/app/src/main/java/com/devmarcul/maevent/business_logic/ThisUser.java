package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.ClientError;
import com.android.volley.ServerError;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.content_providers.hardcoded.UserProfileBuilder;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.utils.Prompt;
import com.devmarcul.maevent.utils.StringUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThisUser {

    private static String LOG_TAG = "ThisUser";
    private static String PREFERENCE_THISUSER = "pref-thisuser";

    private static UserProfile profile;

    private static int accountId;
    private static String accountEmail;

    //TODO Refactor photo storage in order not to load from the internet constantly
    private static Bitmap photo;

    public static UserProfile getProfile() {
        return profile;
    }

    public static boolean isRegistered() {
        return profile != null && profile.id != 0;
    }

    public static boolean hasCompleteProfile() {
        return isRegistered() && User.isProfileValid(profile);
    }

    public static Bitmap getPhoto() {
        return photo;
    }

    public static void setProfile(UserProfile profile) {
        ThisUser.profile = profile;
    }

    public static synchronized void initializeContent(Context context, GoogleSignInAccount account) {
        if (account == null
                || account.getEmail() == null) {
            return;
        }
        if (profile == null) {
            profile = new UserProfile();
        }

        boolean res = restoreStatus(context);
        if (!res) {
            profile.id = 0;
            profile.email = null;
            Log.d(LOG_TAG, "User new in the system or internal failure");
            return;
        }

        profile.id = accountId;
    }

    public static synchronized void updateContent(Context context) {
        if (profile == null) {
            profile = new UserProfile();
        }

        boolean res = restoreStatus(context);
        if (!res) {
            profile.id = 0;
            profile.email = null;
            Log.d(LOG_TAG, "User new in the system or internal failure");
            return;
        }

        profile.id = accountId;
    }

    public static synchronized void saveContent(Context context) {
        saveStatus(context);
    }

    public static String getContentForDebug() {
        final String ENDL = StringUtils.getNewLine();
        StringBuilder sb = new StringBuilder();
        sb.append(ENDL);
        sb.append(profile.firstName).append(", ").append(profile.lastName).append(ENDL);
        sb.append(profile.email).append(", ").append(profile.phone).append(ENDL);
        sb.append("in: ").append(profile.linkedin)
                .append(", location: ").append(profile.location).append(ENDL);

        return sb.toString();
    }

    private static boolean saveStatus(Context context) {
        if (profile == null) {
            return false;
        }
        if (profile.id == 0) {
            return false;
        }
        if (profile.email == null) {
            return false;
        }
        if (context == null) {
            return false;
        }
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs == null) {
                return false;
            }
            SharedPreferences.Editor editor = prefs.edit();
            if (editor == null) {
                return false;
            }

            editor.putInt(PREFERENCE_THISUSER, profile.id);
            editor.apply();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    private static boolean restoreStatus(Context context) {
        if (context == null) {
            return false;
        }
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs == null) {
                return false;
            }

            int content = prefs.getInt(PREFERENCE_THISUSER, 0);
            if (content == 0) {
                return false;
            }
            accountId = content;

            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}

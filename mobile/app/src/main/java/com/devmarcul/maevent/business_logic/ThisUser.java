package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.ClientError;
import com.android.volley.ServerError;
import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.content_providers.hardcoded.UserProfileBuilder;
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

    private static GoogleSignInAccount googleAccount;
    private static UserProfile profile;

    //TODO Refactor photo storage in order not to load from the internet constantly
    private static Bitmap photo;

    public static UserProfile getProfile() {
        //TODO get from database
        //TODO replace with maevent steward
        return profile;
    }

    public static boolean hasCompleteProfile() {
        return profile != null && profile.id != 0;
    }

    public static Bitmap getPhoto() {
        return photo;
    }

    public static void setProfile(UserProfile profile) {
        ThisUser.profile = profile;
    }

    public static void updateContent(GoogleSignInAccount account) {
        //TODO Replace dummy initialization with data base query
        UserProfileBuilder.setCnt(0);
//        profile = UserProfileBuilder.build();
        profile = new UserProfile();
//        profile.id = 2000000008;
        profile.id = 0;

        googleAccount = account;

        final String debugContent = getContentForDebug();
        Log.d(LOG_TAG, "Profile: " + debugContent);
    }

    public static String getContentForDebug() {
        final String ENDL = StringUtils.getNewLine();
        StringBuilder sb = new StringBuilder();

        //TODO Hide sensitive data
        sb.append(ENDL);
        sb.append(profile.firstName).append(", ").append(profile.lastName).append(ENDL);
        sb.append(profile.email).append(", ").append(profile.phone).append(ENDL);
        sb.append("in: ").append(profile.linkedin)
                .append(", location: ").append(profile.location).append(ENDL);

        return sb.toString();
    }
}

package com.devmarcul.maevent.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.devmarcul.maevent.content_provider.hardcoded.UserProfileBuilder;
import com.devmarcul.maevent.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThisUser {

    private static String LOG_TAG = "ThisUser";

    private static GoogleSignInAccount googleAccount;
    private static UserProfile profile;
    private static boolean registered;

    //TODO Refactor photo storage in order not to load from the internet constantly
    private static Bitmap photo;

    public static UserProfile getProfile() {
        return profile;
    }

    public static boolean isRegistered() {
        return registered;
    }

    public static Bitmap getPhoto() {
        return photo;
    }

    public static void updateContent(GoogleSignInAccount account) {
        //TODO Replace dummy initialization with data base query
        UserProfileBuilder.setCnt(0);
        profile = UserProfileBuilder.build();
        registered = true;

        googleAccount = account;
        if (!registered && googleAccount != null) {
            updateFromGoogleAccount();
        }

        final String debugContent = getContentForDebug();
        Log.d(LOG_TAG, "Profile: " + debugContent);
    }

    public static String getContentForDebug() {
        final String ENDL = Utils.getNewLine();
        StringBuilder sb = new StringBuilder();

        //TODO Hide sensitive data
        sb.append(ENDL);
        sb.append(profile.firstName).append(", ").append(profile.lastName).append(ENDL);
        sb.append(profile.email).append(", ").append(profile.phone).append(ENDL);
        sb.append("in: ").append(profile.linkedin)
                .append(", location: ").append(profile.location).append(ENDL);

        return sb.toString();
    }

    private static void updateFromGoogleAccount() {
        String givenName = googleAccount.getGivenName();
        String familyName = googleAccount.getFamilyName();
        String email = googleAccount.getEmail();
        //TODO Add selecting image from local memory
        Uri photo = googleAccount.getPhotoUrl();

        if (!givenName.equals("null")) {
            profile.firstName = givenName;
        }
        if (!familyName.equals("null")) {
            profile.lastName = familyName;
        }
        if (!email.equals("null")) {
            profile.email = email;
        }

        if (photo != null) {
            profile.photo = photo;
            profile.hasPhoto = true;
        }
        else {
            profile.hasPhoto = false;
        }
    }

    private static class GetPhotoFromUrlTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            photo = bmp;
        }
    }
}

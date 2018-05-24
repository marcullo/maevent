package com.devmarcul.maevent.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Profile {

    private static String LOG_TAG = "Profile";

    private static ProfileContent content = new ProfileContent();
    private static GoogleSignInAccount googleAccount;
    //TODO Refactor photo storage in order not to load from the internet constantly
    private static Bitmap photo;

    public static boolean isValid() {
        return content.valid;
    }

    public static boolean hasPhoto() {
        return content.hasPhoto;
    }

    public static String getFirstName() {
        return content.firstName;
    }

    public static String getLastName() {
        return content.lastName;
    }

    public static String getTitle() {
        return content.title;
    }

    public static String getPose() {
        return content.pose;
    }

    public static String getHeadline() {
        return content.headline;
    }

    public static String getPhone() {
        return content.phone;
    }

    public static String getEmail() {
        return content.email;
    }

    public static String getLinkedin() {
        return content.linkedin;
    }

    public static String getLocation() {
        return content.location;
    }

    public static Bitmap getPhoto() {
        new GetPhotoFromUrlTask().execute(content.photo.toString());
        return photo;
    }

    public static List<String> getTags() {
        return content.tags;
    }

    private static void initializeContent() {
        content.email = "";
        content.firstName = "Mr.";
        content.lastName = "Nobody";
        content.title = "Phd.";
        content.pose = "Embedded Software Architect";
        content.headline = "Looking for 2 well-experienced dev-ops for collaboration.";
        content.phone = "+48123456789";
        content.linkedin = "maeventTest";
        content.location = "Warsaw";

        content.tags = new ArrayList<>();
        content.tags.add("Android");
        content.tags.add("Java");
        content.tags.add("Python");
        content.tags.add("SOLID");

        content.valid = false;
    }

    private static void initializeIncompleteContent() {
        content.email = "";
        content.firstName = "Incomplete";
        content.valid = false;
    }

    public static void updateContent(GoogleSignInAccount account) {

        //TODO Replace dummy initialization with data base query
        initializeContent();
        content.checkValidity();

        googleAccount = account;
        if (!content.valid && googleAccount != null) {
            updateFromGoogleAccount();
            content.checkValidity();
        }

        final String debugContent = content.getContentForDebug();
        Log.d(LOG_TAG, "Profile: " + debugContent);
    }

    private static void updateFromGoogleAccount() {
        String givenName = googleAccount.getGivenName();
        String familyName = googleAccount.getFamilyName();
        String email = googleAccount.getEmail();
        //TODO Add selecting image from local memory
        Uri photo = googleAccount.getPhotoUrl();

        if (!givenName.equals("null")) {
            content.firstName = givenName;
        }
        if (!familyName.equals("null")) {
            content.lastName = familyName;
        }
        if (!email.equals("null")) {
            content.email = email;
        }

        if (photo != null) {
            content.photo = photo;
            content.hasPhoto = true;
        }
        else {
            content.hasPhoto = false;
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

package com.devmarcul.maevent.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.devmarcul.maevent.utils.tools.Prompt;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile {
    private static ProfileContent content = new ProfileContent();
    private static GoogleSignInAccount googleAccount;
    //TODO Refactor photo storage in order not to load from the internet constantly
    private static Bitmap photo;
    private static String location;

    public static void updateContent(Context context) {
        //TODO Replace dummy initialization with data base query
        initializeContent();

        if (!isNameValid()) {
            googleAccount = GoogleSignIn.getLastSignedInAccount(context);
            if (googleAccount != null) {
                updateFromGoogleAccount();
            }
            else {
                Prompt.displayShort("Error while retrieving data!", context);
            }
        }
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

    private static void initializeContent() {
        content.firstName = "";
        content.lastName = "";
        content.title = "Phd.";
        content.pose = "Embedded Software Architect";
        content.headline = "Looking for 2 well-experienced dev-ops for collaboration.";
        content.phone = "+48123456789";
        content.linkedin = "maeventTest";
        content.location = "Warsaw";
    }

    private static boolean isNameValid() {
        return content.firstName != null
                && content.lastName != null
                && content.firstName.length() > 2
                && content.lastName.length() > 2
                && !content.firstName.equals(content.lastName);
    }

    private static void updateFromGoogleAccount() {
        String name = googleAccount.getDisplayName();
        String[] limbs = name.split(" ");

        content.firstName = limbs[0];
        content.lastName = limbs[1];
        content.email = googleAccount.getEmail();

        //TODO Add selecting image from local memory
        /*
        content.photo = googleAccount.getPhotoUrl();
        if (content.photo != null) {
            content.hasPhoto = true;
        }
        */
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

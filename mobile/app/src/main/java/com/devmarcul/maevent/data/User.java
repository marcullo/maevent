package com.devmarcul.maevent.data;

import android.util.Log;

import com.devmarcul.maevent.content_provider.hardcoded.UserProfileBuilder;
import com.devmarcul.maevent.utils.Utils;

public class User implements ContentUpdater, DataValidator {

    private static String LOG_TAG = "User";

    private UserProfile profile;
    private boolean registered;

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    @Override
    public void updateContent() {
        //TODO Replace dummy initialization with data base query
        profile = UserProfileBuilder.build();
        registered = true;

        final String debugContent = getContentForDebug();
        Log.d(LOG_TAG, "Profile: " + debugContent);
    }

    @Override
    public boolean checkValidity() {
        boolean valid = profile.firstName != null
                && profile.lastName != null
                && profile.firstName.length() > 1
                && profile.lastName.length() > 1
                && !profile.firstName.equals(profile.lastName)
                && profile.email != null
                && profile.email.length() > 5
                && profile.phone != null
                && profile.phone.length() > 8;
        return valid;
    }

    public String getContentForDebug() {
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
}

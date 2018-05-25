package com.devmarcul.maevent.business_logic;

import android.util.Log;

import com.devmarcul.maevent.content_provider.hardcoded.UserProfileBuilder;
import com.devmarcul.maevent.data.ContentUpdater;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;

public class UserManager implements ContentUpdater<User> {

    public static final String LOG_TAG = "UserManager";

    @Override
    public void updateContent(User user) {
        UserProfile profile = user.getProfile();

        //TODO Replace dummy initialization with data base query
        profile = UserProfileBuilder.build();

        if (User.isProfileValid(profile)) {
            user.setProfile(profile);
            user.setRegistered(true);
            final String debugContent = user.getContentForDebug();
            Log.d(LOG_TAG, "Profile: " + debugContent);
        }
    }
}

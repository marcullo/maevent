package com.devmarcul.maevent.business_logic;

import android.util.Log;

import com.devmarcul.maevent.content_providers.hardcoded.UserProfileBuilder;
import com.devmarcul.maevent.data.ContentUpdater;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.data.UserSearcher;

public class MaeventUserManager implements
        ContentUpdater<User>,
        UserSearcher {

    public static final String LOG_TAG = "MaeventUserManager";

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

    @Override
    public String getUserPhone(int userId) {
        //TODO Replace with query
        UserProfile[] userProfiles = UserProfileBuilder.getObjects();
        for (int i = 0; i < userProfiles.length; i++) {
            if (userProfiles[i].uid == userId) {
                return userProfiles[i].phone;
            }
        }
        return "Empty";
    }

    @Override
    public String getUserName(int userId) {
        //TODO Replace with query
        UserProfile[] userProfiles = UserProfileBuilder.getObjects();
        for (int i = 0; i < userProfiles.length; i++) {
            if (userProfiles[i].uid == userId) {
                return userProfiles[i].firstName + " " + userProfiles[i].lastName;
            }
        }
        return "Noname";
    }
}

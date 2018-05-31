package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.util.Log;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.content_providers.hardcoded.UserProfileBuilder;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;

public class MaeventUserManager implements
        UserContentUpdater,
        UserSearcher {

    public static final String LOG_TAG = "MaeventUserManager";

    private static final MaeventUserManager instance = new MaeventUserManager();

    public static synchronized MaeventUserManager getInstance() {
        return instance;
    }

    protected MaeventUserManager() {
    }

    @Override
    public void getUser(Context context, String identifier, NetworkReceiver.Callback<String> callback) {
        if (identifier == null) {
            return;
        }
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_USER, MaeventApi.Param.STRING, identifier, callback);
    }

    @Override
    public void updateUser(final Context context, UserProfile profile, NetworkReceiver.Callback<Boolean> callback) {
    }

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
            if (userProfiles[i].id == userId) {
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
            if (userProfiles[i].id == userId) {
                return userProfiles[i].firstName + " " + userProfiles[i].lastName;
            }
        }
        return "Noname";
    }
}

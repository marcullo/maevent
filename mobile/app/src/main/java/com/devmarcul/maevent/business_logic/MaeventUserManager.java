package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.util.Log;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.apis.models.UsersModel;
import com.devmarcul.maevent.business_logic.interfaces.UserContentUpdater;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.Maevents;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.data.Users;

public class MaeventUserManager implements UserContentUpdater {

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
    public void createUser(final Context context, UserProfile profile, NetworkReceiver.Callback<String> callback) {
        if (!User.isProfileValid(profile)) {
            Log.e(LOG_TAG, "User profile invalid!");
            return;
        }

        User user = new User();
        user.setProfile(profile);

        UserModel model = new UserModel(user);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.CREATE_USER, MaeventApi.Param.USER, model, callback);
    }

    @Override
    public void updateUser(final Context context, UserProfile profile, NetworkReceiver.Callback<String> callback) {
        if (!User.isProfileValid(profile)) {
            Log.e(LOG_TAG, "User profile invalid!");
            return;
        }

        User user = new User();
        user.setProfile(profile);

        UserModel model = new UserModel(user);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.UPDATE_USER, MaeventApi.Param.USER, model, callback);
    }

    @Override
    public void getAllUsers(final Context context, final NetworkReceiver.Callback<Users> callback) {
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_USERS, MaeventApi.Param.NONE, new NetworkReceiver.Callback<UsersModel>() {
                    @Override
                    public void onSuccess(UsersModel model) {
                        Users users = model.toUsers();
                        callback.onSuccess(users);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }

    @Override
    public void getAllAttendees(Context context, Maevent event, final NetworkReceiver.Callback<Users> callback) {
        String eventIdentifier = String.valueOf(event.getId());
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_ATTENDEES, MaeventApi.Param.STRING, eventIdentifier, new NetworkReceiver.Callback<UsersModel>() {
                    @Override
                    public void onSuccess(UsersModel model) {
                        Users users = model.toUsers();
                        callback.onSuccess(users);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }

    @Override
    public void getUsersByQuery(Context context, String query, final NetworkReceiver.Callback<Users> callback) {
        if (query == null) {
            return;
        }
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_USERS_BY_QUERY, MaeventApi.Param.STRING, query, new NetworkReceiver.Callback<UsersModel>() {
                    @Override
                    public void onSuccess(UsersModel model) {
                        Users users = model.toUsers();
                        callback.onSuccess(users);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }
}

package com.devmarcul.maevent.business_logic.interfaces;

import android.content.Context;

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.data.Users;

public interface UserContentUpdater {
    void getUser(final Context context, String identifier, NetworkReceiver.Callback<String> callback);
    void createUser(final Context context, UserProfile profile, NetworkReceiver.Callback<String> callback);
    void updateUser(final Context context, UserProfile profile, NetworkReceiver.Callback<String> callback);
    void getAllUsers(final Context context, final NetworkReceiver.Callback<Users> callback);
    void getAllAttendees(final Context context, Maevent event, final NetworkReceiver.Callback<Users> callback);
    void getUsersByQuery(final Context context, String query, final NetworkReceiver.Callback<Users> callback);
}

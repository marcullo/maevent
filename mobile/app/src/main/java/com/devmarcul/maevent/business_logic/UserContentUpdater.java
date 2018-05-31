package com.devmarcul.maevent.business_logic;

import android.content.Context;

import com.devmarcul.maevent.data.UserProfile;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;

public interface UserContentUpdater {
    void getUser(final Context context, String identifier, NetworkReceiver.Callback<String> callback);
    void updateUser(final Context context, UserProfile profile, NetworkReceiver.Callback<Boolean> callback);
}

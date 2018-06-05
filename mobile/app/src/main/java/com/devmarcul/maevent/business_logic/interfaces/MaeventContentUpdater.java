package com.devmarcul.maevent.business_logic.interfaces;

import android.content.Context;

import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.Maevents;
import com.devmarcul.maevent.data.UserProfile;

public interface MaeventContentUpdater {
    void createEvent(final Context context, Maevent event, NetworkReceiver.Callback<Boolean> callback);
    void getAllEvents(final Context context, final NetworkReceiver.Callback<Maevents> callback);
    void getAllEventsIntendedForUser(final Context context, UserProfile profile, final NetworkReceiver.Callback<Maevents> callback);
}
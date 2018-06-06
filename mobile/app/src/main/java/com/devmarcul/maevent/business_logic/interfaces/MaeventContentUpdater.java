package com.devmarcul.maevent.business_logic.interfaces;

import android.content.Context;

import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.Maevents;
import com.devmarcul.maevent.data.UserProfile;

public interface MaeventContentUpdater {
    void createEvent(final Context context, Maevent event, NetworkReceiver.Callback<Maevent> callback);
    void addAttendee(final Context context, Invitation invitation, NetworkReceiver.Callback<Maevent> callback);
    void getAllEvents(final Context context, final NetworkReceiver.Callback<Maevents> callback);
    void getAllEventsIntendedForUser(final Context context, UserProfile profile, final NetworkReceiver.Callback<Maevents> callback);
    void getEvent(final Context context, int id, final NetworkReceiver.Callback<Maevent> callback);
}
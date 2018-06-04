package com.devmarcul.maevent.business_logic.interfaces;

import android.content.Context;

import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.Maevents;

public interface MaeventContentUpdater {
    void createEvent(final Context context, MaeventParams params, NetworkReceiver.Callback<Boolean> callback);
    void getAllEvents(final Context context, final NetworkReceiver.Callback<Maevents> callback);
}
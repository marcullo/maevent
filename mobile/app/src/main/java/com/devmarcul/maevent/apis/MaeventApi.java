package com.devmarcul.maevent.apis;

import android.os.ResultReceiver;

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.utils.network.NetworkManager;

public interface MaeventApi {
    NetworkManager.UrlBuilder builder = new NetworkManager.UrlBuilder();

    String TAG = "MaeventApi";
    String RESULT_RECEIVER = "RESULT_RECEIVER";

    enum Action { GET_EVENTS, CREATE_EVENT };
    enum Param { NONE, EVENT };

    String URL_BASE = "https://maevent-api.conveyor.cloud/api/";
    String URL_EVENTS = builder.setBase(URL_BASE).build("events");

    void handleGetEvents(final ResultReceiver receiver);
    void handleCreateEvent(final ResultReceiver receiver, Maevent event);
}

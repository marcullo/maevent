package com.devmarcul.maevent.api;

import com.devmarcul.maevent.utils.network.NetworkManager;

public interface MaeventApi {
    NetworkManager.UrlBuilder builder = new NetworkManager.UrlBuilder();

    String TAG = "get";

    enum Action { GET_EVENTS, CREATE_EVENT };
    enum Param { NONE, EVENT };

    String URL_BASE = "https://maevent-api.conveyor.cloud/api/";
    String URL_EVENTS = builder.setBase(URL_BASE).build("events");
}

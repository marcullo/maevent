package com.devmarcul.maevent.business_logic;

import android.content.Context;

import com.devmarcul.maevent.api.MaeventApi;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.ThisUser;
import com.devmarcul.maevent.receivers.NetworkReceiver;
import com.devmarcul.maevent.services.NetworkService;

public class MaeventManager {

    private static final MaeventManager instance = new MaeventManager();

    public static synchronized MaeventManager getInstance() {
        return instance;
    }

    private MaeventManager() {
    }

    public void createEvent(final Context context, MaeventParams params, NetworkReceiver.Callback<Boolean> callback) {
        if (!Maevent.areParamsValid(params)) {
            return;
        }

        int hostId = ThisUser.getProfile().id;

        Maevent event = new Maevent();
        event.setParams(params);
        event.setHostId(hostId);
        event.setAttendeesNr(1);

//        NetworkService.startService(context, MaeventApi.Action.CREATE_EVENT, MaeventApi.Param.EVENT, event, callback);
        NetworkService.startService(context, MaeventApi.Action.GET_EVENTS, MaeventApi.Param.NONE, event, callback);
    }
}

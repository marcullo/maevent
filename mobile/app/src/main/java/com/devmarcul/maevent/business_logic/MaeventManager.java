package com.devmarcul.maevent.business_logic;

import android.content.Context;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;

public class MaeventManager {

    private static final MaeventManager instance = new MaeventManager();

    public static synchronized MaeventManager getInstance() {
        return instance;
    }

    protected MaeventManager() {
    }

    public void createEvent(final Context context, MaeventParams params, NetworkReceiver.Callback<Boolean> callback) {
        if (!Maevent.areParamsValid(params)) {
            return;
        }

        int id = ThisUser.getProfile().id;

        Maevent event = new Maevent();
        event.setParams(params);
        event.setHostId(id);
        event.setAttendeesIds(String.valueOf(id));

        MaeventModel model = new MaeventModel(event);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.CREATE_EVENT, MaeventApi.Param.EVENT, model, callback);
    }
}

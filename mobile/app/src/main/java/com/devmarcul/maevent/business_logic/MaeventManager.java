package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.os.Parcelable;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.MaeventApiModel;
import com.devmarcul.maevent.apis.models.MaeventModel;
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

    protected MaeventManager() {
    }

    public void createEvent(final Context context, MaeventParams params, NetworkReceiver.Callback<Boolean> callback) {
        if (!Maevent.areParamsValid(params)) {
            return;
        }

        int hostId = ThisUser.getProfile().id;

        Maevent event = new Maevent();
        params.beginTime = "2018-05-29T11:54:26.312170";
        params.endTime = "2018-05-29T11:54:26.312170";
        event.setParams(params);
        event.setHostId(hostId);
        event.setAttendeesNr(1);

        MaeventModel model = new MaeventModel(event);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.CREATE_EVENT, MaeventApi.Param.EVENT, model, callback);
    }
}

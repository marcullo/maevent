package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.util.Log;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.apis.models.MaeventsModel;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.data.Maevents;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;

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

        ThisUser.updateContent(context);
        UserProfile profile = ThisUser.getProfile();
        if (profile == null) {
            throw new IllegalStateException("ThisUser profile must not be null");
        }

        User host = new User();
        host.setProfile(profile);

        Maevent event = new Maevent();
        event.setParams(params);
        event.setHost(host);
        event.setAttendeesIds(String.valueOf(host.getProfile().id));

        MaeventModel model = new MaeventModel(event);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.CREATE_EVENT, MaeventApi.Param.EVENT, model, callback);
    }

    public void getAllEvents(final Context context, final NetworkReceiver.Callback<Maevents> callback) {
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_EVENTS, MaeventApi.Param.NONE, new NetworkReceiver.Callback<MaeventsModel>() {
                    @Override
                    public void onSuccess(MaeventsModel eventsModel) {
                        Maevents events = eventsModel.toMaevents();
                        callback.onSuccess(events);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }
}

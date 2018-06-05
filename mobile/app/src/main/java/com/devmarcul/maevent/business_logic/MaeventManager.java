package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.util.Log;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.apis.models.MaeventsModel;
import com.devmarcul.maevent.business_logic.interfaces.MaeventContentUpdater;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.data.Maevents;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;

public class MaeventManager implements MaeventContentUpdater {

    private static final MaeventManager instance = new MaeventManager();

    public static synchronized MaeventManager getInstance() {
        return instance;
    }

    protected MaeventManager() {
    }

    @Override
    public void createEvent(final Context context, Maevent event, NetworkReceiver.Callback<Boolean> callback) {
        if (event == null) {
            return;
        }
        if (!event.isValid()) {
            return;
        }

        MaeventModel model = new MaeventModel(event);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.CREATE_EVENT, MaeventApi.Param.EVENT, model, callback);
    }

    @Override
    public void getAllEvents(final Context context, final NetworkReceiver.Callback<Maevents> callback) {
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_EVENTS, MaeventApi.Param.NONE, new NetworkReceiver.Callback<MaeventsModel>() {
                    @Override
                    public void onSuccess(MaeventsModel model) {
                        Maevents events = model.toMaevents();
                        callback.onSuccess(events);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }

    @Override
    public void getAllEventsIntendedForUser(final Context context, UserProfile profile, final NetworkReceiver.Callback<Maevents> callback) {
        String identifier = String.valueOf(profile.id);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_EVENTS_INTENDED_FOR_USER, MaeventApi.Param.STRING, identifier, new NetworkReceiver.Callback<MaeventsModel>() {
                    @Override
                    public void onSuccess(MaeventsModel model) {
                        Maevents events = model.toMaevents();
                        callback.onSuccess(events);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }
}

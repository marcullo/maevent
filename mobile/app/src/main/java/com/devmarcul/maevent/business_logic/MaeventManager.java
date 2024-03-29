package com.devmarcul.maevent.business_logic;

import android.content.Context;
import android.util.Log;
import android.widget.ThemedSpinnerAdapter;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.InvitationModel;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.apis.models.MaeventsModel;
import com.devmarcul.maevent.business_logic.interfaces.MaeventContentUpdater;
import com.devmarcul.maevent.data.Invitation;
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
    public void createEvent(final Context context, Maevent event, final NetworkReceiver.Callback<Maevent> callback) {
        if (event == null) {
            return;
        }
        if (!event.isValid()) {
            return;
        }

        final MaeventModel model = new MaeventModel(event);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.CREATE_EVENT, MaeventApi.Param.EVENT, model, new NetworkReceiver.Callback<MaeventModel>() {
                    @Override
                    public void onSuccess(MaeventModel model) {
                        Maevent event = model.toMaevent();
                        callback.onSuccess(event);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }

    @Override
    public void addAttendee(Context context, Invitation invitation, final NetworkReceiver.Callback<Maevent> callback) {
        if (invitation == null) {
            return;
        }
        if (!invitation.isValid()) {
            return;
        }

        InvitationModel model = new InvitationModel(invitation);

        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.ADD_ATTENDEE, MaeventApi.Param.INVITATION, model, new NetworkReceiver.Callback<MaeventModel>() {
                    @Override
                    public void onSuccess(MaeventModel model) {
                        Maevent event = model.toMaevent();
                        callback.onSuccess(event);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }

    @Override
    public void deleteAttendee(Context context, int attendeId, Maevent event, final NetworkReceiver.Callback<Boolean> callback) {
        if (event == null) {
            return;
        }
        if (!event.isValid()) {
            return;
        }

        MaeventModel model = new MaeventModel(event);
        ThisUser.updateContent(context);
        String attendeeIdentifier = String.valueOf(ThisUser.getProfile().id);

        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.DELETE_ATTENDEE, MaeventApi.Param.EVENT, attendeeIdentifier, model, new NetworkReceiver.Callback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean model) {
                        callback.onSuccess(model);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
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

    @Override
    public void getEvent(Context context, int id, final NetworkReceiver.Callback<Maevent> callback) {
        String identifier = String.valueOf(id);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_EVENT, MaeventApi.Param.STRING, identifier, new NetworkReceiver.Callback<MaeventModel>() {
                    @Override
                    public void onSuccess(MaeventModel model) {
                        Maevent event = model.toMaevent();
                        callback.onSuccess(event);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }
}

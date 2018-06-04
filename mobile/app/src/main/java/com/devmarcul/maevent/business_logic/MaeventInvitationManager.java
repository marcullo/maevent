package com.devmarcul.maevent.business_logic;

import android.content.Context;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.InvitationsModel;
import com.devmarcul.maevent.business_logic.interfaces.InvitationContentUpdater;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.data.Invitations;

public class MaeventInvitationManager implements InvitationContentUpdater {

    private static final MaeventInvitationManager instance = new MaeventInvitationManager();

    public static synchronized MaeventInvitationManager getInstance() {
        return instance;
    }

    protected MaeventInvitationManager() {
    }

    @Override
    public void getAllInvitations(Context context, final NetworkReceiver.Callback<Invitations> callback) {
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_INVITATIONS, MaeventApi.Param.NONE, new NetworkReceiver.Callback<InvitationsModel>() {
                    @Override
                    public void onSuccess(InvitationsModel model) {
                        Invitations events = model.toInvitations();
                        callback.onSuccess(events);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }
}

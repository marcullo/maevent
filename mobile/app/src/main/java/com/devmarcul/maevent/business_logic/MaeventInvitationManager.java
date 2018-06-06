package com.devmarcul.maevent.business_logic;

import android.content.Context;

import com.devmarcul.maevent.apis.MaeventApi;
import com.devmarcul.maevent.apis.models.InvitationModel;
import com.devmarcul.maevent.apis.models.InvitationsModel;
import com.devmarcul.maevent.business_logic.interfaces.InvitationContentUpdater;
import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.business_logic.services.NetworkService;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Invitations;
import com.devmarcul.maevent.data.UserProfile;

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

    @Override
    public void getAllInvitationsIntendedForUser(Context context, UserProfile profile, final NetworkReceiver.Callback<Invitations> callback) {
        String identifier = String.valueOf(profile.id);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.GET_INVITATIONS_INTENDED_FOR_USER, MaeventApi.Param.STRING, identifier, new NetworkReceiver.Callback<InvitationsModel>() {
                    @Override
                    public void onSuccess(InvitationsModel model) {
                        Invitations invitations = model.toInvitations();
                        callback.onSuccess(invitations);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }

    @Override
    public void sendInvitation(Context context, Invitation invitation, final NetworkReceiver.Callback<Invitation> callback) {
        final InvitationModel model = new InvitationModel(invitation);
        NetworkService.getInstance()
                .startService(context, MaeventApi.Action.SEND_INVITATION, MaeventApi.Param.INVITATION, model, new NetworkReceiver.Callback<InvitationModel>() {
                    @Override
                    public void onSuccess(InvitationModel result) {
                        Invitation invitation = model.toInvitation();
                        callback.onSuccess(invitation);
                    }

                    @Override
                    public void onError(Exception exception) {
                        callback.onError(exception);
                    }
                });
    }

}

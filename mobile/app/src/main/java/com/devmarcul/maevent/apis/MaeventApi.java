package com.devmarcul.maevent.apis;

import android.os.ResultReceiver;

import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.utils.Network;

public interface MaeventApi {
    Network.UrlBuilder builder = new Network.UrlBuilder();

    String TAG = "MaeventApi";
    String RESULT_RECEIVER = "RESULT_RECEIVER";

    enum Action {
        GET_EVENTS, CREATE_EVENT,
        GET_USERS, GET_USER, CREATE_USER, UPDATE_USER,
        GET_INVITATIONS, GET_INVITATIONS_INTENDED_FOR_USER };
    enum Param { NONE, STRING, EVENT, USER };

    String URL_BASE = "https://maevent-api.conveyor.cloud/api/";
    String URL_EVENTS = builder.setBase(URL_BASE).build("events/");
    String URL_USERS = builder.setBase(URL_BASE).build("users/");
    String URL_INVITATIONS = builder.setBase(URL_BASE).build("invitations/");
    String URL_INVITATIONS_EVENT_ID = builder.setBase("ev=").build();
    String URL_INVITATIONS_EVENT_ID_ALL = builder.setBase("all").build();
    String URL_INVITATIONS_INVITEE = builder.setBase(URL_INVITATIONS).build("invitee");
    String URL_INVITATIONS_INVITEE_ID = builder.setBase("id=").build();

    void cancelAllRequests();

    /* /api/events */
    void handleGetEvents(final ResultReceiver receiver);
    void handleCreateEvent(final ResultReceiver receiver, MaeventModel model);

    /* /api/users */
    void handleGetUsers(final ResultReceiver receiver);
    void handleCreateUser(final ResultReceiver receiver, UserModel model);
    /* /api/users/identifier */
    void handleGetUser(final ResultReceiver receiver, String identifier);
    void handleUpdateUser(final ResultReceiver receiver, UserModel model);

    /* /api/invitations */
    void handleGetInvitations(final ResultReceiver receiver);
    void handleGetInvitationsIntendedForUser(final ResultReceiver receiver, String identifier);
}

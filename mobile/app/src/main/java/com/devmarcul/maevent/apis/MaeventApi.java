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
        GET_EVENTS, GET_EVENTS_INTENDED_FOR_USER, CREATE_EVENT,
        GET_USERS, GET_ATTENDEES, GET_USER, CREATE_USER, UPDATE_USER,
        GET_INVITATIONS, GET_INVITATIONS_INTENDED_FOR_USER };
    enum Param { NONE, STRING, EVENT, USER };

    String URL_EVENT_ID = "ev=";
    String URL_USER_ID = "id=";
    String URL_ALL = "all";

    String URL_BASE = "https://maevent-api.conveyor.cloud/api/";
    String URL_EVENTS               = builder.setBase(URL_BASE).build("events/");
    String URL_EVENTS_ATTENDEE      = builder.setBase(URL_BASE).build("events/attendee");
    String URL_USERS                = builder.setBase(URL_BASE).build("users/");
    String URL_EVENT_ATTENDEES      = builder.setBase(URL_BASE).build("users/event");
    String URL_INVITATIONS          = builder.setBase(URL_BASE).build("invitations/");
    String URL_INVITATIONS_INVITEE  = builder.setBase(URL_BASE).build("invitations/invitee");

    void cancelAllRequests();

    /* /api/events */
    void handleGetEvents(final ResultReceiver receiver);
    /* .........../attendee */
    void handleGetEventsIntendedForUser(final ResultReceiver receiver, String identifier);
    void handleCreateEvent(final ResultReceiver receiver, MaeventModel model);

    /* /api/users */
    void handleGetUsers(final ResultReceiver receiver);
    void handleGetAttendees(final ResultReceiver receiver, String eventIdentifier);
    void handleCreateUser(final ResultReceiver receiver, UserModel model);
    /* ........../identifier */
    void handleGetUser(final ResultReceiver receiver, String identifier);
    void handleUpdateUser(final ResultReceiver receiver, UserModel model);

    /* /api/invitations */
    void handleGetInvitations(final ResultReceiver receiver);
    /* ................/invitee */
    void handleGetInvitationsIntendedForUser(final ResultReceiver receiver, String identifier);
}

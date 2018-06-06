package com.devmarcul.maevent.apis;

import android.os.ResultReceiver;

import com.devmarcul.maevent.apis.models.InvitationModel;
import com.devmarcul.maevent.apis.models.MaeventModel;
import com.devmarcul.maevent.apis.models.UserModel;
import com.devmarcul.maevent.utils.Network;

public interface MaeventApi {
    Network.UrlBuilder builder = new Network.UrlBuilder();

    String TAG = "MaeventApi";
    String RESULT_RECEIVER = "RESULT_RECEIVER";

    enum Action {
        GET_EVENTS, GET_EVENTS_INTENDED_FOR_USER, GET_EVENT, CREATE_EVENT, ADD_ATTENDEE, DELETE_ATTENDEE,
        GET_USERS, GET_ATTENDEES, GET_USERS_BY_QUERY, GET_USER, CREATE_USER, UPDATE_USER,
        GET_INVITATIONS, GET_INVITATIONS_INTENDED_FOR_USER, SEND_INVITATION };
    enum Param { NONE, STRING, STRING2, EVENT, USER, INVITATION };

    String URL_ATTENDEES_TEXT = "attendees";
    String URL_INVITATION_ID = "inv=";
    String URL_ATTENDEE_ID = "insert=";
    String URL_ATTENDEE_DELETE_ID = "usr=";
    String URL_EVENT_ID = "ev=";
    String URL_QUERY_ID = "q=";
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
    void handleGetEvent(final ResultReceiver receiver, String identifier);
    void handleCreateEvent(final ResultReceiver receiver, MaeventModel model);
    /* .........../identifier/attendees */
    void handleAddAttendee(final ResultReceiver receiver, InvitationModel model);
    void handleDeleteAttendee(final ResultReceiver receiver, String attendeeIdentifier, MaeventModel model);

    /* .........../attendee */
    void handleGetEventsIntendedForUser(final ResultReceiver receiver, String identifier);

    /* /api/users */
    void handleGetUsers(final ResultReceiver receiver);
    void handleGetAttendees(final ResultReceiver receiver, String eventIdentifier);
    void handleGetUsersByQuery(final ResultReceiver receiver, String query);
    void handleCreateUser(final ResultReceiver receiver, UserModel model);
    /* ........../identifier */
    void handleGetUser(final ResultReceiver receiver, String identifier);
    void handleUpdateUser(final ResultReceiver receiver, UserModel model);

    /* /api/invitations */
    void handleGetInvitations(final ResultReceiver receiver);
    /* ................/invitee */
    void handleGetInvitationsIntendedForUser(final ResultReceiver receiver, String identifier);
    /* ................/multiple */
    void handleSendInvitation(final ResultReceiver receiver, InvitationModel model);
}

package com.devmarcul.maevent.data;

import com.devmarcul.maevent.utils.StringUtils;

public class Invitation extends Maevent {

    public static String LOG_TAG = "Invitation";

    private User inviter;
    private User invitee;
    private int eventId;
    private String message;

    public Invitation(Maevent event, User inviter, User invitee, String message) {
        this.params = event.params;
        this.attendeesIds = event.attendeesIds;
        this.host = event.host;
        this.inviter = inviter;
        this.invitee = invitee;
        this.eventId = event.getId();
        this.message = message;
    }

    public User getInviter() {
        return inviter;
    }

    public User getInvitee() {
        return invitee;
    }

    public int getEventId() {
        return eventId;
    }

    public UserProfile getInviterProfile() {
        return inviter.getProfile();
    }

    public UserProfile getInviteeProfile() {
        return invitee.getProfile();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean isValid() {
        boolean valid = super.isValid() && inviter.isValid();
        return valid;
    }

    @Override
    public String getContentForDebug() {
        final String ENDL = StringUtils.getNewLine();
        StringBuilder sb = new StringBuilder();

        sb.append(ENDL);
        sb.append(Invitation.LOG_TAG).append(ENDL);
        sb.append(super.getContentForDebug()).append(ENDL);
        sb.append("Inviter: ").append(inviter.getContentForDebug()).append(ENDL);
        sb.append("Invitee: ").append(invitee.getContentForDebug()).append(ENDL);
        sb.append("Event: ").append(super.getContentForDebug()).append(ENDL);
        return sb.toString();
    }
}

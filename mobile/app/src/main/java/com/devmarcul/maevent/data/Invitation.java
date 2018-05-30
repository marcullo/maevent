package com.devmarcul.maevent.data;

import com.devmarcul.maevent.utils.StringUtils;

public class Invitation extends Maevent {

    public static String LOG_TAG = "Invitation";

    private User inviter;
    private int inviteesNr;

    public Invitation(MaeventParams params, int hostId, User inviter, int inviteesNr) {
        super();
        this.params = params;
        this.hostUid = hostId;
        this.inviter = inviter;
        this.inviteesNr = inviteesNr;
    }

    public User getInviter() {
        return inviter;
    }

    public int getInviteesNr() {
        return inviteesNr;
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

        //TODO Hide sensitive data
        sb.append(ENDL);
        sb.append(Invitation.LOG_TAG).append(ENDL);
        sb.append(super.getContentForDebug()).append(ENDL);
        sb.append(inviter.getContentForDebug()).append(ENDL);
        sb.append("Invitees nr: ").append(inviteesNr).append(ENDL);
        return sb.toString();
    }
}

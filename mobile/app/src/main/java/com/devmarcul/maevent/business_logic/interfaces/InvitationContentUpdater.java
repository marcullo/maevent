package com.devmarcul.maevent.business_logic.interfaces;

import android.content.Context;

import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Invitations;
import com.devmarcul.maevent.data.UserProfile;

public interface InvitationContentUpdater {
    void getAllInvitations(final Context context, final NetworkReceiver.Callback<Invitations> callback);
    void getAllInvitationsIntendedForUser(final Context context, UserProfile profile, final NetworkReceiver.Callback<Invitations> callback);
    void sendInvitation(final Context context, Invitation invitation, final NetworkReceiver.Callback<Boolean> callback);
}

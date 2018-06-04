package com.devmarcul.maevent.business_logic.interfaces;

import android.content.Context;

import com.devmarcul.maevent.business_logic.receivers.NetworkReceiver;
import com.devmarcul.maevent.data.Invitations;

public interface InvitationContentUpdater {
    void getAllInvitations(final Context context, final NetworkReceiver.Callback<Invitations> callback);
}

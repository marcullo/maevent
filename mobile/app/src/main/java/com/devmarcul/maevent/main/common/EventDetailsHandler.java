package com.devmarcul.maevent.main.common;

import android.app.Activity;

import com.devmarcul.maevent.business_logic.MaeventSteward;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;

public class EventDetailsHandler implements EventDetailsViewAdapter.OnClickHandler {

    private Maevent mFocusedEvent;
    private Invitation mFocusedInvitation;
    private Activity parent;
    private OnClickJoinHandler onClickJoinHandler;

    public interface OnClickJoinHandler {
        void onClickJoin();
    }

    public EventDetailsHandler() {
    }

    public Maevent getFocus() {
        if (mFocusedEvent != null) {
            return mFocusedEvent;
        }
        else if (mFocusedInvitation != null) {
            return mFocusedInvitation;
        }
        return null;
    }

    public void setOnClickJoinHandler(OnClickJoinHandler handler) {
        onClickJoinHandler = handler;
    }

    public void focus(Maevent event, Invitation invitation) {
        if (event != null) {
            mFocusedEvent = event;
        }
        else if (invitation != null) {
            mFocusedInvitation = invitation;
        }
    }

    public void focus(Activity parent) {
        this.parent = parent;
    }

    @Override
    public void onClickCall() {
        Maevent focused;
        if (mFocusedEvent != null) {
            focused = mFocusedEvent;
        }
        else if (mFocusedInvitation != null) {
            focused = mFocusedInvitation;
        }
        else {
            return;
        }

        User host = focused.getHost();
        String hostPhone = host.getProfile().phone;
        MaeventSteward.callHost(hostPhone, parent);
    }

    @Override
    public void onClickLocation() {
        if (mFocusedEvent != null) {
            MaeventSteward.openEventLocation(mFocusedEvent, parent);
        }
        else if (mFocusedInvitation != null) {
            MaeventSteward.openEventLocation(mFocusedInvitation, parent);
        }
    }

    @Override
    public void onClickCalendar() {
        Maevent focused;
        if (mFocusedEvent != null) {
            focused = mFocusedEvent;
        }
        else if (mFocusedInvitation != null) {
            focused = mFocusedInvitation;
        }
        else {
            return;
        }

        UserProfile profile = focused.getHost().getProfile();

        StringBuilder builder = new StringBuilder();
        builder.append(profile.firstName).append(" ").append(profile.lastName);

        String hostName = builder.toString();
        String hostPhone = profile.phone;

        if (mFocusedEvent != null) {
            MaeventSteward.saveEventToCalendar(mFocusedEvent, hostName, hostPhone, parent);
        }
        else if (mFocusedInvitation != null) {
            MaeventSteward.saveEventToCalendar(mFocusedInvitation, hostName, hostPhone, parent);
        }
    }

    @Override
    public void onClickJoin() {
        if (onClickJoinHandler != null) {
            onClickJoinHandler.onClickJoin();
        }
    }
}

package com.devmarcul.maevent.main.common;

import android.app.Activity;

import com.devmarcul.maevent.business_logic.MaeventSteward;
import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Maevent;

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
        int hostId;
        if (mFocusedEvent != null) {
            hostId = mFocusedEvent.getHostId();
        }
        else if (mFocusedInvitation != null) {
            hostId = mFocusedInvitation.getHostId();
        }
        else {
            return;
        }
        String phone = MaeventSteward.getHostPhone(hostId);
        MaeventSteward.callHost(phone, parent);
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
        int hostId;
        if (mFocusedEvent != null) {
            hostId = mFocusedEvent.getHostId();
            MaeventSteward.saveEventToCalendar(mFocusedEvent, hostId, parent);
        }
        else if (mFocusedInvitation != null) {
            hostId = mFocusedInvitation.getHostId();
            MaeventSteward.saveEventToCalendar(mFocusedInvitation, hostId, parent);
        }
    }

    @Override
    public void onClickJoin() {
        if (onClickJoinHandler != null) {
            onClickJoinHandler.onClickJoin();
        }
    }
}

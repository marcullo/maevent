package com.devmarcul.maevent.event;

import com.devmarcul.maevent.data.Maevent;

public class InvitationContent {
    public Maevent event;
    public String attendee;

    public InvitationContent() {
        event = new Maevent();
        attendee = "";
    }
}

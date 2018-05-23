package com.devmarcul.maevent.event;

public class InvitationContent {
    public Maevent event;
    public String attendee;

    public InvitationContent() {
        event = new Maevent();
        attendee = "";
    }
}

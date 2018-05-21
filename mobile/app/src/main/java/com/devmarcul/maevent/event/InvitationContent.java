package com.devmarcul.maevent.event;

public class InvitationContent {
    public Maevent event;
    public String guest;

    public InvitationContent() {
        event = new Maevent();
        guest = "";
    }
}

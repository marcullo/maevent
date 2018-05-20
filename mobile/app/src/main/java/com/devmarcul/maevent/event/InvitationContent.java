package com.devmarcul.maevent.event;

public class InvitationContent {
    public Maevent event;
    public String host;

    public InvitationContent() {
        event = new Maevent();
        host = "";
    }
}

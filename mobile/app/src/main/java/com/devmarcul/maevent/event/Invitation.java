package com.devmarcul.maevent.event;

import com.devmarcul.maevent.static_data.InvitationStaticData;

public class Invitation implements InvitationStaticData {

    private InvitationContent content = new InvitationContent();

    public String getEventName() {
        return content.event.getName();
    }

    public String getEventHosts() {
        return content.host;
    }

    public String getEventPlace() {
        return content.event.getPlace();
    }

    public String getEventStartTime() {
        return content.event.getStartTime();
    }

    public void updateContent() {
        //TODO Replace dummy initialization with data base query
        initializeContent();
    }

    private void initializeContent() {
        content.event.updateContent();
        content.host = "Andrew Block";
    }

    private void initializeIncompleteContent() {
        content.host = "";
    }
}

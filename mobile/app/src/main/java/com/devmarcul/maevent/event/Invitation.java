package com.devmarcul.maevent.event;

import com.devmarcul.maevent.business_logic.MaeventManager;
import com.devmarcul.maevent.data.Maevent;

public class Invitation {

    private static String LOG_TAG = "Invitation";

    private InvitationContent content = new InvitationContent();

    public Maevent getEvent() {
        return content.event;
    }

    public String getEventName() {
        return content.event.getParams().name;
    }

    public String getEventHosts() {
        return "Michael Block";
    }

    public String getEventAttendees() {
        return content.attendee;
    }

    public String getEventPlace() {
        return content.event.getParams().place;
    }

    public String getEventStartTime() {
        return content.event.getParams().startTime;
    }

    public void updateContent() {
        //TODO Replace dummy initialization with data base query
        initializeContent();
    }

    private void initializeContent() {
        MaeventManager mm = new MaeventManager();
        mm.updateContent(content.event);
        content.attendee = "Andrew Block";
    }

    private void initializeIncompleteContent() {
        content.attendee = "";
    }
}

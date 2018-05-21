package com.devmarcul.maevent.event;

import android.util.Log;

import com.devmarcul.maevent.static_data.MaeventStaticData;

public class Maevent implements MaeventStaticData {

    private MaeventContent content = new MaeventContent();

    public boolean isValid() {
        return content.valid;
    }

    public boolean hasRsvp() {
        return content.rsvp;
    }

    public boolean wasConfirmed() {
        return content.confirmed;
    }

    public String getName() {
        return content.name;
    }

    public String getPlace() {
        return content.place;
    }

    public String getHosts() {
        return content.hosts;
    }

    public String getAddressStreet() {
        return content.addressStreet;
    }

    public String getAddressPostCode() {
        return content.addressPostCode;
    }

    public String getStartTime() {
        return content.startTime;
    }

    public String getStopTime() {
        return content.stopTime;
    }

    public void setRsvp(boolean rsvp) {
        content.rsvp = rsvp;
    }

    public void confirm(boolean presence) {
        content.confirmed = presence;
    }

    public void setTime(String start, String stop) {
        //TODO Add event time verification
        content.startTime = start;
        content.stopTime = stop;
    }

    public void updateContent() {
        //TODO Replace dummy initialization with data base query
        initializeContent();
        content.checkValidity();

        final String debugContent = content.getContentForDebug();
        Log.d(LOG_TAG, "Event: " + debugContent);
    }

    private void initializeContent() {
        content.name = "Presidential Banquet";
        content.hosts = "Michael Block";
        content.place = "Warsaw Belveder";
        content.addressStreet = "ul. Belwederska 54/56";
        content.addressPostCode = "00-001 Warszawa";
        content.startTime = "17:00, 20.05";
        content.stopTime = "23:00, 20.05";
        content.rsvp = true;
        content.valid = false;
    }

    private void initializeIncompleteContent() {
        content.name = "";
        content.valid = false;
    }
}

package com.devmarcul.maevent.event;

import com.devmarcul.maevent.utils.Utils;

class MaeventContent {
    public String name;
    public String hosts;
    public String place;
    public String addressStreet;
    public String addressPostCode;
    public String startTime;
    public String stopTime;
    public int inviteesNumber;
    public boolean rsvp;
    public boolean confirmed;
    public boolean valid;

    public MaeventContent() {
        name = "";
        hosts = "";
        place = "";
        addressStreet = "";
        addressPostCode = "";
        startTime = "";
        stopTime = "";
        inviteesNumber = 0;
        rsvp = false;
        confirmed = false;
        valid = false;
    }

    public boolean isPlaceValid() {
        return place != null && place.length() > 2;
    }

    public boolean isAddressValid() {
        return isAddressStreetValid() && isAddressPostCodeValid();
    }

    public boolean checkValidity() {
        valid = name != null
                && name.length() > 5
                && hosts != null
                && hosts.length() > 5
                && isPlaceValid()
                && isAddressValid()
                && startTime != null
                && startTime.length() > 5
                && stopTime != null
                && stopTime.length() > 5;
        return valid;
    }

    public String getContentForDebug() {
        //TODO Hide sensitive data
        final String ENDL = Utils.getNewLine();
        String content = ENDL;
        content += name + ENDL;
        content += "Hosts: " + hosts + ENDL;
        content += place + " (" + addressStreet + ", " + addressPostCode + ")" + ENDL;
        content += startTime + " - " + stopTime + " rsvp " + (rsvp ? "yes" : "not required") + ENDL;
        return content;
    }

    private boolean isAddressStreetValid() {
        return addressStreet != null && addressPostCode.length() > 6;
    }

    private boolean isAddressPostCodeValid() {
        return addressPostCode != null && addressPostCode.length() == 6;
    }
}

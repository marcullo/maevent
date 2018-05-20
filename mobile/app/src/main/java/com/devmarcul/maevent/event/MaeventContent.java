package com.devmarcul.maevent.event;

import com.devmarcul.maevent.utils.Utils;

class MaeventContent {
    public String name;
    public String place;
    public String address;
    public String startTime;
    public String stopTime;
    public boolean rsvp;
    public boolean confirmed;
    public boolean valid;

    public MaeventContent() {
        name = "";
        place = "";
        address = "";
        startTime = "";
        stopTime = "";
        rsvp = false;
        confirmed = false;
        valid = false;
    }

    public boolean isPlaceValid() {
        return place != null && place.length() > 2;
    }

    public boolean checkValidity() {
        valid = name != null
                && name.length() > 5
                && (
                    (isPlaceValid())
                 || (address != null && address.length() > 10)
                )
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
        content += place + " (" + address + ")" + ENDL;
        content += startTime + " - " + stopTime + " rsvp " + (rsvp ? "yes" : "not required") + ENDL;
        return content;
    }
}

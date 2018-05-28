package com.devmarcul.maevent.apis.models;

import com.devmarcul.maevent.apis.MaeventApiModel;

public class EventModel extends MaeventApiModel {
    public String Name;
    public String Place;
    public String AddressStreet;
    public String AddressPostCode;
    public String BeginTime;
    public String EndTime;
    public boolean Rsvp;
    public int HostId;
}

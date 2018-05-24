package com.devmarcul.maevent.content_provider.hardcoded;

import com.devmarcul.maevent.data.MaeventParams;

public class MaeventParamsBuilder {

    public static final int CNT = 5;
    private static int cnt = 0;
    private static MaeventParams object0 = new MaeventParams();
    private static MaeventParams object1 = new MaeventParams();
    private static MaeventParams object2 = new MaeventParams();
    private static MaeventParams object3 = new MaeventParams();
    private static MaeventParams object4 = new MaeventParams();
    private static MaeventParams[] objects;

    public static MaeventParams build() {
        initialize();
        assignObjects();

        MaeventParams objectCopy = MaeventParamsBuilder.objects[cnt];
        MaeventParams object = new MaeventParams();

        object.name = objectCopy.name;
        object.place = objectCopy.place;
        object.addressStreet = objectCopy.addressStreet;
        object.addressPostCode = objectCopy.addressPostCode;
        object.startTime = objectCopy.startTime;
        object.stopTime = objectCopy.stopTime;
        object.rsvp = objectCopy.rsvp;

        cnt++;
        if (cnt == CNT) {
            cnt = 0;
        }

        return object;
    }

    public static MaeventParams buildIncomplete() {
        MaeventParams object = new MaeventParams();
        object.name = "Incomplete Event";
        object.startTime = "1500230518";
        object.rsvp = true;
        return object;
    }

    private static void initialize() {
        object0.name = "Presidential Banquet";
        object0.place = "Belveder";
        object0.addressStreet = "ul. Belwederska 54/56";
        object0.addressPostCode = "00-001 Warszawa";
        object0.startTime   = "1700200518";
        object0.stopTime    = "2300200518";
        object0.rsvp = true;

        object1.name = "Young Leaders Conference";
        object1.place = "Palladium";
        object1.addressStreet = "ul. Zlota 9";
        object1.addressPostCode = "00-019 Warszawa";
        object1.startTime   = "0900240518";
        object1.stopTime    = "1800250518";
        object1.rsvp = false;

        object2.name = "Google I/O";
        object2.place = "Shoreline Amphitheatre";
        object2.addressStreet = "1 Amphitheatre Pkwy";
        object2.addressPostCode = "Mountain View, Ca 94043";
        object2.startTime   = "0900080518";
        object2.stopTime    = "1800110518";
        object2.rsvp = false;

        object3.name = "Software Specialists Summit";
        object3.place = "ICE Cracow";
        object3.addressStreet = "ul. Marii Konopnickiej 17";
        object3.addressPostCode = "30-302 Krakow";
        object3.startTime   = "0900280518";
        object3.stopTime    = "1600280518";
        object3.rsvp = false;

        object4.name = "Android Day";
        object4.place = "Conference Center NIMBUS";
        object4.addressStreet = "Al. Jerozolimskie 98";
        object4.addressPostCode = "00-807 Warszawa";
        object4.startTime   = "0000300518";
        object4.stopTime    = "2359300518";
        object4.rsvp = false;
    }

    private static void assignObjects() {
        objects = new MaeventParams[CNT];
        objects[0] = object0;
        objects[1] = object1;
        objects[2] = object2;
        objects[3] = object3;
        objects[4] = object4;
    }

}

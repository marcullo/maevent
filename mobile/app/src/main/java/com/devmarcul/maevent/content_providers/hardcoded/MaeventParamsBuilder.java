package com.devmarcul.maevent.content_providers.hardcoded;

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
        object.beginTime = objectCopy.beginTime;
        object.endTime = objectCopy.endTime;
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
        object.beginTime = "0300PM230518";
        object.rsvp = true;
        return object;
    }

    private static void initialize() {
        object0.name = "Presidential Banquet";
        object0.place = "Belveder";
        object0.addressStreet = "ul. Belwederska 54/56";
        object0.addressPostCode = "00-001 Warszawa";
        object0.beginTime   = "0600PM060618";
        object0.endTime     = "1200AM070618";
        object0.rsvp = true;

        object1.name = "Young Leaders Conference";
        object1.place = "Palladium";
        object1.addressStreet = "ul. Zlota 9";
        object1.addressPostCode = "00-019 Warszawa";
        object1.beginTime   = "0900AM240518";
        object1.endTime     = "0600PM250518";
        object1.rsvp = false;

        object2.name = "Google I/O";
        object2.place = "Shoreline Amphitheatre";
        object2.addressStreet = "1 Amphitheatre Pkwy";
        object2.addressPostCode = "Mountain View, Ca 94043";
        object2.beginTime   = "1900AM250518";
        object2.endTime     = "0400PM280518";
        object2.rsvp = true;

        object3.name = "Software Specialists Summit";
        object3.place = "ICE Cracow";
        object3.addressStreet = "ul. Marii Konopnickiej 17";
        object3.addressPostCode = "30-302 Krakow";
        object3.beginTime   = "0524AM260518";
        object3.endTime     = "2200PM260518";
        object3.rsvp = false;

        object4.name = "Android Day";
        object4.place = "Conference Center NIMBUS";
        object4.addressStreet = "Al. Jerozolimskie 98";
        object4.addressPostCode = "00-807 Warszawa";
        object4.beginTime   = "0500AM260518";
        object4.endTime     = "2200PM250518";
        object4.rsvp = true;
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

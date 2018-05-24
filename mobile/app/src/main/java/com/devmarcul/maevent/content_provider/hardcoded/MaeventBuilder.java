package com.devmarcul.maevent.content_provider.hardcoded;

import com.devmarcul.maevent.data.Maevent;

public class MaeventBuilder {

    public static final int CNT = 5;
    private static int cnt = 0;
    private static Maevent object0 = new Maevent();
    private static Maevent object1 = new Maevent();
    private static Maevent object2 = new Maevent();
    private static Maevent object3 = new Maevent();
    private static Maevent object4 = new Maevent();
    private static Maevent[] objects;

    public static Maevent build() {
        initialize();
        assignObjects();

        Maevent objectCopy = MaeventBuilder.objects[cnt];
        Maevent object = new Maevent();

        object.params = objectCopy.params;

        cnt++;
        if (cnt == CNT) {
            cnt = 0;
        }

        return object;
    }

    public static Maevent buildIncomplete() {
        Maevent object = new Maevent();
        object.params = MaeventParamsBuilder.buildIncomplete();
        return object;
    }

    private static void initialize() {
        object0.params = MaeventParamsBuilder.build();

        object1.params = MaeventParamsBuilder.build();

        object2.params = MaeventParamsBuilder.build();

        object3.params = MaeventParamsBuilder.build();

        object4.params = MaeventParamsBuilder.build();
    }

    private static void assignObjects() {
        objects = new Maevent[CNT];
        objects[0] = object0;
        objects[1] = object1;
        objects[2] = object2;
        objects[3] = object3;
        objects[4] = object4;
    }
}

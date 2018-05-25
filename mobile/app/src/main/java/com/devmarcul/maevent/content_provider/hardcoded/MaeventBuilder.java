package com.devmarcul.maevent.content_provider.hardcoded;

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;

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

        object.setParams(objectCopy.getParams());
        object.setHost(objectCopy.getHostId());
        object.setId(objectCopy.getId());

        cnt++;
        if (cnt == CNT) {
            cnt = 0;
        }

        return object;
    }

    public static Maevent buildIncomplete() {
        Maevent object = new Maevent();
        object.setParams(MaeventParamsBuilder.buildIncomplete());
        return object;
    }

    private static void initialize() {
        MaeventParams params;
        int hostId = 100;
        int id;

        params = MaeventParamsBuilder.build();
        hostId++;
        id = 3;
        object0.setParams(params);
        object0.setHost(hostId);
        object0.setId(id);

        params = MaeventParamsBuilder.build();
        hostId++;
        id = 4;
        object1.setParams(params);
        object1.setHost(hostId);
        object1.setId(id);

        params = MaeventParamsBuilder.build();
        hostId++;
        id = 5;
        object2.setParams(params);
        object2.setHost(hostId);
        object2.setId(id);

        params = MaeventParamsBuilder.build();
        hostId++;
        id = 6;
        object3.setParams(params);
        object3.setHost(hostId);
        object3.setId(id);

        params = MaeventParamsBuilder.build();
        hostId++;
        id = 7;
        object4.setParams(params);
        object4.setHost(hostId);
        object4.setId(id);
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

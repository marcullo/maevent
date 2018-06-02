package com.devmarcul.maevent.content_providers.hardcoded;

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.User;

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
        object.setHost(objectCopy.getHost());
        object.setId(objectCopy.getId());
        object.setInviteesNumber(objectCopy.getInviteesNumber());

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
        int id;

        params = MaeventParamsBuilder.build();
        id = 0;
        object0.setParams(params);
        object0.setHost(UserBuilder.build());
        object0.setId(id);
        object0.setInviteesNumber(220);

        params = MaeventParamsBuilder.build();
        id = 1;
        object1.setParams(params);
        object1.setHost(UserBuilder.build());
        object1.setId(id);
        object1.setInviteesNumber(44);

        params = MaeventParamsBuilder.build();
        id = 2;
        object2.setParams(params);
        object2.setHost(UserBuilder.build());
        object2.setId(id);
        object2.setInviteesNumber(23);

        params = MaeventParamsBuilder.build();
        id = 3;
        object3.setParams(params);
        object3.setHost(UserBuilder.build());
        object3.setId(id);
        object3.setInviteesNumber(100);

        params = MaeventParamsBuilder.build();
        id = 4;
        object4.setParams(params);
        object4.setHost(UserBuilder.build());
        object4.setId(id);
        object4.setInviteesNumber(60);
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

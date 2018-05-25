package com.devmarcul.maevent.content_provider.hardcoded;

import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.User;

public class InvitationBuilder {

    public static final int CNT = 5;
    private static int cnt = 0;
    private static Invitation object0;
    private static Invitation object1;
    private static Invitation object2;
    private static Invitation object3;
    private static Invitation object4;
    private static Invitation[] objects;

    public static Invitation build() {
        initialize();
        assignObjects();

        Invitation objectCopy = InvitationBuilder.objects[cnt];
        Invitation object = new Invitation(
                objectCopy.getParams(),
                objectCopy.getHostId(),
                objectCopy.getInviter(),
                objectCopy.getInviteesNr());

        cnt++;
        if (cnt == CNT) {
            cnt = 0;
        }

        return object;
    }

    private static void initialize() {
        MaeventParams params;
        int hostId = 100;
        User inviter;
        int inviteesNr = 2;

        params = MaeventParamsBuilder.build();
        hostId++;
        inviter = UserBuilder.build();
        inviteesNr++;
        object0 = new Invitation(params, hostId, inviter, inviteesNr);

        params = MaeventParamsBuilder.build();
        hostId++;
        inviter = UserBuilder.build();
        inviteesNr++;
        object1 = new Invitation(params, hostId, inviter, inviteesNr);

        params = MaeventParamsBuilder.build();
        hostId++;
        inviter = UserBuilder.build();
        inviteesNr++;
        object2 = new Invitation(params, hostId, inviter, inviteesNr);

        params = MaeventParamsBuilder.build();
        hostId++;
        inviter = UserBuilder.build();
        inviteesNr++;
        object3 = new Invitation(params, hostId, inviter, inviteesNr);

        params = MaeventParamsBuilder.build();
        hostId++;
        inviter = UserBuilder.build();
        inviteesNr++;
        object4 = new Invitation(params, hostId, inviter, inviteesNr);
    }

    private static void assignObjects() {
        objects = new Invitation[CNT];
        objects[0] = object0;
        objects[1] = object1;
        objects[2] = object2;
        objects[3] = object3;
        objects[4] = object4;
    }
}

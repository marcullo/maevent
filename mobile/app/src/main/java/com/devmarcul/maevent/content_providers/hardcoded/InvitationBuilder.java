package com.devmarcul.maevent.content_providers.hardcoded;

import com.devmarcul.maevent.data.Invitation;
import com.devmarcul.maevent.data.Maevent;
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
        Maevent event = objectCopy;
        Invitation object = new Invitation(
                event,
                objectCopy.getInviter(),
                objectCopy.getInvitee(),
                objectCopy.getMessage());

        cnt++;
        if (cnt == CNT) {
            cnt = 0;
        }

        return object;
    }

    private static void initialize() {
        Maevent event;
        MaeventParams params;
        User inviter;
        User invitee;
        String message = "Let's go!";
        int inviteesNr = 2;

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        invitee = UserBuilder.build();
        inviteesNr++;
        event = MaeventBuilder.build();
        object0 = new Invitation(event, inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        event = MaeventBuilder.build();
        object1 = new Invitation(event, inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        event = MaeventBuilder.build();
        object2 = new Invitation(event, inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        event = MaeventBuilder.build();
        object3 = new Invitation(event, inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        message = "";
        event = MaeventBuilder.build();
        object4 = new Invitation(event, inviter, invitee, message);
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

package com.devmarcul.maevent.content_providers.hardcoded;

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
                objectCopy.getAttendeesIds(),
                objectCopy.getHost(),
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
        MaeventParams params;
        User inviter;
        User invitee;
        String message = "Let's go!";
        int inviteesNr = 2;

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        invitee = UserBuilder.build();
        inviteesNr++;
        object0 = new Invitation(params, ";1;2;", UserBuilder.build(), inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        object1 = new Invitation(params,";2;6;7;", UserBuilder.build(), inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        message = "You're welcome!";
        object2 = new Invitation(params, ";5;", UserBuilder.build(), inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        object3 = new Invitation(params, ";1;2;3;4;5;6;7;", UserBuilder.build(), inviter, invitee, message);

        params = MaeventParamsBuilder.build();
        inviter = UserBuilder.build();
        inviteesNr++;
        message = "";
        object4 = new Invitation(params, "5;6;7;8;9;", UserBuilder.build(), inviter, invitee, message);
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

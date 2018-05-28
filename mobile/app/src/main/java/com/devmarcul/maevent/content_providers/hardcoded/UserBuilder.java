package com.devmarcul.maevent.content_providers.hardcoded;

import com.devmarcul.maevent.data.User;
import com.devmarcul.maevent.data.UserProfile;

public class UserBuilder {

    public static final int CNT = 5;
    private static int cnt = 0;
    private static User object0 = new User();
    private static User object1 = new User();
    private static User object2 = new User();
    private static User object3 = new User();
    private static User object4 = new User();
    private static User[] objects;


    public static User build() {
        initialize();
        assignObjects();

        User objectCopy = UserBuilder.objects[cnt];
        User object = new User();

        UserProfile profile = objectCopy.getProfile();
        object.setProfile(profile);

        cnt++;
        if (cnt == CNT) {
            cnt = 0;
        }

        return object;
    }

    public static User buildIncomplete() {
        User object = new User();
        UserProfile profile = UserProfileBuilder.buildIncomplete();
        object.setProfile(profile);
        return object;
    }

    private static void initialize() {
        object0.setProfile(UserProfileBuilder.build());

        object1.setProfile(UserProfileBuilder.build());

        object2.setProfile(UserProfileBuilder.build());

        object3.setProfile(UserProfileBuilder.build());

        object4.setProfile(UserProfileBuilder.build());
    }

    private static void assignObjects() {
        objects = new User[CNT];
        objects[0] = object0;
        objects[1] = object1;
        objects[2] = object2;
        objects[3] = object3;
        objects[4] = object4;
    }
}

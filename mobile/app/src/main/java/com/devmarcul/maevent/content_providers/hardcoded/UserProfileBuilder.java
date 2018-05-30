package com.devmarcul.maevent.content_providers.hardcoded;

import com.devmarcul.maevent.data.UserProfile;

import java.util.ArrayList;

public class UserProfileBuilder {

    public static final int INTITIAL_USER_ID = 100;
    public static final int CNT = 5;
    private static int cnt = 0;
    private static UserProfile object0 = new UserProfile();
    private static UserProfile object1 = new UserProfile();
    private static UserProfile object2 = new UserProfile();
    private static UserProfile object3 = new UserProfile();
    private static UserProfile object4 = new UserProfile();
    private static UserProfile[] objects;

    public static UserProfile[] getObjects() {
        return objects;
    }

    public static void setCnt(int cnt) {
        if (cnt >= CNT) {
            UserProfileBuilder.cnt = CNT - 1;
        }
        else if (cnt < 0) {
            UserProfileBuilder.cnt = 0;
        }
        UserProfileBuilder.cnt = cnt;
    }

    public static UserProfile build() {
        initialize();
        assignObjects();

        UserProfile objectCopy = UserProfileBuilder.objects[cnt];
        UserProfile object = new UserProfile();

        object.email = objectCopy.email;
        object.firstName = objectCopy.firstName;
        object.lastName = objectCopy.lastName;
        object.title = objectCopy.title;
        object.pose = objectCopy.pose;
        object.headline = objectCopy.headline;
        object.phone = objectCopy.phone;
        object.linkedin = objectCopy.linkedin;
        object.location = objectCopy.location;
        object.tags = objectCopy.tags;

        cnt++;
        if (cnt == CNT) {
            cnt = 0;
        }

        return object;
    }

    public static UserProfile buildIncomplete() {
        UserProfile object = new UserProfile();
        object.firstName = "Andrew";
        object.lastName = "Block";
        object.email = "test.maevent@gmail.com";
        return object;
    }

    private static void initialize() {
        int id = INTITIAL_USER_ID;

        id++;
        object0.uid = id;
        object0.email = "test.maevent@gmail.com";
        object0.firstName = "Andrew";
        object0.lastName = "Block";
        object0.title = "Phd.";
        object0.pose = "Embedded Software Architect";
        object0.headline = "Looking for 2 well-experienced dev-ops for collaboration.";
        object0.phone = "++48123456789";
        object0.linkedin = "maeventTest";
        object0.location = "Warsaw";
        object0.tags = new ArrayList<>();
        object0.tags.add("Android");
        object0.tags.add("Java");
        object0.tags.add("Python");
        object0.tags.add("SOLID");

        id++;
        object1.uid = id;
        object1.email = "mr-nobody@nobody.com";
        object1.firstName = "Mr.";
        object1.lastName = "Nobody";
        object1.title = "Phd.";
        object1.pose = "Taxi Rider";
        object1.headline = "Everything matters";
        object1.phone = "+22195332004";
        object1.linkedin = "noempty";
        object1.location = "Cracow";
        object1.tags = new ArrayList<>();
        object1.tags.add("Loop");
        object1.tags.add("Fast cars");

        id++;
        object2.uid = id;
        object2.email = "hey.me@gmail.com";
        object2.firstName = "Michael";
        object2.lastName = "Block";
        object2.title = "Eng.";
        object2.pose = "Cross-platform Dev-Ops";
        object2.headline = "Looking for 2 new field of experience.";
        object2.phone = "+02 20041 22";
        object2.linkedin = "medevop";
        object2.location = "Warsaw";
        object2.tags = new ArrayList<>();
        object2.tags.add("Android");
        object2.tags.add("Java");
        object2.tags.add("Python");
        object2.tags.add("SOLID");
        object2.tags.add("Ruby");
        object2.tags.add("TDD");
        object2.tags.add("Maevent");

        id++;
        object3.uid = id;
        object3.email = "jsklansky@gmail.com";
        object3.firstName = "Justine";
        object3.lastName = "Sklansky";
        object3.title = "M.Eng.";
        object3.pose = "Android Developer";
        object3.headline = "Androids eats only apples.";
        object3.phone = "+4212234055";
        object3.linkedin = "Jusklansky";
        object3.location = "New York";
        object3.tags = new ArrayList<>();

        id++;
        object4.uid = id;
        object4.email = "blockandregmail.com";
        object4.firstName = "Andre";
        object4.lastName = "Blockings";
        object4.title = "Phd.";
        object4.pose = "Full-stack Developer";
        object4.headline = "I give only negative feedback.";
        object4.phone = "+08 03 02 05 04";
        object4.linkedin = "blockandre";
        object4.location = "Gdansk";
        object4.tags = new ArrayList<>();
    }

    private static void assignObjects() {
        objects = new UserProfile[CNT];
        objects[0] = object0;
        objects[1] = object1;
        objects[2] = object2;
        objects[3] = object3;
        objects[4] = object4;
    }
}

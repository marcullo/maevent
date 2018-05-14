package com.devmarcul.maevent.profile;

import android.net.Uri;

public class ProfileContent {
    public String firstName;
    public String lastName;
    public String title;
    public String pose;
    public String headline;
    public String phone;
    public String email;
    public String linkedin;
    public String location;
    public boolean hasPhoto;
    public Uri photo;

    ProfileContent() {
        firstName = "";
        lastName = "";
        title = "";
        pose = "";
        headline = "";
        phone = "";
        email = "";
        linkedin = "";
        location = "";
        hasPhoto = false;
        photo = null;
    }
}

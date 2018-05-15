package com.devmarcul.maevent.profile;

import android.net.Uri;

import java.util.List;

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
    public List<String> tags;
    public Uri photo;
    public boolean valid;

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
        tags = null;
        hasPhoto = false;
        photo = null;
        valid = false;
    }
}

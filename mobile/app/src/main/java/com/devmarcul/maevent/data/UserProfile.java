package com.devmarcul.maevent.data;

import android.net.Uri;

import java.util.List;

public class UserProfile {

    private static String LOG_TAG = "UserProfile";

    public int id;
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
    public Tags tags;
    public Uri photo;
}

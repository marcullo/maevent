package com.devmarcul.maevent.profile;

import android.net.Uri;

import com.devmarcul.maevent.utils.Utils;

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

    public boolean isValid() {
        return firstName != null
                && lastName != null
                && firstName.length() > 1
                && lastName.length() > 1
                && !firstName.equals(lastName)
                && phone != null
                && phone.length() > 8
                && hasPhoto;
    }

    public String getContentForDebug() {
        //TODO Hide sensitive data
        final String ENDL = Utils.getNewLine();
        String content = ENDL;
        content += firstName + ", " + lastName + ENDL;
        content += email + ", " + phone + ENDL;
        content += "in: " + linkedin + ", location: " + location + ENDL;
        return content;
    }
}

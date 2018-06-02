package com.devmarcul.maevent.data;

import com.devmarcul.maevent.utils.StringUtils;

public class User implements DataValidator {

    private static String LOG_TAG = "User";

    private UserProfile profile;
    private boolean registered;

    public UserProfile getProfile() {
        return profile;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public boolean isValid() {
        return isProfileValid(profile);
    }

    public static boolean isProfileValid(UserProfile profile) {
        boolean valid = profile != null
                && isNameValid(profile.firstName, profile.lastName)
                && profile.email != null
                && profile.email.length() > 5
                && profile.phone != null
                && profile.phone.length() > 8;
        return valid;
    }

    public static boolean isNameValid(String firstName, String lastName) {
        return firstName != null
                && lastName != null
                && firstName.length() > 1
                && lastName.length() > 3
                && !firstName.contentEquals(lastName);
    }

    public String getContentForDebug() {
        final String ENDL = StringUtils.getNewLine();
        StringBuilder sb = new StringBuilder();

        sb.append(ENDL);
        sb.append(profile.firstName).append(", ").append(profile.lastName).append(ENDL);
        sb.append(profile.email).append(", ").append(profile.phone).append(ENDL);
        sb.append("in: ").append(profile.linkedin)
                .append(", location: ").append(profile.location).append(ENDL);

        return sb.toString();
    }
}

package com.devmarcul.maevent.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.devmarcul.maevent.utils.StringUtils;

public class Maevent implements DataValidator {

    private static String LOG_TAG = "Maevent";

    protected int id;
    protected int hostId;
    protected int attendeesNr;
    protected MaeventParams params;

    public MaeventParams getParams() {
        return params;
    }

    public int getId() {
        return id;
    }

    public int getHostId() {
        return hostId;
    }

    public int getAttendeesNr() {
        return attendeesNr;
    }

    public void setParams(MaeventParams params) {
        this.params = params;
    }

    public void setName(String name) {
        params.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public void setAttendeesNr(int attendeesNr) {
        this.attendeesNr = attendeesNr;
    }

    @Override
    public boolean isValid() {
        return areParamsValid(params);
    }

    public static boolean areParamsValid(MaeventParams params) {
        boolean valid = isNameValid(params.name)
                && isPlaceValid(params)
                && isAddressValid(params)
                && params.beginTime != null
                && params.beginTime.length() > 5
                && params.endTime != null
                && params.endTime.length() > 5;
        return valid;
    }

    @Override
    public String getContentForDebug() {
        final String ENDL = StringUtils.getNewLine();
        StringBuilder sb = new StringBuilder();

        //TODO Hide sensitive data
        sb.append(ENDL);
        sb.append(Maevent.LOG_TAG).append(ENDL);
        sb.append(MaeventParams.LOG_TAG).append(":").append(ENDL);
        sb.append(params.name).append(ENDL);
        sb.append(params.place).append(" (")
                .append(params.addressStreet).append(", ")
                .append(params.addressPostCode).append(")").append(ENDL);
        sb.append(params.beginTime).append(" - ")
                .append(params.endTime).append(" rsvp ")
                .append(params.rsvp ? "yes" : "not required").append(ENDL);

        return sb.toString();
    }

    public static boolean isNameValid(String name) {
        return name != null && name.length() > 5;
    }

    private static boolean isPlaceValid(MaeventParams params) {
        return params.place != null && params.place.length() > 2;
    }

    private static boolean isAddressValid(MaeventParams params) {
        return isAddressStreetValid(params) && isAddressPostCodeValid(params);
    }

    private static boolean isAddressStreetValid(MaeventParams params) {
        return params.addressStreet != null && params.addressPostCode.length() > 6;
    }

    private static boolean isAddressPostCodeValid(MaeventParams params) {
        return params.addressPostCode != null && params.addressPostCode.length() > 6;
    }
}

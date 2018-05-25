package com.devmarcul.maevent.data;

import com.devmarcul.maevent.content_provider.hardcoded.MaeventParamsBuilder;
import com.devmarcul.maevent.utils.Utils;

public class Maevent implements DataValidator {

    private static String LOG_TAG = "Maevent";

    public MaeventParams params;

    public MaeventParams getParams() {
        return params;
    }

    public void setParams(MaeventParams params) {
        this.params = params;
    }

    public void setName(String name) {
        params.name = name;
    }

    @Override
    public boolean isValid() {
        return areParamsValid(params);
    }

    public static boolean areParamsValid(MaeventParams params) {
        boolean valid = isNameValid(params.name)
                && isPlaceValid(params)
                && isAddressValid(params)
                && params.startTime != null
                && params.startTime.length() > 5
                && params.stopTime != null
                && params.stopTime.length() > 5;
        return valid;
    }

    @Override
    public String getContentForDebug() {
        final String ENDL = Utils.getNewLine();
        StringBuilder sb = new StringBuilder();

        //TODO Hide sensitive data
        sb.append(ENDL);
        sb.append(Maevent.LOG_TAG).append(ENDL);
        sb.append(MaeventParams.LOG_TAG).append(":").append(ENDL);
        sb.append(params.name).append(ENDL);
        sb.append(params.place).append(" (")
                .append(params.addressStreet).append(", ")
                .append(params.addressPostCode).append(")").append(ENDL);
        sb.append(params.startTime).append(" - ")
                .append(params.stopTime).append(" rsvp ")
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

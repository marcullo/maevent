package com.devmarcul.maevent.data;

import com.devmarcul.maevent.content_provider.hardcoded.MaeventParamsBuilder;
import com.devmarcul.maevent.utils.Utils;

public class Maevent implements ContentUpdater, DataValidator {

    private static String LOG_TAG = "Maevent";

    public MaeventParams params;

    public MaeventParams getParams() {
        return params;
    }

    public void setName(String name) {
        params.name = name;
    }

    @Override
    public void updateContent() {
        //TODO Replace dummy initialization with data base query
        params = MaeventParamsBuilder.build();
        checkValidity();
    }

    @Override
    public boolean checkValidity() {
        boolean valid = params.name != null
                && params.name.length() > 5
                && isPlaceValid()
                && isAddressValid()
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

    private boolean isPlaceValid() {
        return params.place != null && params.place.length() > 2;
    }

    private boolean isAddressValid() {
        return isAddressStreetValid() && isAddressPostCodeValid();
    }

    private boolean isAddressStreetValid() {
        return params.addressStreet != null && params.addressPostCode.length() > 6;
    }

    private boolean isAddressPostCodeValid() {
        return params.addressPostCode != null && params.addressPostCode.length() == 6;
    }
}

package com.devmarcul.maevent.data;

import android.provider.CalendarContract;

import com.devmarcul.maevent.utils.Utils;

import java.util.Calendar;

public class MaeventCalendar implements DataValidator  {

    private MaeventCalendarParams params;

    public static String LOG_TAG = "MaeventCalendar";

    public MaeventCalendar() {
        this.params = new MaeventCalendarParams();
    }

    public MaeventCalendar(MaeventCalendarParams params) {
        this.params = params;
    }

    public MaeventCalendarParams getParams() {
        return params;
    }

    public void setParams(MaeventCalendarParams params) {
        this.params = params;
    }

    public void update(MaeventParams params) {
        Calendar beginTimeCal = Utils.getCalendarFromString(params.beginTime, MaeventParams.TIME_FORMAT);
        Calendar endTimeCal = Utils.getCalendarFromString(params.endTime, MaeventParams.TIME_FORMAT);
        assert beginTimeCal != null;
        assert endTimeCal != null;

        StringBuilder sb = new StringBuilder();
        sb.append(params.place).append(", ").append(params.addressStreet).append(params.addressPostCode);

        this.params.beginTime = beginTimeCal.getTimeInMillis();
        this.params.endTime = endTimeCal.getTimeInMillis();
        this.params.title = params.name;
        this.params.description = "";
        this.params.location = sb.toString();
        this.params.availability = CalendarContract.Events.AVAILABILITY_BUSY;
    }

    public void update(MaeventParams params, String hostName, String hostPhone) {
        update(params);

        StringBuilder sb = new StringBuilder();
        sb.append(hostPhone).append(" ").append(hostName).append(" (host)");
        this.params.description = sb.toString();
    }

    @Override
    public boolean isValid() {
        //TODO Calendar verification
        return true;
    }

    @Override
    public String getContentForDebug() {
        final String ENDL = Utils.getNewLine();
        StringBuilder sb = new StringBuilder();

        sb.append(ENDL);
        sb.append(MaeventCalendar.LOG_TAG).append(ENDL);
        sb.append(MaeventCalendarParams.LOG_TAG).append(ENDL);
        sb.append(params.title).append(ENDL);
        sb.append("Timestamps start: ").append(String.valueOf(params.beginTime))
            .append(", stop: ").append(String.valueOf(params.endTime)).append(ENDL);
        sb.append(params.location).append(ENDL);
        sb.append("Availability: ")
                .append(params.availability == CalendarContract.Events.AVAILABILITY_BUSY ?
                        ("busy") : ("not busy")).append(ENDL);

        return sb.toString();
    }
}

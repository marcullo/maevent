package com.devmarcul.maevent.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static String getNewLine() {
        return java.lang.System.getProperty("line.separator");
    }

    public static String getKeyValueString(String key, String value) {
        return key + ": " + value;
    }

    public static String getStringFromDuration(Date startDate, Date endDate) {
        String startDateString  = DateFormat.format("dd", startDate).toString();
        String endDateString  = DateFormat.format("dd", endDate).toString();

        StringBuilder sb = new StringBuilder();

        if (startDateString.contentEquals(endDateString)) {
            String day = DateFormat.format("dd.MM", startDate).toString();
            String hourFormat = "hh:mm";
            String startHour;
            String endHour;

            String startAmpm = DateFormat.format("a", startDate).toString();
            String endAmpm = DateFormat.format("a", endDate).toString();
            if (startAmpm.contentEquals(endAmpm)) {
                startHour = DateFormat.format(hourFormat, startDate).toString();
                endHour = DateFormat.format(hourFormat + " a", endDate).toString();
            }
            else {
                hourFormat += " a";
                startHour = DateFormat.format(hourFormat, startDate).toString();
                endHour = DateFormat.format(hourFormat, endDate).toString();
            }
            sb.append(startHour).append(" - ").append(endHour).append(" ").append(day);
        }
        else {
            String start = DateFormat.format("hh:mm a dd.MM", startDate).toString();
            String end = DateFormat.format("hh:mm a dd.MM", endDate).toString();
            sb.append(start).append(" - ").append(end);
        }
        return sb.toString();
    }

    public static String getStringFromDate(Date date) {
        return DateFormat.format("hh:mm a dd:MM", date).toString();
    }
}

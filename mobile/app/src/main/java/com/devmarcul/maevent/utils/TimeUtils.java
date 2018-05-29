package com.devmarcul.maevent.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static Calendar getCalendarFromString(String timeStr, String format) {
        Calendar time = Calendar.getInstance();
        Date date;

        try {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            date = fmt.parse(timeStr);
        } catch (ParseException e) {
            return null;
        }

        time.setTime(date);
        return time;
    }

    public static String getStringFromCalendar(Calendar calendar, String format) {
        return DateFormat.format(format, calendar).toString();
    }

    public static String convertTimeStringToOtherFormat(String time, String oldFormat, String newFormat) {
        Calendar calendar = getCalendarFromString(time, oldFormat);
        time = getStringFromCalendar(calendar, newFormat);
        return time;
    }

    public static String getStringFromCalendarDuration(Calendar startDate, Calendar endDate, String singleFormat) {
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
            String start = DateFormat.format(singleFormat, startDate).toString();
            String end = DateFormat.format(singleFormat, endDate).toString();
            sb.append(start).append(" - ").append(end);
        }
        return sb.toString();
    }

    public static String getTimeStringFromStringDuration(String start, String end, String initialFormat) {
        Calendar startCal = getCalendarFromString(start, initialFormat);
        Calendar endCal = getCalendarFromString(end, initialFormat);
        return getStringFromCalendarDuration(startCal, endCal, initialFormat);
    }

    public static String getStringDurationFromCalendarDuration(Calendar startDate, Calendar endDate) {
        long startMillis = startDate.getTimeInMillis();
        long endMillis = endDate.getTimeInMillis();
        long duration;
        String prefix;

        boolean positive = startMillis <= endMillis;
        if (positive) {
            duration = endMillis - startMillis;
            prefix = "";
        }
        else {
            duration = startMillis - endMillis;
            prefix = "-";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        if (minutes < 60) {
            sb.append(minutes).append(" m.");
        }
        else if (minutes < 24 * 60) {
            long hours = TimeUnit.MINUTES.toHours(minutes);
            sb.append(hours).append(" h.");
        }
        else {
            long days = TimeUnit.MINUTES.toDays(minutes);
            sb.append(days).append(" d.");
        }

        return sb.toString();
    }
}

package com.devmarcul.maevent.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static Calendar getCalendarFromString(String timeStr, String format) {
        Calendar time = Calendar.getInstance();
        Date date;

        try {
            String fmt = format.replace("T", "");
            timeStr = timeStr.replace("T", "");
            SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.US);
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            time.set(2000, 12, 31);
            return time;
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

    public static String[] splitCalendarDuration(String duration, String initialFormat, String newFormat) {
        if (duration == null) {
            return null;
        }

        String[] calStrings = duration.split("-");
        if (calStrings.length != 2) {
            return null;
        }

        String beginStr = calStrings[0].trim();
        final String endStr = calStrings[1].trim();

        Calendar begin = getCalendarFromString(beginStr, initialFormat);
        if (begin != null) {
            // separated start and end

            if (initialFormat.contentEquals(newFormat)) {
                calStrings[0] = beginStr;
                calStrings[1] = endStr;
                return calStrings;
            }

            Calendar end = getCalendarFromString(endStr, initialFormat);
            if (end == null) {
                return null;
            }

            calStrings[0] = DateFormat.format(newFormat, begin).toString();
            calStrings[1] = DateFormat.format(newFormat, end).toString();
        }
        else {
            // one-day view

            String[] segments = endStr.split(" ");
            String endTime = segments[0];
            String amPm = segments[1];
            String endDate = segments[2];

            StringBuilder builder = new StringBuilder();
            builder.append(beginStr).append(" ").append(amPm).append(" ").append(endDate);
            calStrings[0] = builder.toString();
        }

        return calStrings;
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

package com.devmarcul.maevent.business_logic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventCalendar;
import com.devmarcul.maevent.data.MaeventCalendarParams;
import com.devmarcul.maevent.data.MaeventParams;

public class MaeventSteward {

    public static String getHostName(int hostId) {
        MaeventUserManager mum = new MaeventUserManager();
        String phone = mum.getUserName(hostId);
        return phone;
    }

    public static String getHostPhone(int hostId) {
        MaeventUserManager mum = new MaeventUserManager();
        String phone = mum.getUserPhone(hostId);
        return phone;
    }

    public static void callHost(String phoneNumber, Activity activity) {
        Uri phoneUri = Uri.fromParts("tel", phoneNumber, null);
        Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
        activity.startActivity(intent);
    }

    public static void saveEventToCalendar(Maevent event, int hostId, Activity activity) {
        MaeventCalendarParams params = prepareBaseParams(event);
        Intent intent = prepareBaseIntent(params);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, params.description);
        activity.startActivity(intent);
    }

    public static void openEventLocation(Maevent event, Activity activity) {
        StringBuilder sb = new StringBuilder();
        MaeventParams params = event.getParams();

        sb.append(params.place)
                .append(", ").append(params.addressStreet)
                .append(" ").append(params.addressPostCode);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
            .appendPath("0,0")
            .appendQueryParameter("q", sb.toString());

        Uri gmmIntentUri = builder.build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        activity.startActivity(mapIntent);
    }

    private static MaeventCalendarParams prepareBaseParams(Maevent event) {
        MaeventParams eventParams = event.getParams();
        int hostId = event.getHostId();
        String hostName = getHostName(hostId);
        String hostPhone = getHostPhone(hostId);

        MaeventCalendar cal = new MaeventCalendar();
        cal.update(eventParams, hostName, hostPhone);
        return cal.getParams();
    }

    private static Intent prepareBaseIntent(MaeventCalendarParams params) {
        return new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, params.beginTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, params.endTime)
                .putExtra(CalendarContract.Events.TITLE, params.title)
                .putExtra(CalendarContract.Events.DESCRIPTION, params.description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, params.location)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
    }
}

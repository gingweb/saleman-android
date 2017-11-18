package com.akkaratanapat.altear.vrp_onboard.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by altear on 8/10/2017.
 */

public class CalendarHandler {

    public static Calendar getCalendarJobInfo(String pickupDateTime){
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", new Locale("th", "TH"));
        try {
            c1.setTime(sdf.parse(String.valueOf(pickupDateTime)));
            c1.add(Calendar.HOUR, 7);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c1;
    }

    public static String getPickupDate(Date date){
        String tempMouth = "00" + (date.getMonth() + 1);
        String tempDay = "00" + date.getDate();
        return  tempDay.substring(tempDay.length() - 2) + "/" +
                tempMouth.substring(tempMouth.length() - 2) + "/" + (date.getYear() + 1900);
    }

    public static String getPickupTime(Date date){
        String tempMinute = "00" + date.getMinutes();
        String tempHour = "00" + date.getHours();
        return tempHour.substring(tempHour.length() - 2) + ":"
                + tempMinute.substring(tempMinute.length() - 2);
    }
}

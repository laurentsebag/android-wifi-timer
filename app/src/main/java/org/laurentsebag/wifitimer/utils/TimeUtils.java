package org.laurentsebag.wifitimer.utils;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    public static String getDisplayHour(Calendar calendar, boolean is24HourFormat) {
        if (is24HourFormat) {
            return String.format(Locale.ENGLISH, "%02d", calendar.get(Calendar.HOUR_OF_DAY));
        }
        return String.valueOf(calendar.get(Calendar.HOUR));
    }

    public static String getDisplayMinute(Calendar calendar) {
        return String.format(Locale.ENGLISH, "%02d", calendar.get(Calendar.MINUTE));
    }
}

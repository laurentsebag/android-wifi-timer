package org.laurentsebag.wifitimer.utils;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    private static final String TWO_DIGITS_FORMAT = "%02d";

    public static String getDisplayHour(Calendar calendar, boolean is24HourFormat) {
        if (is24HourFormat) {
            return String.format(Locale.ENGLISH, TWO_DIGITS_FORMAT, calendar.get(Calendar.HOUR_OF_DAY));
        }
        return String.valueOf(calendar.get(Calendar.HOUR));
    }

    public static String getDisplayMinute(Calendar calendar) {
        return String.format(Locale.ENGLISH, TWO_DIGITS_FORMAT, calendar.get(Calendar.MINUTE));
    }
}

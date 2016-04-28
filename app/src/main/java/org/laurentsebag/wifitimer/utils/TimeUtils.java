/*-
 *  Copyright (C) 2016 Laurent Sebag
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.laurentsebag.wifitimer.utils;

import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {

    public static final String TWO_DIGITS_FORMAT = "%02d";

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

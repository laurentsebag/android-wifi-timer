/*-
 *  Copyright (C) 2011 Laurent Sebag
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

package org.laurentsebag.wifitimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppConfig {

    public static final String APP_PREFERENCES = "wifitimer";

    public static final String MODE_ON_WIFI_ACTIVATION = "on_wifi_activation";
    public static final String MODE_ON_WIFI_DEACTIVATION = "on_wifi_deactivation";
    public static final String PREFERENCE_KEY_WIFI_CHANGE_TIME = "wifi_state_changed_time";
    public static final String PREFERENCE_KEY_TIMER_USAGE = "timer_usage";
    public static final String PREFERENCE_KEY_APP_ENABLED = "wifi_timer_enabled";

    public static final String SNOOZE_ALARM_ACTION = "wifitimer.intent.SNOOZE_ALARM";
    public static final String CANCEL_ALARM_ACTION = "wifitimer.intent.CANCEL_ALARM";
    public static final String WIFI_TOGGLE_ACTION = "wifitimer.intent.WIFI_TOGGLE";

    public static String getWifiTimerUsage(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREFERENCE_KEY_TIMER_USAGE, MODE_ON_WIFI_DEACTIVATION);
    }

    public static boolean isAppEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREFERENCE_KEY_APP_ENABLED, true);
    }
}

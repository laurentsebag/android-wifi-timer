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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TrackerUtils {

    private static final String TRACK_ACTION_CLICK = "click";
    private static final String TRACK_ACTION_TIMER_CANCEL = "timer_cancel";
    public static final String TRACK_ACTION_OPEN_EXTERNAL_LINK = "open_external_link";
    public static final String TRACK_ACTION_OPEN_EXTERNAL_APP = "open_external_app";

    public static final String TRACK_CATEGORY_TIMER = "timer";
    public static final String TRACK_CATEGORY_NOTIFICATION = "notification";
    public static final String TRACK_CATEGORY_SNACK_BAR = "snack_bar";
    public static final String TRACK_CATEGORY_PREFERENCE = "preference";
    private static final String TRACK_CATEGORY_SYSTEM_EVENTS = "system_events";

    private static final String TRACK_VARIABLE_WIFI_DETECTION = "wifi_detection";

    public static final String TRACK_LABEL_SNOOZE = "snooze";
    public static final String TRACK_LABEL_CANCEL = "cancel";
    public static final String TRACK_LABEL_TOGGLE = "toggle_wifi";
    public static final String TRACK_LABEL_TIMER_SET = "set";
    public static final String TRACK_LABEL_TIMER_CANCEL = "cancel";
    public static final String TRACK_LABEL_TIMER_UNDO = "undo";
    public static final String TRACK_LABEL_TIMER_INCREASE_HOUR = "increase_hour";
    public static final String TRACK_LABEL_TIMER_DECREASE_HOUR = "decrease_hour";
    public static final String TRACK_LABEL_TIMER_INCREASE_MINUTE = "increase_minute";
    public static final String TRACK_LABEL_TIMER_DECREASE_MINUTE = "decrease_minute";
    public static final String TRACK_LABEL_TIMER_SWITCH_AM_PM = "switch_am_pm";
    public static final String TRACK_LABEL_TIMER_CANCEL_APP = "app";
    public static final String TRACK_LABEL_TIMER_CANCEL_EXTERNAL = "external";
    public static final String TRACK_LABEL_ENABLE_APP = "enable_app";

    public static void trackScreen(Tracker tracker, String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void trackClick(Tracker tracker, String category, String label) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(TRACK_ACTION_CLICK).setLabel(label).build());
    }

    public static void trackClick(Tracker tracker, String category, String label, long value) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(TRACK_ACTION_CLICK).setLabel(label).setValue(value).build());
    }

    public static void trackPreference(Tracker tracker, String preferenceName, String preferenceValue) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(TRACK_CATEGORY_PREFERENCE).setAction(preferenceName).setLabel(preferenceValue).build());
    }

    public static void trackWifiDialogTiming(Tracker tracker, String wifiTimerMode, long durationInMillis) {
        tracker.send(new HitBuilders.TimingBuilder().setCategory(TRACK_CATEGORY_SYSTEM_EVENTS).setVariable(TRACK_VARIABLE_WIFI_DETECTION).setLabel(wifiTimerMode).setValue(durationInMillis).build());
    }

    public static void trackTimerEvent(Tracker tracker, String label) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(TRACK_CATEGORY_TIMER).setAction(TRACK_ACTION_TIMER_CANCEL).setLabel(label).build());
    }

    public static void trackExternalLink(Tracker tracker, String category, String action, String url) {
        tracker.send(new HitBuilders.EventBuilder(category, action).setLabel(url).build());
    }
}

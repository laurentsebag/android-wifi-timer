package org.laurentsebag.wifitimer.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TrackerUtils extends TrackerUtilsConstants  {

    private static final String TRACK_ACTION_CLICK = "click";
    public static final String TRACK_ACTION_TIMER_CANCEL = "timer_cancel";

    public static final String TRACK_CATEGORY_TIMER = "timer";
    public static final String TRACK_CATEGORY_NOTIFICATION = "notification";
    private static final String TRACK_CATEGORY_PREFERENCE = "preference";
    private static final String TRACK_CATEGORY_SYSTEM_EVENTS = "system_events";

    private static final String TRACK_VARIABLE_WIFI_DETECTION = "wifi_detection";

    public static final String TRACK_LABEL_TIMER_CANCEL_APP = "app";
    public static final String TRACK_LABEL_TIMER_CANCEL_EXTERNAL = "external";

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
}

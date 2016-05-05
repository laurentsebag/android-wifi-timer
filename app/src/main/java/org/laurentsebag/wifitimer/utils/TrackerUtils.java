package org.laurentsebag.wifitimer.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TrackerUtils {

    private static final String ACTION_CLICK = "click";

    public static final String TRACK_CATEGORY_TIMER = "timer";
    public static final String TRACK_CATEGORY_NOTIFICATION = "notification";
    private static final String TRACK_CATEGORY_PREFERENCE = "preference";

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

    // TODO see if we can measure how long it takes for dialog to appear after wifi changed
    // TODO track percentage of timers ending naturally, or cancelled from within or outside app
    // TODO track amount of tutorial pages people read before skipping
    // TODO create build variant for pre-v9 that has tracking disabled

    public static void trackScreen(Tracker tracker, String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void trackClick(Tracker tracker, String category, String label) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(ACTION_CLICK).setLabel(label).build());
    }

    public static void trackClick(Tracker tracker, String category, String label, long value) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(ACTION_CLICK).setLabel(label).setValue(value).build());
    }

    public static void trackPreference(Tracker tracker, String preferenceName, String preferenceValue) {
        tracker.send(new HitBuilders.EventBuilder().setCategory(TRACK_CATEGORY_PREFERENCE).setAction(preferenceName).setLabel(preferenceValue).build());
    }
}

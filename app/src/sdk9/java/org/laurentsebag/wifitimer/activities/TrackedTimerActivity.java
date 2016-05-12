package org.laurentsebag.wifitimer.activities;

import android.content.Context;
import android.content.SharedPreferences;

import org.laurentsebag.wifitimer.AppConfig;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public abstract class TrackedTimerActivity extends TrackedActivity {
    private static final int TIME_INVALID = -1;

    protected void trackTimerCancelled() {
        TrackerUtils.trackTimerEvent(tracker, TrackerUtils.TRACK_LABEL_TIMER_CANCEL_APP);
    }

    protected void trackTimeToDisplayDialog(String wifiTimerUsage) {
        SharedPreferences preferences = getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
        long wifiChangeTime = preferences.getLong(AppConfig.PREFERENCE_KEY_WIFI_CHANGE_TIME, TIME_INVALID);
        if (wifiChangeTime > TIME_INVALID) {
            long duration = System.currentTimeMillis() - wifiChangeTime;
            TrackerUtils.trackWifiDialogTiming(tracker, wifiTimerUsage, duration);
            preferences.edit().remove(AppConfig.PREFERENCE_KEY_WIFI_CHANGE_TIME).apply();
        }
    }

    protected void trackAction(String label, long value) {
        TrackerUtils.trackClick(tracker, TrackerUtils.TRACK_CATEGORY_TIMER, label, value);
    }

    protected void trackAction(String label) {
        TrackerUtils.trackClick(tracker, TrackerUtils.TRACK_CATEGORY_TIMER, label);
    }
}

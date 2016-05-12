package org.laurentsebag.wifitimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.AppConfig;
import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.activities.TimerActivity;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public abstract class TrackedWifiStateReceiver extends BroadcastReceiver {

    private Tracker tracker;

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiTimerApplication application = (WifiTimerApplication) context.getApplicationContext();
        tracker = application.getDefaultTracker();
    }

    protected void trackTimerCancel() {
        TrackerUtils.trackTimerEvent(tracker, TrackerUtils.TRACK_LABEL_TIMER_CANCEL_EXTERNAL);
    }

    protected void showWifiDialog(Context context, SharedPreferences.Editor editor) {
        editor.putLong(AppConfig.PREFERENCE_KEY_WIFI_CHANGE_TIME, System.currentTimeMillis());
        editor.apply();
    }

    protected void savePreference(SharedPreferences.Editor editor) {
        editor.apply();
    }
}

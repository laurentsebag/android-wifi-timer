package org.laurentsebag.wifitimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public abstract class TrackedNotifActionReceiver extends BroadcastReceiver {

    private Tracker tracker;

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiTimerApplication application = ((WifiTimerApplication) context.getApplicationContext());
        tracker = application.getDefaultTracker();
    }

    protected void trackNotificationClick(String label) {
        TrackerUtils.trackClick(tracker, TrackerUtils.TRACK_CATEGORY_NOTIFICATION, label);
    }

    protected void trackTimerCancel() {
        TrackerUtils.trackTimerEvent(tracker, TrackerUtils.TRACK_LABEL_TIMER_CANCEL_APP);
    }
}

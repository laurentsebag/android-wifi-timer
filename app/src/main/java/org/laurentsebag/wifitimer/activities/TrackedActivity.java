package org.laurentsebag.wifitimer.activities;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public class TrackedActivity extends Activity {
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiTimerApplication application = (WifiTimerApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackerUtils.trackScreen(tracker, getClass().getSimpleName());
    }

    protected void trackClick(String category, String label) {
        TrackerUtils.trackClick(tracker, category, label);
    }

    protected void trackClick(String category, String label, long value) {
        TrackerUtils.trackClick(tracker, category, label, value);
    }
}

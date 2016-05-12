package org.laurentsebag.wifitimer.activities;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public abstract class TrackedActivity extends Activity {
    protected Tracker tracker;

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
}

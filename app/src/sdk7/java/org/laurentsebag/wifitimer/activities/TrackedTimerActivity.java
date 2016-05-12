package org.laurentsebag.wifitimer.activities;

import android.app.Activity;

public abstract class TrackedTimerActivity extends Activity {

    protected void trackTimerCancelled() {
    }

    protected void trackTimeToDisplayDialog(String wifiTimerUsage) {
    }

    protected void trackAction(String label, long value) {
    }

    protected void trackAction(String label) {
    }
}

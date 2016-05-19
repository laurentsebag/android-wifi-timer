package org.laurentsebag.wifitimer.activities;

import android.content.SharedPreferences;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public class TrackedAppCompatActivity extends AppCompatActivity {
    protected static final int TRANSITION_DURATION = 250;
    protected Tracker tracker;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiTimerApplication application = (WifiTimerApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupToolbarColor() {
        String appEnabledKey = getString(R.string.preference_wifi_timer_enabled_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean appEnabled = sharedPreferences.getBoolean(appEnabledKey, true);
        transitionToolbarColor(appEnabled, 0);
    }

    protected void transitionToolbarColor(boolean appEnabled) {
        transitionToolbarColor(appEnabled, TRANSITION_DURATION);
    }

    private void transitionToolbarColor(boolean appEnabled, int transitionDuration) {
        toolbar.setBackgroundResource(appEnabled ? R.drawable.toolbar_background_enabling : R.drawable.toolbar_background_disabling);
        TransitionDrawable drawable = (TransitionDrawable) toolbar.getBackground();
        drawable.startTransition(transitionDuration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackerUtils.trackScreen(tracker, getClass().getSimpleName());
        setupToolbarColor();
    }
}

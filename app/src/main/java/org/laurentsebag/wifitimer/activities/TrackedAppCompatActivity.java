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

public abstract class TrackedAppCompatActivity extends AppCompatActivity {
    private static final int TRANSITION_DURATION = 250;
    protected Tracker tracker;
    protected String appEnabledKey;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiTimerApplication application = (WifiTimerApplication) getApplication();
        tracker = application.getDefaultTracker();
        appEnabledKey = getString(R.string.preference_wifi_timer_enabled_key);
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void transitionToolbarColor(boolean appEnabled) {
        transitionToolbarColor(appEnabled, TRANSITION_DURATION);
    }

    private void transitionToolbarColor(boolean appEnabled, int transitionDuration) {
        if (transitionDuration > 0) {
            toolbar.setBackgroundResource(appEnabled ? R.drawable.toolbar_background_enabling : R.drawable.toolbar_background_disabling);
            TransitionDrawable drawable = (TransitionDrawable) toolbar.getBackground();
            drawable.startTransition(transitionDuration);
        } else {
            toolbar.setBackgroundResource(appEnabled ? R.color.primary : R.color.toolbar_app_disabled);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackerUtils.trackScreen(tracker, getClass().getSimpleName());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean appEnabled = sharedPreferences.getBoolean(appEnabledKey, true);
        onResume(appEnabled);
    }

    protected void onResume(boolean appEnabled) {
        transitionToolbarColor(appEnabled, 0);
    }
}

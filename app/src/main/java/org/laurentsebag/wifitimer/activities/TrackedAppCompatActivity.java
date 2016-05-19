package org.laurentsebag.wifitimer.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public class TrackedAppCompatActivity extends AppCompatActivity {
    protected Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiTimerApplication application = (WifiTimerApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void setupToolbarColor() {
        ActionBar toolbar = getSupportActionBar();
        String appEnabledKey = getString(R.string.preference_wifi_timer_enabled_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean appEnabled = sharedPreferences.getBoolean(appEnabledKey, true);

        if (toolbar != null) {
            toolbar.setBackgroundDrawable(ContextCompat.getDrawable(this, appEnabled ? R.color.primary : R.color.toolbar_app_disabled));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackerUtils.trackScreen(tracker, getClass().getSimpleName());
        setupToolbarColor();
    }
}

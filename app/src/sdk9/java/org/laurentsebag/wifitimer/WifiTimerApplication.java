package org.laurentsebag.wifitimer;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class WifiTimerApplication extends Application {
    private static final String TAG = WifiTimerApplication.class.getSimpleName();
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        setupAnalytics();
    }

    private void setupAnalytics() {
        String preferenceKeyAnalytics = getString(R.string.preference_share_analytics_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean shareAnalytics = sharedPreferences.getBoolean(preferenceKeyAnalytics, true);
        Log.d(TAG, "Analytics sharing: " + shareAnalytics);
        GoogleAnalytics.getInstance(this).setAppOptOut(!shareAnalytics);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker.enableExceptionReporting(true);
            mTracker.setAnonymizeIp(true);
        }
        return mTracker;
    }
}

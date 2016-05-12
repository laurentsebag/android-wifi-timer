package org.laurentsebag.wifitimer.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public abstract class TrackedSettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = TrackedSettingsFragment.class.getSimpleName();
    private static final String PREFERENCE_NOT_SET = "not-set";

    private Tracker tracker;

    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String preferenceKeyAnalytics = getString(R.string.preference_share_analytics_key);

            if (key.equals(preferenceKeyAnalytics)) {
                boolean shareAnalytics = sharedPreferences.getBoolean(preferenceKeyAnalytics, true);

                if (!shareAnalytics) {
                    TrackerUtils.trackPreference(tracker, key, String.valueOf(false));
                }

                Log.d(TAG, "Analytics sharing: " + shareAnalytics);
                GoogleAnalytics.getInstance(getContext()).setAppOptOut(!shareAnalytics);

                if (shareAnalytics) {
                    TrackerUtils.trackPreference(tracker, key, String.valueOf(true));
                }
            } else {
                String value = sharedPreferences.getString(key, PREFERENCE_NOT_SET);
                TrackerUtils.trackPreference(tracker, key, value);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiTimerApplication application = (WifiTimerApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        userPrefs.registerOnSharedPreferenceChangeListener(preferenceListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        userPrefs.unregisterOnSharedPreferenceChangeListener(preferenceListener);
    }
}

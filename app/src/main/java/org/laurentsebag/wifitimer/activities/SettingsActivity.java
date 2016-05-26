/*-
 *  Copyright (C) 2011 Laurent Sebag
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.laurentsebag.wifitimer.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.fragments.SettingsFragment;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public class SettingsActivity extends TrackedAppCompatActivity {
    private static final String TAG = SettingsFragment.class.getSimpleName();
    private static final String PREFERENCE_NOT_SET = "not-set";

    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String analyticsKey = getString(R.string.preference_share_analytics_key);

            if (key.equals(analyticsKey)) {
                boolean shareAnalytics = sharedPreferences.getBoolean(analyticsKey, true);

                if (!shareAnalytics) {
                    TrackerUtils.trackPreference(tracker, key, String.valueOf(false));
                }

                Log.d(TAG, "Analytics sharing: " + shareAnalytics);
                GoogleAnalytics.getInstance(getApplicationContext()).setAppOptOut(!shareAnalytics);

                if (shareAnalytics) {
                    TrackerUtils.trackPreference(tracker, key, String.valueOf(true));
                }
            } else if (key.equals(appEnabledKey)) {
                boolean appEnabled = sharedPreferences.getBoolean(appEnabledKey, true);
                TrackerUtils.trackPreference(tracker, key, String.valueOf(appEnabled));
                transitionToolbarColor(appEnabled);
            } else {
                String value = sharedPreferences.getString(key, PREFERENCE_NOT_SET);
                TrackerUtils.trackPreference(tracker, key, value);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        userPrefs.registerOnSharedPreferenceChangeListener(preferenceListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        userPrefs.unregisterOnSharedPreferenceChangeListener(preferenceListener);
    }
}

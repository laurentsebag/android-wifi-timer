/*-
 *  Copyright (C) 2016 Laurent Sebag
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
            mTracker.enableExceptionReporting(!BuildConfig.DEBUG);
            mTracker.setAnonymizeIp(true);
        }
        return mTracker;
    }
}

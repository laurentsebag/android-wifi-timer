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

public abstract class TrackedToolbarActivity extends AppCompatActivity {
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

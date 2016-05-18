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

package org.laurentsebag.wifitimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;

import org.laurentsebag.wifitimer.AppConfig;
import org.laurentsebag.wifitimer.Timer;
import org.laurentsebag.wifitimer.WifiTimerApplication;
import org.laurentsebag.wifitimer.activities.TimerActivity;
import org.laurentsebag.wifitimer.utils.RadioUtils;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public class WifiStateReceiver extends BroadcastReceiver {

    private static final String TAG = WifiStateReceiver.class.getSimpleName();
    private static final String CANCELED_BY_AIRPLANE_MODE = "canceled_by_airplane_mode";
    private static final String TURNED_OFF_BY_AIRPLANE_MODE = "turned_off_by_airplane_mode";
    private static final long IGNORE_BOOT_THRESHOLD = 2 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent data) {
        if (SystemClock.elapsedRealtime() < IGNORE_BOOT_THRESHOLD) {
            return;
        }

        if (!AppConfig.isAppEnabled(context)) {
            return;
        }

        WifiTimerApplication application = (WifiTimerApplication) context.getApplicationContext();
        Tracker tracker = application.getDefaultTracker();

        String action = data.getAction();
        SharedPreferences preferences = context.getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {

            String timerUsage = AppConfig.getWifiTimerUsage(context);

            int wifiState = data.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            Log.d(TAG, "Wifi state changed: " + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLING:
                    if (timerUsage.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
                        boolean turnOffByAirplaneMode = preferences.getBoolean(TURNED_OFF_BY_AIRPLANE_MODE, false);
                        if (turnOffByAirplaneMode) {
                            editor.putBoolean(TURNED_OFF_BY_AIRPLANE_MODE, false);
                            editor.apply();
                        }
                        // If the wifi state has changed but not because of a airplane mode change,
                        // display the wifi timer dialog.
                        if (!turnOffByAirplaneMode) {
                            showWifiDialog(context, editor);
                        }
                    } else {
                        Timer timer = new Timer(context);
                        timer.cancel();
                        TrackerUtils.trackTimerEvent(tracker, TrackerUtils.TRACK_LABEL_TIMER_CANCEL_EXTERNAL);
                    }
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    if (timerUsage.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
                        Timer timer = new Timer(context);
                        timer.cancel();
                        TrackerUtils.trackTimerEvent(tracker, TrackerUtils.TRACK_LABEL_TIMER_CANCEL_EXTERNAL);
                    } else {
                        showWifiDialog(context, editor);
                    }
                    break;
            }
        } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
            boolean airplaneModeOn = data.getBooleanExtra("state", false);
            Log.d(TAG, "Wifi state airplane mode changed: " + airplaneModeOn);

            if (airplaneModeOn) {
                editor.putBoolean(TURNED_OFF_BY_AIRPLANE_MODE, true);

                // If user sets airplane mode, cancel current timer
                Timer timer = new Timer(context);
                if (timer.isSet()) {
                    timer.cancel();
                    editor.putBoolean(CANCELED_BY_AIRPLANE_MODE, true);
                    TrackerUtils.trackTimerEvent(tracker, TrackerUtils.TRACK_LABEL_TIMER_CANCEL_EXTERNAL);
                }
                editor.apply();
            } else {
                // If user had a set timer before turning on airplane mode,
                // when airplane mode is turned off, restore wifi.
                if (preferences.getBoolean(CANCELED_BY_AIRPLANE_MODE, false)) {
                    editor.putBoolean(CANCELED_BY_AIRPLANE_MODE, false);
                    editor.apply();
                    if (AppConfig.getWifiTimerUsage(context).equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
                        RadioUtils.setWifiOn(context);
                    }
                }
            }
        }
    }

    private void showWifiDialog(Context context, Editor editor) {
        Log.d(TAG, "showWifiDialog");
        editor.putLong(AppConfig.PREFERENCE_KEY_WIFI_CHANGE_TIME, System.currentTimeMillis());
        editor.apply();

        Intent intent = new Intent(context, TimerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

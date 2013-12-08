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

package org.laurentsebag.wifitimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiStateReceiver extends BroadcastReceiver {
	
	private static final String CANCELED_BY_AIRPLANE_MODE = "canceled_by_airplane_mode";
	private static final String TURNED_OFF_BY_AIRPLANE_MODE = "turned_off_by_airplane_mode";
	
	@Override
	public void onReceive(Context context, Intent data) {
		String action = data.getAction();
		SharedPreferences preferences = context.getSharedPreferences(
				AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
		Editor edit = preferences.edit();

		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
			
			String timerUsage = AppConfig.getWifiTimerUsage(context);

			int wifiState = data.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
			switch(wifiState) {
            case WifiManager.WIFI_STATE_DISABLED:
				if (timerUsage.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
                    boolean turnOffByAirplaneMode = preferences.getBoolean(TURNED_OFF_BY_AIRPLANE_MODE, false);
                    if (turnOffByAirplaneMode) {
                        edit.putBoolean(TURNED_OFF_BY_AIRPLANE_MODE, false);
                        edit.commit();
                    }
					// If the wifi state has changed but not because of a airplane mode change,
					// display the wifi timer dialog.
					if (!turnOffByAirplaneMode) {
						Intent intent = new Intent(context, TimerActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				} else {
					Timer timer = new Timer(context);
					timer.cancel();
				}
				break;
            case WifiManager.WIFI_STATE_ENABLED:
				if (timerUsage.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
					Timer timer = new Timer(context);
					timer.cancel();
				} else {
					Intent intent = new Intent(context, TimerActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
				break;
			}
		}
		else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
			boolean airplaneModeOn = data.getBooleanExtra("state", false);
					
			if (airplaneModeOn) {
				edit.putBoolean(TURNED_OFF_BY_AIRPLANE_MODE, true);
				
				// If user sets airplane mode, cancel current timer
				Timer t = new Timer(context);
				if (t.isSet()) {
					t.cancel();
					edit.putBoolean(CANCELED_BY_AIRPLANE_MODE, true);
				}
				edit.commit();
			} else {
				// If user had set timer before turning to airplane mode
				// when airplane mode is quit, restore wifi
				if (preferences.getBoolean(CANCELED_BY_AIRPLANE_MODE, false)) {
					edit.putBoolean(CANCELED_BY_AIRPLANE_MODE, false);
					edit.commit();
					if(AppConfig.getWifiTimerUsage(context).equals(AppConfig.MODE_ON_WIFI_DEACTIVATION))
					RadioUtils.setWifiOn(context);
				}
			}
		}
	}
	
}

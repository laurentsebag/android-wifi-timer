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

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class RadioUtils {
	
    public static void setWifiStateBack(Context context) {
		String timerUsage = AppConfig.getWifiTimerUsage(context);
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(timerUsage.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
			manager.setWifiEnabled(true);
		} else {
			manager.setWifiEnabled(false);
		}
    }
    
    public static void setWifiOn(Context context) {
    	WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		manager.setWifiEnabled(true);
    }

	public static boolean getAirplaneMode(Context context) {
    	try {
			int airplaneModeSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON);
			return airplaneModeSetting==1?true:false;
		} catch (SettingNotFoundException e) {
			return false;
		}
	}

}

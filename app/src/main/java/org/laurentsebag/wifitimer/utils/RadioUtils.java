/*-
 *  Copyright (C) 2018 Laurent Sebag
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

package org.laurentsebag.wifitimer.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.laurentsebag.wifitimer.AppConfig;

public class RadioUtils {

    public static void setWifiStateBack(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager != null) {
            String timerUsage = AppConfig.getWifiTimerUsage(context);
            if (timerUsage.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
                manager.setWifiEnabled(true);
            } else {
                manager.setWifiEnabled(false);
            }
        }
    }

    public static void setWifiOn(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager != null) {
            manager.setWifiEnabled(true);
        }
    }
}

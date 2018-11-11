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

package org.laurentsebag.wifitimer;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;

import org.laurentsebag.wifitimer.receivers.WifiStateReceiver;

public class WifiTimerApplication extends Application {
    private static final String TAG = WifiTimerApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //setupBroadcastReceiver();
        setupNetworkListener();
    }

    private void setupBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        registerReceiver(new WifiStateReceiver(), intentFilter);
    }

//    private void setupNetworkListener() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            NetworkRequest networkRequest = new NetworkRequest.Builder()
//                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                    .build();
//
//            if (manager != null) {
//                manager.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback() {
//                    @Override
//                    public void onAvailable(Network network) {
//                        super.onAvailable(network);
//                        Log.d(TAG, "onAvailable " + network);
//                    }
//
//                    @Override
//                    public void onLost(Network network) {
//                        super.onLost(network);
//                        Log.d(TAG, "onLost " + network);
//                    }
//                });
//            }
//        }
//    }

    private void setupNetworkListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();

            if (manager != null) {
                manager.requestNetwork(networkRequest, createWifiStateIntent());
            }
        }
    }

    private PendingIntent createWifiStateIntent() {
        int requestCode = 0;
        Intent intent = new Intent(this, WifiStateReceiver.class);
        intent.setAction("wifi_changed");
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        return PendingIntent.getBroadcast(this, requestCode, intent, flags);
    }
}

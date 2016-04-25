/*-
 *  Copyright (C) 2014 Laurent Sebag
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

import org.laurentsebag.wifitimer.AppConfig;
import org.laurentsebag.wifitimer.utils.RadioUtils;
import org.laurentsebag.wifitimer.Timer;
import org.laurentsebag.wifitimer.activities.TimerActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Receiver for button actions from the notification
 */
public class NotifActionReceiver extends BroadcastReceiver {

    private static final int MINUTE_INCREMENT = 15;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Timer timer = new Timer(context.getApplicationContext());
        long millis;

        if (AppConfig.SNOOZE_ALARM_ACTION.equals(action)) {
            if (intent.hasExtra(TimerActivity.EXTRA_TIME)) {
                millis = intent.getLongExtra(TimerActivity.EXTRA_TIME, 0);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(millis);
                calendar.add(Calendar.MINUTE, MINUTE_INCREMENT);
                timer.set(calendar.getTimeInMillis());
            }
        } else if (AppConfig.CANCEL_ALARM_ACTION.equals(action)) {
            timer.cancel();
        } else if (AppConfig.WIFI_TOGGLE_ACTION.equals(action)) {
            RadioUtils.setWifiStateBack(context.getApplicationContext());
        }
    }
}

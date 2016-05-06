/*-
 *  Copyright (C) 2011 Laurent Sebag
 *  Copyright (C) 2010 Peter Baldwin
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

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;

import org.laurentsebag.wifitimer.activities.TimerActivity;
import org.laurentsebag.wifitimer.receivers.AlarmReceiver;
import org.laurentsebag.wifitimer.receivers.NotifActionReceiver;

import java.text.Format;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Sets/cancels ringer silence timer.
 */
public class Timer {
    private static final String PREF_SET = "set";
    private static final String TIME_ZONE_GMT = "GMT";

    private final Context context;

    public Timer(Context context) {
        this.context = context;
    }

    public static String getFormattedDuration(Context context, long from, long to) {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone(TIME_ZONE_GMT));
        calendar.setTimeInMillis(to - from);
        Resources resources = context.getResources();
        StringBuilder text = new StringBuilder();

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours != 0) {
            final String quantityString = resources.getQuantityString(R.plurals.Nhours, hours, hours);
            text.append(quantityString);
        }

        int minutes = calendar.get(Calendar.MINUTE);
        if (minutes != 0 || text.length() == 0) {
            if (text.length() != 0) {
                text.append(' ');
            }
            text.append(resources.getQuantityString(R.plurals.Nminutes, minutes, minutes));
        }
        return text.toString();
    }

    public void set(long time) {
        showNotification(time);
        setAlarm(time);
        updateTimerSetPreference(true);
    }

    public void cancel() {
        cancelAlarm();
        cancelNotification();
        updateTimerSetPreference(false);
    }

    private void cancelAlarm() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent operation = createAlarmIntent();
        manager.cancel(operation);
    }

    private void cancelNotification() {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(R.id.notification_wifi_off);
    }

    private PendingIntent createAlarmIntent() {
        int requestCode = 0;
        Intent intent = new Intent(context, AlarmReceiver.class);
        int flags = 0;
        return PendingIntent.getBroadcast(context, requestCode, intent, flags);
    }

    private Notification createNotification(long time) {
        CharSequence duration = getFormattedDuration(context, System.currentTimeMillis(), time);
        CharSequence formattedTime = getFormattedTime(time);
        CharSequence contentTitle;
        CharSequence tickerText;
        String mode = AppConfig.getWifiTimerUsage(context);
        if (mode.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
            tickerText = context.getString(R.string.ticker_on_wifi_deactivation, duration);
            contentTitle = context.getString(R.string.notification_title_on_wifi_deactivation, formattedTime);
        } else {
            tickerText = context.getString(R.string.ticker_on_wifi_activation, duration);
            contentTitle = context.getString(R.string.notification_title_on_wifi_activation, formattedTime);
        }

        CharSequence contentText = context.getText(R.string.notification_text);
        PendingIntent contentIntent = createNotificationIntent(time);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setTicker(tickerText)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.wifi_timer_back_on)
                .setOngoing(true)
                .setContentIntent(contentIntent);

        PendingIntent cancelIntent = createNotificationActionIntent(time, AppConfig.CANCEL_ALARM_ACTION);
        PendingIntent snoozeIntent = createNotificationActionIntent(time, AppConfig.SNOOZE_ALARM_ACTION);
        PendingIntent toggleIntent = createNotificationActionIntent(time, AppConfig.WIFI_TOGGLE_ACTION);
        int toggleActionString = mode.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION) ? R.string.notification_action_wifion : R.string.notification_action_wifioff;

        return notificationBuilder
                .addAction(0, context.getString(R.string.notification_action_cancel), cancelIntent)
                .addAction(0, context.getString(R.string.notification_action_snooze), snoozeIntent)
                .addAction(0, context.getString(toggleActionString), toggleIntent)
                .build();
    }

    private PendingIntent createNotificationIntent(long time) {
        int requestCode = 0;
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra(TimerActivity.BUNDLE_EXTRA_TIME, time);
        return PendingIntent.getActivity(context, requestCode, intent, flags);
    }

    private PendingIntent createNotificationActionIntent(long time, String action) {
        int requestCode = 0;
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(context, NotifActionReceiver.class);
        intent.setAction(action);
        intent.putExtra(TimerActivity.BUNDLE_EXTRA_TIME, time);
        return PendingIntent.getBroadcast(context, requestCode, intent, flags);
    }

    private CharSequence getFormattedTime(long date) {
        Format timeFormat = DateFormat.getTimeFormat(context);
        return timeFormat.format(date);
    }

    public boolean isSet() {
        SharedPreferences preferences = context.getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREF_SET, false);
    }

    private void setAlarm(long time) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int type = AlarmManager.RTC_WAKEUP;
        PendingIntent operation = createAlarmIntent();
        manager.set(type, time, operation);
    }

    private void showNotification(long time) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = createNotification(time);
        manager.notify(R.id.notification_wifi_off, notification);
    }

    private void updateTimerSetPreference(boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Timer.PREF_SET, value);
        editor.apply();
    }
}

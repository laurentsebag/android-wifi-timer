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
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.text.format.Time;

import java.text.Format;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Sets/cancels ringer silence timer.
 */
public class Timer {
    private static final String PREF_SET = "set";

    private final Context mContext;

    public Timer(Context context) {
        mContext = context;
    }

    public static CharSequence getFormattedDuration(Context context, long from, long to) {

//        TODO check difference between GMT and UTC
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(to - from);
        Resources resources = context.getResources();
        StringBuilder text = new StringBuilder();

        int hours = calendar.get(Calendar.HOUR);
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
        return text;
    }

    public static long now() {
        //TODO maybe remove this method
        return System.currentTimeMillis();
    }

    public static Time tomorrow() {
//        Time time = now();
//        time.monthDay += 1;
//        time.normalize(true);
//        return time;
        return null; //TODO
    }

    public void set(long time) {
        showNotification(time);
        setAlarm(time);
        updatePreference(PREF_SET, true);
    }

    public void cancel() {
        cancelAlarm();
        cancelNotification();
        updatePreference(PREF_SET, false);
    }

    private void cancelAlarm() {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent operation = createAlarmIntent();
        manager.cancel(operation);
    }

    private void cancelNotification() {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(R.id.notification_wifi_off);
    }

    private PendingIntent createAlarmIntent() {
        int requestCode = 0;
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        int flags = 0;
        return PendingIntent.getBroadcast(mContext, requestCode, intent, flags);
    }

    private Notification createNotification(long time) {
        CharSequence duration = getFormattedDuration(mContext, now(), time);
        CharSequence formattedTime = getFormattedTime(time);
        CharSequence contentTitle;
        CharSequence tickerText;
        String mode = AppConfig.getWifiTimerUsage(mContext);
        if (mode.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
            tickerText = mContext.getString(R.string.ticker_on_wifi_deactivation, duration);
            contentTitle = mContext.getString(R.string.notification_title_on_wifi_deactivation, formattedTime);
        } else {
            tickerText = mContext.getString(R.string.ticker_on_wifi_activation, duration);
            contentTitle = mContext.getString(R.string.notification_title_on_wifi_activation, formattedTime);
        }

        CharSequence contentText = mContext.getText(R.string.notification_text);
        PendingIntent contentIntent = createNotificationIntent(time);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
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
                .addAction(0, mContext.getString(R.string.notification_action_cancel), cancelIntent)
                .addAction(0, mContext.getString(R.string.notification_action_snooze), snoozeIntent)
                .addAction(0, mContext.getString(toggleActionString), toggleIntent)
                .build();
    }

    private PendingIntent createNotificationIntent(long time) {
        int requestCode = 0;
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(mContext, TimerActivity.class);
        intent.putExtra(TimerActivity.EXTRA_TIME, time);
        return PendingIntent.getActivity(mContext, requestCode, intent, flags);
    }

    private PendingIntent createNotificationActionIntent(long time, String action) {
        int requestCode = 0;
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(mContext, NotifActionReceiver.class);
        intent.setAction(action);
        intent.putExtra(TimerActivity.EXTRA_TIME, time);
        return PendingIntent.getBroadcast(mContext, requestCode, intent, flags);
    }

    private CharSequence getFormattedTime(long date) {
        Format timeFormat = DateFormat.getTimeFormat(mContext);
        return timeFormat.format(date);
    }

    public boolean isSet() {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREF_SET, false);
    }

    private void setAlarm(long time) {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        int type = AlarmManager.RTC_WAKEUP;
        PendingIntent operation = createAlarmIntent();
        manager.set(type, time, operation);
    }

    private void showNotification(long time) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = createNotification(time);
        manager.notify(R.id.notification_wifi_off, notification);
    }

    private void updatePreference(String key, boolean value) {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConfig.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}

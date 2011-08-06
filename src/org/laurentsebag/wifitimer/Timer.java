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

import java.text.Format;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.text.format.Time;

/**
 * Sets/cancels ringer silence timer.
 */
public class Timer {

    private static final String PREF_SET = "set";

    private final Context mContext;
    
    public Timer(Context context) {
    	mContext = context;
    }

    private static Date getDate(Time time) {
        long millis = time.toMillis(false);
        return new Date(millis);
    }

    public static CharSequence getFormattedDuration(Context context, Time from, Time to) {
        int millis = (int) (to.toMillis(false) - from.toMillis(false));
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        // Subtract hours
        minutes = minutes % 60;

        Resources resources = context.getResources();
        StringBuilder text = new StringBuilder();
        if (hours != 0) {
            text.append(resources.getQuantityString(R.plurals.Nhours, hours, hours));
        }
        if (minutes != 0 || text.length() == 0) {
            if (text.length() != 0) {
                text.append(' ');
            }
            text.append(resources.getQuantityString(R.plurals.Nminutes, minutes, minutes));
        }
        return text;
    }

    public static Time now() {
        Time time = new Time();
        time.setToNow();
        return time;
    }

    public static Time tomorrow() {
        Time time = now();
        time.monthDay += 1;
        time.normalize(true);
        return time;
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
        NotificationManager manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(R.id.notification_wifi_off);
    }

    private PendingIntent createAlarmIntent() {
        int requestCode = 0;
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        int flags = 0;
        return PendingIntent.getBroadcast(mContext, requestCode, intent, flags);
    }

    private Notification createNotification(Time time) {
    	String mode = AppConfig.getWifiTimerUsage(mContext);
    	
        int icon = R.drawable.wifi_timer_back_on;
        CharSequence duration = getFormattedDuration(mContext, now(), time);
        Date date = getDate(time);
        CharSequence formattedTime = getFormattedTime(date);
        
        CharSequence tickerText;
        CharSequence contentTitle;
        if(mode.equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
        	tickerText = mContext.getString(R.string.ticker_on_wifi_deactivation, duration);
        	contentTitle = mContext.getString(R.string.notification_title_on_wifi_deactivation, formattedTime);
        } else {
        	tickerText = mContext.getString(R.string.ticker_on_wifi_activation, duration);
        	contentTitle = mContext.getString(R.string.notification_title_on_wifi_activation, formattedTime);
        }
        
        long when = System.currentTimeMillis();
        CharSequence contentText = mContext.getText(R.string.notification_text);
        Notification notification = new Notification(icon, tickerText, when);
        PendingIntent contentIntent = createNotificationIntent(time);
        notification.setLatestEventInfo(mContext, contentTitle, contentText, contentIntent);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        return notification;
    }

    private PendingIntent createNotificationIntent(Time time) {
        int requestCode = 0;
        Intent intent = new Intent(mContext, TimerActivity.class);
        long millis = time.toMillis(false);
        intent.putExtra(TimerActivity.EXTRA_TIME, millis);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        return PendingIntent.getActivity(mContext, requestCode, intent, flags);
    }

    private CharSequence getFormattedTime(Date date) {
        Format timeFormat = DateFormat.getTimeFormat(mContext);
        return timeFormat.format(date);
    }

    public boolean isSet() {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConfig.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(PREF_SET, false);
    }

    public void set(Time time) {
        showNotification(time);
        setAlarm(time);
        updatePreference(PREF_SET, true);
    }

    private void setAlarm(Time time) {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        int type = AlarmManager.RTC_WAKEUP;
        long triggerAtTime = time.toMillis(false);
        PendingIntent operation = createAlarmIntent();
        manager.set(type, triggerAtTime, operation);
    }

    private void showNotification(Time time) {
        NotificationManager manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = createNotification(time);
        manager.notify(R.id.notification_wifi_off, notification);
    }

    private void updatePreference(String key, boolean value) {
        SharedPreferences preferences = mContext.getSharedPreferences(AppConfig.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}

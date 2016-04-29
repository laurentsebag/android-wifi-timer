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

package org.laurentsebag.wifitimer.presenters;

import android.content.Context;

import org.laurentsebag.wifitimer.AppConfig;
import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.Timer;
import org.laurentsebag.wifitimer.contracts.TimerActivityContract;
import org.laurentsebag.wifitimer.utils.TimeUtils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimerPresenter implements TimerActivityContract.UserActionsListener {
    public static final int MINUTE_INCREMENT = 10;
    public static final int MINIMUM_INCREMENT = 5;
    public static final long TIME_INVALID = -1;
    private final Context context;
    private final DateFormat formatter;
    private final boolean is24HourFormat;
    private final Timer timer;
    private final TimerActivityContract.View view;
    private final GregorianCalendar calendar;
    final private String[] amPmStrings;

    public TimerPresenter(Context context, DateFormat formatter, boolean is24HourFormat, Timer timer, TimerActivityContract.View view) {
        this.context = context;
        this.formatter = formatter;
        this.is24HourFormat = is24HourFormat;
        this.timer = timer;
        this.view = view;
        calendar = new GregorianCalendar();

        DateFormatSymbols dfs = new DateFormatSymbols();
        amPmStrings = dfs.getAmPmStrings();
    }

    @Override
    public void setTimer() {
        timer.set(calendar.getTimeInMillis());
        view.close();
    }

    @Override
    public void cancelTimer() {
        timer.cancel();
        view.close();
    }

    @Override
    public void undoTimer() {
        timer.cancel();
        view.undoWifiState();
        view.close();
    }

    @Override
    public void increaseTimerHour() {
        calendar.add(Calendar.HOUR, 1);
        updateTime();
    }

    @Override
    public void decreaseTimerHour() {
        calendar.add(Calendar.HOUR, -1);
        updateTime();
    }

    @Override
    public void increaseTimerMinute() {
        calendar.add(Calendar.MINUTE, -(calendar.get(Calendar.MINUTE) % MINUTE_INCREMENT));
        calendar.add(Calendar.MINUTE, MINUTE_INCREMENT);
        updateTime();
    }

    @Override
    public void decreaseTimerMinute() {
        int remainder = calendar.get(Calendar.MINUTE) % MINUTE_INCREMENT;
        if (remainder == 0) {
            calendar.add(Calendar.MINUTE, -MINUTE_INCREMENT);
        } else {
            calendar.add(Calendar.MINUTE, -remainder);
        }
        updateTime();
    }

    @Override
    public void switchAmPm() {
        calendar.add(Calendar.HOUR, 12);
        updateTime();
    }

    @Override
    public void updateTime() {
        Calendar now = new GregorianCalendar();
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, 1);

        while (calendar.before(now)) {
            calendar.add(Calendar.DATE, 1);
        }
        while (calendar.after(tomorrow)) {
            calendar.add(Calendar.DATE, -1);
        }

        String displayHour = TimeUtils.getDisplayHour(calendar, is24HourFormat);
        String displayMinute = TimeUtils.getDisplayMinute(calendar);
        String duration = Timer.getFormattedDuration(context, now.getTimeInMillis(), calendar.getTimeInMillis());

        Date time = new Date(calendar.getTimeInMillis());
        String formattedTime = formatter.format(time);

        view.updateTime(displayHour, displayMinute, amPmStrings[calendar.get(Calendar.AM_PM)], duration, formattedTime);

        Calendar nextDecrease = new GregorianCalendar();
        nextDecrease.setTimeInMillis(calendar.getTimeInMillis());
        nextDecrease.add(Calendar.HOUR, -1);
        view.setDecreaseHourButtonEnabled(!nextDecrease.before(now));

        nextDecrease.setTimeInMillis(calendar.getTimeInMillis());
        nextDecrease.add(Calendar.MINUTE, -TimerPresenter.MINUTE_INCREMENT);
        view.setDecreaseMinuteButtonEnabled(!nextDecrease.before(now));
    }

    @Override
    public void setupTitle(String timerMode) {
        if (AppConfig.MODE_ON_WIFI_ACTIVATION.equals(timerMode)) {
            view.setDialogTitle(R.string.instructions_on_wifi_activation);
        } else {
            view.setDialogTitle(R.string.instructions_on_wifi_deactivation);
        }
    }

    @Override
    public long getTime() {
        return calendar.getTimeInMillis();
    }

    @Override
    public void setTime(long time) {
        if (time > TIME_INVALID) {
            calendar.setTimeInMillis(time);
        } else {
            roundTimeUp(calendar);
        }
    }

    public static void roundTimeUp(Calendar calendar) {
        calendar.set(Calendar.SECOND, 0);
        // Round to the nearest MINUTE_INCREMENT, advancing the clock at least MINIMUM_INCREMENT.
        int remainder = calendar.get(Calendar.MINUTE) % MINUTE_INCREMENT;
        int increment = -remainder + MINUTE_INCREMENT;
        calendar.add(Calendar.MINUTE, increment);
        if (increment < MINIMUM_INCREMENT) {
            calendar.add(Calendar.MINUTE, MINUTE_INCREMENT);
        }
    }

    public void setCalendar(Calendar calendar) {
        this.calendar.setTimeInMillis(calendar.getTimeInMillis());
    }
}

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
    public static final int MINUTE_INCREMENT = 15;
    private final Context context;
    private final DateFormat formatter;
    private final boolean is24HourFormat;
    private final Timer timer;
    private final TimerActivityContract.View view;
    private final GregorianCalendar calendar;
    private String[] amPmStrings;

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
        Calendar tomorrow = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE) + 1);

        while (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        while (calendar.after(tomorrow)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        String displayHour = TimeUtils.getDisplayHour(calendar, is24HourFormat);
        String displayMinute = TimeUtils.getDisplayMinute(calendar);
        String duration = Timer.getFormattedDuration(context, now.getTimeInMillis(), calendar.getTimeInMillis());

        Date time = new Date(calendar.getTimeInMillis());
        String formattedTime = formatter.format(time);

        view.updateTime(displayHour, displayMinute, amPmStrings[calendar.get(Calendar.AM_PM)], duration, formattedTime);
    }

    @Override
    public void setupTitle(String timerMode) {
        if (AppConfig.MODE_ON_WIFI_ACTIVATION.equals(timerMode)) {
            view.setTitle(R.string.instructions_on_wifi_activation);
        } else {
            view.setTitle(R.string.instructions_on_wifi_deactivation);
        }
    }

    public void setCalendar(Calendar calendar) {
        this.calendar.setTimeInMillis(calendar.getTimeInMillis());
    }
}

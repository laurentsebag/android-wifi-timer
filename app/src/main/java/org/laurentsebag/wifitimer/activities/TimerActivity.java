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

package org.laurentsebag.wifitimer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.laurentsebag.wifitimer.AppConfig;
import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.Timer;
import org.laurentsebag.wifitimer.contracts.TimerActivityContract;
import org.laurentsebag.wifitimer.presenters.TimerPresenter;
import org.laurentsebag.wifitimer.utils.RadioUtils;

/**
 * Prompts the user to enter a time at which to turn the ringer back on.
 */
public class TimerActivity extends Activity implements View.OnClickListener, TimerActivityContract.View {

    public static final String EXTRA_TIME = "silencer:time";
    private static final String STATE_TIME = "silencer:time";

    private TextView duration;
    private TextView hour;
    private View incrementHourView;
    private View decrementHourView;
    private TextView minuteTextView;
    private View incrementMinuteView;
    private View decrementMinuteView;
    private Button buttonAmPm;
    private Button buttonSet;
    private Button buttonNever;
    private Button buttonNow;
    private TimerActivityContract.UserActionsListener presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Context context = getApplicationContext();
        boolean is24HourFormat = DateFormat.is24HourFormat(this);
        Timer timer = new Timer(context);
        presenter = new TimerPresenter(context, android.text.format.DateFormat.getTimeFormat(context), is24HourFormat, timer, this);

        View hourPicker = findViewById(R.id.hour);
        hour = (TextView) hourPicker.findViewById(R.id.timepicker_input);
        incrementHourView = hourPicker.findViewById(R.id.increment);
        decrementHourView = hourPicker.findViewById(R.id.decrement);
        hour.setCursorVisible(false);
        incrementHourView.setOnClickListener(this);
        decrementHourView.setOnClickListener(this);

        View minutePicker = findViewById(R.id.minute);
        minuteTextView = (TextView) minutePicker.findViewById(R.id.timepicker_input);
        incrementMinuteView = minutePicker.findViewById(R.id.increment);
        decrementMinuteView = minutePicker.findViewById(R.id.decrement);
        minuteTextView.setCursorVisible(false);
        incrementMinuteView.setOnClickListener(this);
        decrementMinuteView.setOnClickListener(this);
        buttonAmPm = (Button) findViewById(R.id.amPm);
        duration = (TextView) findViewById(R.id.duration);
        buttonAmPm.setOnClickListener(this);
        buttonAmPm.setVisibility(is24HourFormat ? View.GONE : View.VISIBLE);

        buttonSet = (Button) findViewById(R.id.set);
        buttonNever = (Button) findViewById(R.id.never);
        buttonNow = (Button) findViewById(R.id.now);

        buttonSet.setOnClickListener(this);
        buttonNever.setOnClickListener(this);
        buttonNow.setOnClickListener(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();

            long time = intent.getLongExtra(EXTRA_TIME, TimerPresenter.TIME_INVALID);
            presenter.setTime(time);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setupTitle(AppConfig.getWifiTimerUsage(this));
        presenter.updateTime();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_TIME, presenter.getTime());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.setTime(savedInstanceState.getLong(STATE_TIME));
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View v) {
        if (v == buttonSet) {
            presenter.setTimer();
        } else if (v == buttonNever) {
            presenter.cancelTimer();
        } else if (v == buttonNow) {
            presenter.undoTimer();
        } else if (v == incrementHourView) {
            presenter.increaseTimerHour();
        } else if (v == decrementHourView) {
            presenter.decreaseTimerHour();
        } else if (v == incrementMinuteView) {
            presenter.increaseTimerMinute();
        } else if (v == decrementMinuteView) {
            presenter.decreaseTimerMinute();
        } else if (v == buttonAmPm) {
            presenter.switchAmPm();
        }
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void undoWifiState() {
        RadioUtils.setWifiStateBack(this);
    }

    @Override
    public void setDialogTitle(int titleId) {
        TextView textView = (TextView) findViewById(R.id.timer_activity_instructions);
        textView.setText(titleId);
    }

    @Override
    public void updateTime(String displayedHours, String displayedMinutes, String amPm, String duration, String formattedTime) {
        hour.setText(displayedHours);
        minuteTextView.setText(displayedMinutes);
        this.buttonAmPm.setText(amPm);
        this.duration.setText(duration);
        buttonSet.setText(formattedTime);
    }
}

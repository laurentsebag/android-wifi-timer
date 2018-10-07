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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
public class TimerActivity extends AppCompatActivity implements View.OnClickListener, TimerActivityContract.View {

    public static final String BUNDLE_EXTRA_TIME = "extra_time";
    private static final String STATE_TIME = "time";
    private static final int TIME_INVALID = -1;

    private TextView duration;
    private TextView hour;
    private View increaseHourView;
    private View decreaseHourView;
    private TextView minuteTextView;
    private View increaseMinuteView;
    private View decreaseMinuteView;
    private Button buttonAmPm;
    private Button buttonSet;
    private Button buttonNever;
    private Button buttonNow;
    private TimerActivityContract.UserActionsListener presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timer);

        Context context = getApplicationContext();
        boolean is24HourFormat = DateFormat.is24HourFormat(this);
        Timer timer = new Timer(context);
        presenter = new TimerPresenter(context, android.text.format.DateFormat.getTimeFormat(context), is24HourFormat, timer, this);

        View hourPicker = findViewById(R.id.hour);
        hour = hourPicker.findViewById(R.id.time_picker_input);
        increaseHourView = hourPicker.findViewById(R.id.increase);
        decreaseHourView = hourPicker.findViewById(R.id.decrease);
        hour.setCursorVisible(false);
        increaseHourView.setOnClickListener(this);
        decreaseHourView.setOnClickListener(this);

        View minutePicker = findViewById(R.id.minute);
        minuteTextView = minutePicker.findViewById(R.id.time_picker_input);
        increaseMinuteView = minutePicker.findViewById(R.id.increase);
        decreaseMinuteView = minutePicker.findViewById(R.id.decrease);
        minuteTextView.setCursorVisible(false);
        increaseMinuteView.setOnClickListener(this);
        decreaseMinuteView.setOnClickListener(this);
        buttonAmPm = findViewById(R.id.amPm);
        duration = findViewById(R.id.duration);
        buttonAmPm.setOnClickListener(this);
        buttonAmPm.setVisibility(is24HourFormat ? View.GONE : View.VISIBLE);

        buttonSet = findViewById(R.id.set);
        buttonNever = findViewById(R.id.never);
        buttonNow = findViewById(R.id.now);

        buttonSet.setOnClickListener(this);
        buttonNever.setOnClickListener(this);
        buttonNow.setOnClickListener(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();

            long time = intent.getLongExtra(BUNDLE_EXTRA_TIME, TimerPresenter.TIME_INVALID);
            presenter.setTime(time);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String wifiTimerUsage = AppConfig.getWifiTimerUsage(this);
        presenter.setupTitle(wifiTimerUsage);
        presenter.updateTime();

        trackTimeToDisplayDialog();
    }

    private void trackTimeToDisplayDialog() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long wifiChangeTime = sharedPreferences.getLong(AppConfig.PREFERENCE_KEY_WIFI_CHANGE_TIME, TIME_INVALID);
        if (wifiChangeTime > TIME_INVALID) {
            sharedPreferences.edit().remove(AppConfig.PREFERENCE_KEY_WIFI_CHANGE_TIME).apply();
        }
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
        } else if (v == increaseHourView) {
            presenter.increaseTimerHour();
        } else if (v == decreaseHourView) {
            presenter.decreaseTimerHour();
        } else if (v == increaseMinuteView) {
            presenter.increaseTimerMinute();
        } else if (v == decreaseMinuteView) {
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
        TextView textView = findViewById(R.id.timer_activity_instructions);
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

    @Override
    public void setDecreaseHourButtonEnabled(boolean enabled) {
        decreaseHourView.setEnabled(enabled);
    }

    @Override
    public void setDecreaseMinuteButtonEnabled(boolean enabled) {
        decreaseMinuteView.setEnabled(enabled);
    }
}

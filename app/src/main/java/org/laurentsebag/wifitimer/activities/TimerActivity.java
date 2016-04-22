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

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Prompts the user to enter a time at which to turn the ringer back on.
 */
public class TimerActivity extends Activity implements View.OnClickListener, TimerActivityContract.View {

    public static final String EXTRA_TIME = "silencer:time";
    private static final String STATE_TIME = "silencer:time";

    private Timer mTimer;
    private TextView mDuration;
    private TextView mHour;
    private View mIncrementHour;
    private View mDecrementHour;
    private TextView mMinute;
    private View mIncrementMinute;
    private View mDecrementMinute;
    private Button mAmPm;
    private Button mButtonSet;
    private Button mButtonNever;
    private Button mButtonNow;
    private GregorianCalendar mCalendar;
    private TimerActivityContract.UserActionsListener mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        final Context context = getApplicationContext();
        mTimer = new Timer(context);
        mPresenter = new TimerPresenter(context, android.text.format.DateFormat.getTimeFormat(context), DateFormat.is24HourFormat(this), mTimer, this);

        View hourPicker = findViewById(R.id.hour);
        mHour = (TextView) hourPicker.findViewById(R.id.timepicker_input);
        mIncrementHour = hourPicker.findViewById(R.id.increment);
        mDecrementHour = hourPicker.findViewById(R.id.decrement);
        mHour.setCursorVisible(false);
        mIncrementHour.setOnClickListener(this);
        mDecrementHour.setOnClickListener(this);

        View minutePicker = findViewById(R.id.minute);
        mMinute = (TextView) minutePicker.findViewById(R.id.timepicker_input);
        mIncrementMinute = minutePicker.findViewById(R.id.increment);
        mDecrementMinute = minutePicker.findViewById(R.id.decrement);
        mMinute.setCursorVisible(false);
        mIncrementMinute.setOnClickListener(this);
        mDecrementMinute.setOnClickListener(this);
        mAmPm = (Button) findViewById(R.id.amPm);
        mDuration = (TextView) findViewById(R.id.duration);
        mAmPm.setOnClickListener(this);
        //        TODO
        //        mAmPm.setVisibility(is24HourFormat() ? View.GONE : View.VISIBLE);

        mButtonSet = (Button) findViewById(R.id.set);
        mButtonNever = (Button) findViewById(R.id.never);
        mButtonNow = (Button) findViewById(R.id.now);

        mButtonSet.setOnClickListener(this);
        mButtonNever.setOnClickListener(this);
        mButtonNow.setOnClickListener(this);

        mCalendar = new GregorianCalendar();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_TIME)) {
                mCalendar.setTimeInMillis(intent.getLongExtra(EXTRA_TIME, 0));
            } else {
                mCalendar.set(Calendar.SECOND, 0);

                // Round to the nearest 15 minutes, advancing
                // the clock at least 10 minutes.
                int remainder = mCalendar.get(Calendar.MINUTE) % 15;
                mCalendar.add(Calendar.MINUTE, -remainder + 15);
                if (remainder > 10) {
                    mCalendar.add(Calendar.MINUTE, 15);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = (TextView) findViewById(R.id.timer_activity_instructions);

        //        TODO
        //        if (AppConfig.getWifiTimerUsage(this).equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
        //            textView.setText(R.string.instructions_on_wifi_deactivation);
        //        } else {
        //            textView.setText(R.string.instructions_on_wifi_activation);
        //        }

        mPresenter.setupTitle(AppConfig.getWifiTimerUsage(this));
        mPresenter.updateTime();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_TIME, mCalendar.getTimeInMillis());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCalendar.setTimeInMillis(savedInstanceState.getLong(STATE_TIME));
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View v) {
        if (v == mButtonSet) {
            mPresenter.setTimer();
        } else if (v == mButtonNever) {
            mPresenter.cancelTimer();
        } else if (v == mButtonNow) {
            mPresenter.undoTimer();
        } else if (v == mIncrementHour) {
            mPresenter.increaseTimerHour();
        } else if (v == mDecrementHour) {
            mPresenter.decreaseTimerHour();
        } else if (v == mIncrementMinute) {
            mPresenter.increaseTimerMinute();
        } else if (v == mDecrementMinute) {
            mPresenter.decreaseTimerMinute();
        } else if (v == mAmPm) {
            mPresenter.switchAmPm();
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
    public void updateTime(String displayedHours, String displayedMinutes, String amPm, String duration, String formattedTime) {
        //        mHour.setText(displayHour);
        //        mMinute.setText(displayMinute);
        //        mAmPm.setText(mAmPmStrings[calendar.get(Calendar.AM_PM)]);
        //        mDuration.setText(duration);
        //        mButtonSet.setText(formattedTime);
    }
}

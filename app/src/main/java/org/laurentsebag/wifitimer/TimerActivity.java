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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

/**
 * Prompts the user to enter a time at which to turn the ringer back on.
 */
public class TimerActivity extends Activity implements View.OnClickListener {

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

    private String[] mAmPmStrings;

    private Button mButtonSet;

    private Button mButtonNever;

    private Button mButtonNow;

    private Time mTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mTimer = new Timer(getApplicationContext());

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
        mAmPm.setVisibility(is24HourFormat() ? View.GONE : View.VISIBLE);

        DateFormatSymbols dfs = new DateFormatSymbols();
        mAmPmStrings = dfs.getAmPmStrings();

        mButtonSet = (Button) findViewById(R.id.set);
        mButtonNever = (Button) findViewById(R.id.never);
        mButtonNow = (Button) findViewById(R.id.now);

        mButtonSet.setOnClickListener(this);
        mButtonNever.setOnClickListener(this);
        mButtonNow.setOnClickListener(this);

        mTime = new Time();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_TIME)) {
                long millis = intent.getLongExtra(EXTRA_TIME, 0);
                mTime.set(millis);
            } else {
                mTime.setToNow();
                mTime.second = 0;

                // Round to the nearest 15 minutes, advancing
                // the clock at least 10 minutes.
                int remainder = mTime.minute % 15;
                mTime.minute -= remainder;
                mTime.minute += 15;
                if (remainder > 10) {
                    mTime.minute += 15;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = (TextView) findViewById(R.id.timer_activity_instructions);

        if (AppConfig.getWifiTimerUsage(this).equals(AppConfig.MODE_ON_WIFI_DEACTIVATION)) {
            textView.setText(R.string.instructions_on_wifi_deactivation);
        } else {
            textView.setText(R.string.instructions_on_wifi_activation);
        }

        updateTime();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_TIME, mTime.toMillis(false));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTime.set(savedInstanceState.getLong(STATE_TIME));
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View v) {
        if (v == mButtonSet) {
            mTimer.set(mTime);
            finish();
        } else if (v == mButtonNever) {
            mTimer.cancel();
            finish();
        } else if (v == mButtonNow) {
            RadioUtils.setWifiStateBack(this);
            mTimer.cancel();
            finish();
        } else if (v == mIncrementHour) {
            mTime.hour++;
            updateTime();
        } else if (v == mDecrementHour) {
            mTime.hour--;
            updateTime();
        } else if (v == mIncrementMinute) {
            mTime.minute -= (mTime.minute % 15);
            mTime.minute += 15;
            updateTime();
        } else if (v == mDecrementMinute) {
            int remainder = mTime.minute % 15;
            if (remainder == 0) {
                mTime.minute -= 15;
            } else {
                mTime.minute -= remainder;
            }
            updateTime();
        } else if (v == mAmPm) {
            mTime.hour += 12;
            updateTime();
        }
    }

    private void updateTime() {
        mTime.normalize(false);

        Time now = Timer.now();
        Time tomorrow = Timer.tomorrow();

        while (mTime.before(now)) {
            mTime.monthDay++;
            mTime.normalize(true);
        }
        while (mTime.after(tomorrow)) {
            mTime.monthDay--;
            mTime.normalize(true);
        }

        String displayHour = getDisplayHour();
        mHour.setText(displayHour);

        String displayMinute = getDisplayMinute();
        mMinute.setText(displayMinute);

        boolean isPm = (mTime.hour >= 12);
        mAmPm.setText(mAmPmStrings[isPm ? Calendar.PM : Calendar.AM]);

        CharSequence duration = Timer.getFormattedDuration(this, now, mTime);
        mDuration.setText(duration);

        Date time = new Date(mTime.toMillis(false));
        DateFormat formatter = android.text.format.DateFormat.getTimeFormat(this);
        CharSequence formattedTime = formatter.format(time);
        mButtonSet.setText(formattedTime);
    }

    private boolean is24HourFormat() {
        return android.text.format.DateFormat.is24HourFormat(this);
    }

    private String getDisplayHour() {
        int hour = mTime.hour;
        if (is24HourFormat()) {
            return String.format("%02d", hour);
        } else {
            if (hour >= 12) {
                hour -= 12;
            }
            if (hour == 0) {
                hour = 12;
            }
            return String.valueOf(hour);
        }
    }

    private String getDisplayMinute() {
        return String.format("%02d", mTime.minute);
    }
}

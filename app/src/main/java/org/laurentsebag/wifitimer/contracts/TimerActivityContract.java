/*-
 *  Copyright (C) 2018 Laurent Sebag
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

package org.laurentsebag.wifitimer.contracts;

public interface TimerActivityContract {
    interface View {
        void close();

        void undoWifiState();

        void setDialogTitle(int titleId);

        void updateTime(String displayedHours, String displayedMinutes, String amPm, String duration, String formattedTime);

        void setDecreaseHourButtonEnabled(boolean enabled);

        void setDecreaseMinuteButtonEnabled(boolean enabled);
    }

    interface UserActionsListener {
        void setTimer();

        void cancelTimer();

        void undoTimer();

        void increaseTimerHour();

        void decreaseTimerHour();

        void increaseTimerMinute();

        void decreaseTimerMinute();

        void switchAmPm();

        void updateTime();

        void setupTitle(String timerMode);

        void setTime(long time);

        long getTime();
    }
}

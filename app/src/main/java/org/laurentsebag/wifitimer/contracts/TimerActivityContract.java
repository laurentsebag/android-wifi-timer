package org.laurentsebag.wifitimer.contracts;

public class TimerActivityContract {
    public interface View {
        void close();

        void undoWifiState();

        void setTitle(int titleId);

        void updateTime(String displayedHours, String displayedMinutes, String amPm, String duration, String formattedTime);
    }

    public interface UserActionsListener {
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
    }
}

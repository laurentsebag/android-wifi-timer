package org.laurentsebag.wifitimer.presenters;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.laurentsebag.wifitimer.AppConfig;
import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.Timer;
import org.laurentsebag.wifitimer.contracts.TimerActivityContract;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimerPresenterTest {

    public static final String DISPLAYED_MINUTES_0 = "00";
    private static final int TEST_CALENDAR_HOUR_AM = 7;
    private static final int TEST_CALENDAR_HOUR_PM = 14;
    private static final int TEST_CALENDAR_MINUTE = 30;
    private static final boolean FORMAT_24 = true;
    private static final boolean FORMAT_AM_PM = false;
    public static final String PM = "PM";
    public static final String AM = "AM";
    private static final long HOUR_IN_MILLIS = 60 * 60 * 1000;
    private static final long MINUTE_INCREMENT_IN_MILLIS = TimerPresenter.MINUTE_INCREMENT * 60 * 1000;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;

    @Mock
    private Context context;
    @Mock
    private DateFormat formatter;
    @Mock
    private Resources resources;
    @Mock
    private Timer timer;
    @Mock
    private TimerActivityContract.View view;

    private TimerPresenter presenter;
    private TimerPresenter presenterAmPm;
    private Calendar testCalendarPm;
    private Calendar testCalendarAm;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TimerPresenter(context, formatter, FORMAT_24, timer, view);
        presenterAmPm = new TimerPresenter(context, formatter, FORMAT_AM_PM, timer, view);

        testCalendarPm = new GregorianCalendar();
        testCalendarPm.set(Calendar.HOUR_OF_DAY, TEST_CALENDAR_HOUR_PM);
        testCalendarPm.set(Calendar.MINUTE, TEST_CALENDAR_MINUTE);
        presenter.setCalendar(testCalendarPm);
        presenterAmPm.setCalendar(testCalendarPm);

        testCalendarAm = new GregorianCalendar();
        testCalendarAm.set(Calendar.HOUR_OF_DAY, TEST_CALENDAR_HOUR_AM);
        testCalendarAm.set(Calendar.MINUTE, TEST_CALENDAR_MINUTE);

        when(context.getResources()).thenReturn(resources);
        when(formatter.format(any(Date.class), any(StringBuffer.class), any(FieldPosition.class))).thenReturn(new StringBuffer());
    }

    @Test
    public void setTimer_shouldSetTimerAndCloseView() throws Exception {
        presenter.setTimer();
        verify(timer).set(testCalendarPm.getTimeInMillis());
        verify(view).close();
    }

    @Test
    public void cancelTimer_shouldCancelTimerAndCloseView() throws Exception {
        presenter.cancelTimer();
        verify(timer).cancel();
        verify(view).close();
    }

    @Test
    public void undoTimer_shouldRestoreWifiStateAndCloseView() throws Exception {
        presenter.undoTimer();
        verify(timer).cancel();
        verify(view).undoWifiState();
        verify(view).close();
    }

    @Test
    public void increaseTimerHour_shouldAddHourIn24Format() throws Exception {
        presenter.increaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM + 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarPm.getTimeInMillis() + HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void increaseTimerHour_shouldAddHourInAmPmFormatAfternoon() throws Exception {
        presenterAmPm.increaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM - 12 + 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarPm.getTimeInMillis() + HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void increaseTimerHour_shouldAddHourInAmPmFormatMorning() throws Exception {
        presenterAmPm.setCalendar(testCalendarAm);
        presenterAmPm.increaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_AM + 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(AM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarAm.getTimeInMillis() + HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void decreaseTimerHour_shouldRemoveHourIn24Format() throws Exception {
        presenter.decreaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM - 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarPm.getTimeInMillis() - HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void decreaseTimerHour_shouldRemoveHourInAmPmFormatMorning() throws Exception {
        presenterAmPm.setCalendar(testCalendarAm);
        presenterAmPm.decreaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_AM - 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(AM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarAm.getTimeInMillis() - HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void decreaseTimerHour_shouldRemoveHourInAmPmFormatAfternoon() throws Exception {
        presenterAmPm.decreaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM - 12 - 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarPm.getTimeInMillis() - HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void increaseTimerMinute_shouldAddIncrementMinutes() throws Exception {
        presenter.increaseTimerMinute();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM)), eq(String.valueOf(TEST_CALENDAR_MINUTE + TimerPresenter.MINUTE_INCREMENT)), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarPm.getTimeInMillis() + MINUTE_INCREMENT_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void decreaseTimerMinute_shouldRemoveIncrementMinutes() throws Exception {
        presenter.decreaseTimerMinute();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM)), eq(String.valueOf(TEST_CALENDAR_MINUTE - TimerPresenter.MINUTE_INCREMENT)), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarPm.getTimeInMillis() - MINUTE_INCREMENT_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void decreaseTimerMinute_shouldRoundToQuarter() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(testCalendarPm.getTimeInMillis());
        final int minute = 3;
        calendar.set(Calendar.MINUTE, minute);
        presenter.setCalendar(calendar);
        presenter.decreaseTimerMinute();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM)), eq(DISPLAYED_MINUTES_0), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(calendar.getTimeInMillis() - minute * MINUTE_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void switchAmPm_shouldMakeMorningAfternoon() throws Exception {
        presenterAmPm.setCalendar(testCalendarAm);
        presenterAmPm.switchAmPm();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_AM)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(PM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarAm.getTimeInMillis() + 12 * HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void switchAmPm_shouldMakeAfternoonMorning() throws Exception {
        presenterAmPm.switchAmPm();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR_PM - 12)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq(AM), anyString(), anyString());
        verify(formatter).format(eq(new Date(testCalendarPm.getTimeInMillis() - 12 * HOUR_IN_MILLIS)), any(StringBuffer.class), any(FieldPosition.class));
        verify(view, never()).close();
    }

    @Test
    public void setupTitle_shouldSetTitleWithDeactivationMode() throws Exception {
        presenter.setupTitle(AppConfig.MODE_ON_WIFI_DEACTIVATION);
        verify(view).setDialogTitle(R.string.instructions_on_wifi_deactivation);
    }

    @Test
    public void setupTitle_shouldSetTitleWithActivationMode() throws Exception {
        presenter.setupTitle(AppConfig.MODE_ON_WIFI_ACTIVATION);
        verify(view).setDialogTitle(R.string.instructions_on_wifi_activation);
    }

    @Test
    public void setupTitle_shouldHandleInvalidMode() throws Exception {
        presenter.setupTitle(null);
        verify(view).setDialogTitle(R.string.instructions_on_wifi_deactivation);
    }

    @Test
    public void getTime_shouldReturnTimeInMillis() throws Exception {
        assertThat(presenter.getTime(), is(testCalendarPm.getTimeInMillis()));
    }

    @Test
    public void setTime_shouldIncreaseCurrentTime() throws Exception {
        presenter.setCalendar(testCalendarAm);
        presenter.setTime(TimerPresenter.TIME_INVALID);
        testCalendarAm.set(Calendar.SECOND, 0);
        long time = testCalendarAm.getTimeInMillis() + TimerPresenter.MINUTE_INCREMENT * MINUTE_IN_MILLIS;
        assertThat(presenter.getTime(), is(time));
    }

    @Test
    public void setTime_shouldRoundCurrentTimeUp() throws Exception {
        testCalendarAm.set(Calendar.MINUTE, TEST_CALENDAR_MINUTE + 12);
        presenter.setCalendar(testCalendarAm);
        presenter.setTime(TimerPresenter.TIME_INVALID);
        testCalendarAm.set(Calendar.SECOND, 0);
        testCalendarAm.set(Calendar.MINUTE, TEST_CALENDAR_MINUTE);
        long time = testCalendarAm.getTimeInMillis() + TimerPresenter.MINUTE_INCREMENT * 2 * MINUTE_IN_MILLIS;
        assertThat(presenter.getTime(), is(time));
    }

    @Test
    public void setTime_shouldSetToExactTime() throws Exception {
        presenter.setCalendar(testCalendarAm);
        long timeInMillis = testCalendarPm.getTimeInMillis() + MINUTE_IN_MILLIS;
        presenter.setTime(timeInMillis);
        assertThat(presenter.getTime(), is(timeInMillis));
    }
}

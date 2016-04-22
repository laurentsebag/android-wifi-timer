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

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimerPresenterTest {

    private static final int TEST_CALENDAR_HOUR = 14;
    private static final int TEST_CALENDAR_MINUTE = 30;
    private static final boolean FORMAT_24 = true;
    private static final boolean FORMAT_AM_PM = false;

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
    private Calendar testCalendar;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new TimerPresenter(context, formatter, FORMAT_24, timer, view);
        testCalendar = new GregorianCalendar();
        testCalendar.set(Calendar.HOUR_OF_DAY, TEST_CALENDAR_HOUR);
        testCalendar.set(Calendar.MINUTE, TEST_CALENDAR_MINUTE);
        presenter.setCalendar(testCalendar);
        when(context.getResources()).thenReturn(resources);
        when(formatter.format(any(Date.class), any(StringBuffer.class), any(FieldPosition.class))).thenReturn(new StringBuffer());
    }

    @Test
    public void setTimer() throws Exception {
        presenter.setTimer();
        verify(timer).set(testCalendar.getTimeInMillis());
        verify(view).close();
    }

    @Test
    public void cancelTimer() throws Exception {
        presenter.cancelTimer();
        verify(timer).cancel();
        verify(view).close();
    }

    @Test
    public void undoTimer() throws Exception {
        presenter.undoTimer();
        verify(timer).cancel();
        verify(view).undoWifiState();
        verify(view).close();
    }

    @Test
    public void increaseTimerHour_shouldAddHour() throws Exception {
        presenter.increaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR + 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), or(isNull(String.class), eq("")), eq("1 hour"),
                //                TODO do other arg
                anyString());
        verify(view, never()).close();
    }

    @Test
    public void increaseTimerHour_shouldAddHourInAmPmFormat() throws Exception {
        presenter.increaseTimerHour();
        verify(view).updateTime(eq(String.valueOf(TEST_CALENDAR_HOUR + 1)), eq(String.valueOf(TEST_CALENDAR_MINUTE)), eq("PM"), eq("1 hour"),
                //                TODO do other arg
                anyString());
        verify(view, never()).close();
        //        testCalendar.get
    }

    @Test
    public void decreaseTimerHour() throws Exception {
        presenter.decreaseTimerHour();
        verify(view).updateTime(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(view, never()).close();
    }

    @Test
    public void increaseTimerMinute() throws Exception {
        presenter.increaseTimerMinute();
        verify(view).updateTime(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(view, never()).close();
    }

    //    @Test
    //    public void increaseTimerMinute2() throws Exception {
    //        presenter.increaseTimerMinute();
    //        presenter.decreaseTimerMinute();
    //        verify(view, times(2)).updateTime(anyString(), anyString(), anyString(), anyString(), anyString());
    //        verify(view, never()).close();
    //    }

    @Test
    public void decreaseTimerMinute_shouldRemove15Min() throws Exception {
        presenter.decreaseTimerMinute();
        verify(view).updateTime(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(view, never()).close();
    }

    @Test
    public void decreaseTimerMinute_shouldRoundToQuarter() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(testCalendar.getTimeInMillis());
        calendar.set(Calendar.MINUTE, 3);
        presenter.setCalendar(calendar);
        presenter.decreaseTimerMinute();
        verify(view).updateTime(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(view, never()).close();
    }

    @Test
    public void switchAmPm() throws Exception {
        presenter.switchAmPm();
        verify(view).updateTime(anyString(), anyString(), anyString(), anyString(), anyString());
        verify(view, never()).close();
    }

    @Test
    public void updateTime() throws Exception {
        presenter.updateTime();
        verify(view).updateTime(anyString(), anyString(), anyString(), anyString(), anyString());
        // TODO try to cover all cases of updateTime
    }

    @Test
    public void setupTitle_shouldSetTitleWithDeactivationMode() throws Exception {
        presenter.setupTitle(AppConfig.MODE_ON_WIFI_DEACTIVATION);
        verify(view).setTitle(R.string.instructions_on_wifi_deactivation);
    }

    @Test
    public void setupTitle_shouldSetTitleWithActivationMode() throws Exception {
        presenter.setupTitle(AppConfig.MODE_ON_WIFI_ACTIVATION);
        verify(view).setTitle(R.string.instructions_on_wifi_activation);
    }

    @Test
    public void setupTitle_shouldHandleInvalidMode() throws Exception {
        presenter.setupTitle(null);
        verify(view).setTitle(R.string.instructions_on_wifi_deactivation);
    }
}

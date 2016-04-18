package org.laurentsebag.wifitimer;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.NotNull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimerTest {
    private static final long TIME_HOUR_MILLIS = 60 * 60 * 1000;

    @Mock
    Context context;
    @Mock
    Resources resources;

    private Timer timer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        timer = new Timer(context);
    }

    @Test
    public void getFormattedDuration_shouldFormatLessThanHour() throws Exception {
        long from = System.currentTimeMillis();
        long to = from + (long) (TIME_HOUR_MILLIS * 0.5);
        when(context.getResources()).thenReturn(resources);
        when(resources.getQuantityString(eq(R.plurals.Nminutes), anyInt(), anyInt())).thenReturn("minute");
        CharSequence result = Timer.getFormattedDuration(context, from, to);
        verify(resources, never()).getQuantityString(eq(R.plurals.Nhours), anyInt(), anyInt());
        verify(resources).getQuantityString(R.plurals.Nminutes, 30, 30);
        assertThat(result, NotNull.NOT_NULL);
        assertThat("returned value shouldn\'t be empty", result.length() > 0);
//        assertThat(new String(result), contains("minutes"));
//        assertThat(new String(result), contains("hour"));
//        TODO add tests on string in other tests
    }

    @Test
    public void getFormattedDuration_shouldFormatMoreThanHour() throws Exception {
        long from = System.currentTimeMillis();
        long to = from + (long) (TIME_HOUR_MILLIS * 2.5);
        when(context.getResources()).thenReturn(resources);
        when(resources.getQuantityString(eq(R.plurals.Nminutes), anyInt(), anyInt())).thenReturn("minute");
        when(resources.getQuantityString(eq(R.plurals.Nhours), anyInt(), anyInt())).thenReturn("hour");
        CharSequence result = Timer.getFormattedDuration(context, from, to);
        verify(resources).getQuantityString(R.plurals.Nhours, 2, 2);
        verify(resources).getQuantityString(R.plurals.Nminutes, 30, 30);
        assertThat(result, NotNull.NOT_NULL);
        assertThat("returned value shouldn\'t be empty", result.length() > 0);
    }

    @Test
    public void getFormattedDuration_shouldFormatOClockTime() throws Exception {
        long from = System.currentTimeMillis();
        long to = from + TIME_HOUR_MILLIS * 3;
        when(context.getResources()).thenReturn(resources);
        when(resources.getQuantityString(eq(R.plurals.Nhours), anyInt(), anyInt())).thenReturn("hour");
        CharSequence result = Timer.getFormattedDuration(context, from, to);
        verify(resources).getQuantityString(R.plurals.Nhours, 3, 3);
        verify(resources, never()).getQuantityString(eq(R.plurals.Nminutes), anyInt(), anyInt());
        assertThat(result, NotNull.NOT_NULL);
        assertThat("returned value shouldn\'t be empty", result.length() > 0);
    }
}

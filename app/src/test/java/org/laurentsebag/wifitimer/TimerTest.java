/*-
 *  Copyright (C) 2016 Laurent Sebag
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

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimerTest {
    private static final long TIME_HOUR_MILLIS = 60 * 60 * 1000;
    private static final String MINUTE = "minute";
    private static final String HOUR = "hour";

    @Mock
    Context context;
    @Mock
    Resources resources;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFormattedDuration_shouldFormatLessThanHour() throws Exception {
        long from = System.currentTimeMillis();
        long to = from + (long) (TIME_HOUR_MILLIS * 0.5);
        when(context.getResources()).thenReturn(resources);
        when(resources.getQuantityString(eq(R.plurals.Nminutes), anyInt(), anyInt())).thenReturn(MINUTE);
        when(resources.getQuantityString(eq(R.plurals.Nhours), anyInt(), anyInt())).thenReturn(HOUR);
        String result = String.valueOf(Timer.getFormattedDuration(context, from, to));
        verify(resources, never()).getQuantityString(eq(R.plurals.Nhours), anyInt(), anyInt());
        verify(resources).getQuantityString(R.plurals.Nminutes, 30, 30);
        assertThat(result, not(isEmptyOrNullString()));
        assertThat(result, containsString(MINUTE));
        assertThat(result, not(containsString(HOUR)));
    }

    @Test
    public void getFormattedDuration_shouldFormatMoreThanHour() throws Exception {
        long from = System.currentTimeMillis();
        long to = from + (long) (TIME_HOUR_MILLIS * 2.5);
        when(context.getResources()).thenReturn(resources);
        when(resources.getQuantityString(eq(R.plurals.Nminutes), anyInt(), anyInt())).thenReturn(MINUTE);
        when(resources.getQuantityString(eq(R.plurals.Nhours), anyInt(), anyInt())).thenReturn(HOUR);
        String result = Timer.getFormattedDuration(context, from, to);
        verify(resources).getQuantityString(R.plurals.Nhours, 2, 2);
        verify(resources).getQuantityString(R.plurals.Nminutes, 30, 30);
        assertThat(result, not(isEmptyOrNullString()));
        assertThat(result, containsString(MINUTE));
        assertThat(result, containsString(HOUR));
    }

    @Test
    public void getFormattedDuration_shouldFormatOClockTime() throws Exception {
        long from = System.currentTimeMillis();
        long to = from + TIME_HOUR_MILLIS * 3;
        when(context.getResources()).thenReturn(resources);
        when(resources.getQuantityString(eq(R.plurals.Nminutes), anyInt(), anyInt())).thenReturn(MINUTE);
        when(resources.getQuantityString(eq(R.plurals.Nhours), anyInt(), anyInt())).thenReturn(HOUR);
        String result = String.valueOf(Timer.getFormattedDuration(context, from, to));
        verify(resources).getQuantityString(R.plurals.Nhours, 3, 3);
        verify(resources, never()).getQuantityString(eq(R.plurals.Nminutes), anyInt(), anyInt());
        assertThat(result, not(isEmptyOrNullString()));
        assertThat(result, not(containsString(MINUTE)));
        assertThat(result, containsString(HOUR));
    }
}

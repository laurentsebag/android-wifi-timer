package org.laurentsebag.wifitimer.views;

import android.content.Context;
import android.support.v7.preference.ListPreference;
import android.util.AttributeSet;

public class ListPreferenceSummary extends ListPreference {

    public ListPreferenceSummary(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ListPreferenceSummary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListPreferenceSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListPreferenceSummary(Context context) {
        super(context);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        setSummary(getEntry());
    }
}

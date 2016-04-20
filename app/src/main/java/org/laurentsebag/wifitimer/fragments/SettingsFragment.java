package org.laurentsebag.wifitimer.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.laurentsebag.wifitimer.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }
}

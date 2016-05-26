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

package org.laurentsebag.wifitimer.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers;

import org.laurentsebag.wifitimer.BuildConfig;
import org.laurentsebag.wifitimer.R;

public class SettingsFragment extends PreferenceFragmentCompatDividers {

    private static final String PLAY_STORE_APP_URI_BASE = "market://details?id=";
    private static final String PLAY_STORE_URL_BASE = "https://play.google.com/store/apps/details?id=";
    private static final String GITHUB_CREATE_BUG_URL = "https://github.com/laurentsebag/android-wifi-timer/issues/new?body=Device%20name%3A%0AAndroid%20version%3A%0A%0A----%0A%0AReproduction%20steps%3A%0A&labels=bug";
    private static final String GITHUB_REQUEST_FEATURE_URL = "https://github.com/laurentsebag/android-wifi-timer/issues/new?labels=feature%20request";

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);

        findPreference(getString(R.string.preference_report_bug_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_CREATE_BUG_URL)));
                return true;
            }
        });

        findPreference(getString(R.string.preference_request_feature_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_REQUEST_FEATURE_URL)));
                return true;
            }
        });

        findPreference(getString(R.string.preference_rate_app_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openPlayStore();
                return true;
            }
        });
    }

    private void openPlayStore() {
        Uri uri = Uri.parse(PLAY_STORE_APP_URI_BASE + BuildConfig.APPLICATION_ID);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL_BASE + BuildConfig.APPLICATION_ID)));
        }
    }
}

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

package org.laurentsebag.wifitimer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.fragments.AboutDialogFragment;
import org.laurentsebag.wifitimer.utils.TrackerUtils;

public class MainActivity extends TrackedToolbarActivity implements View.OnClickListener {

    private static final String ABOUT_DIALOG = "about_dialog";
    private AboutDialogFragment aboutDialog;
    private View mainContainer;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupViews();
        aboutDialog = new AboutDialogFragment();
    }

    private void setupViews() {
        mainContainer = findViewById(R.id.main_container);

        View view = findViewById(R.id.done);
        if (view != null) {
            view.setOnClickListener(this);
        }

        view = findViewById(R.id.settings);
        if (view != null) {
            view.setOnClickListener(this);
        }

        view = findViewById(R.id.about);
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    protected void onResume(boolean appEnabled) {
        super.onResume(appEnabled);
        if (!appEnabled) {
            if (snackbar == null || !snackbar.isShownOrQueued()) {
                snackbar = Snackbar.make(mainContainer, R.string.snack_bar_app_disabled, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.snack_bar_action_enable, this);
                snackbar.show();
            }
        } else if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.done:
                //                TODO revert changes after finished testing onboarding
                //                finish();
                //                Intent intent = new Intent(this, TimerActivity.class);
                intent = new Intent(this, OnboardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.about:
                //                aboutDialog.show(getSupportFragmentManager(), ABOUT_DIALOG);
                intent = new Intent(this, TimerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.snackbar_action:
                enableApp();
                break;
        }
    }

    private void enableApp() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(appEnabledKey, true);
        editor.apply();
        transitionToolbarColor(true);
        TrackerUtils.trackClick(tracker, TrackerUtils.TRACK_CATEGORY_SNACK_BAR, TrackerUtils.TRACK_LABEL_ENABLE_APP);
    }
}

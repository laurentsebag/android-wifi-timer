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

package org.laurentsebag.wifitimer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.fragments.OnboardingFragment;

public class OnboardingActivity extends OnboardingBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addScreen(OnboardingFragment.newInstance(R.string.onboarding_title_screen1, R.string.onboarding_subtitle_screen1, R.drawable.onboarding_screen_1));
        addScreen(OnboardingFragment.newInstance(R.string.onboarding_title_screen2, R.string.onboarding_subtitle_screen2, R.drawable.onboarding_screen_2));
        addScreen(OnboardingFragment.newInstance(R.string.onboarding_title_screen3, R.string.onboarding_subtitle_screen3, R.drawable.onboarding_screen_3));
        addScreen(OnboardingFragment.newInstance(R.string.onboarding_title_screen4, R.string.onboarding_subtitle_screen4, R.drawable.onboarding_screen_4));
    }

    @Override
    protected void onSkip() {
        finish();
    }

    @Override
    protected void onDone() {
        finish();
    }
}

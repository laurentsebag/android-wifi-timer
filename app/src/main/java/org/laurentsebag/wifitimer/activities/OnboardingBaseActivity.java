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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.laurentsebag.wifitimer.R;
import org.laurentsebag.wifitimer.fragments.OnboardingFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class OnboardingBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private OnboardingFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private View buttonSkip;
    private View buttonPrevious;
    private View buttonNext;
    private View buttonDone;

    // TODO add progress indicator
    // TODO convert the slide images into different resolutions

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        setupViews();
    }

    private void setupViews() {
        pagerAdapter = new OnboardingFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(new OnboardingPageChangeListener());
        }

        buttonSkip = findViewById(R.id.onboarding_button_skip);
        if (buttonSkip != null) {
            buttonSkip.setOnClickListener(this);
        }

        buttonPrevious = findViewById(R.id.onboarding_button_previous);
        if (buttonPrevious != null) {
            buttonPrevious.setOnClickListener(this);
        }

        buttonNext = findViewById(R.id.onboarding_button_next);
        if (buttonNext != null) {
            buttonNext.setOnClickListener(this);
        }

        buttonDone = findViewById(R.id.onboarding_button_done);
        if (buttonDone != null) {
            buttonDone.setOnClickListener(this);
        }
    }

    void addScreen(@NonNull OnboardingFragment fragment) {
        pagerAdapter.addFragment(fragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onboarding_button_skip:
                onSkip();
                break;
            case R.id.onboarding_button_previous:
                goToPreviousScreen();
                break;
            case R.id.onboarding_button_next:
                goToNextScreen();
                break;
            case R.id.onboarding_button_done:
                onDone();
                break;
        }
    }

    private void goToPreviousScreen() {
        int previousItem = viewPager.getCurrentItem() - 1;
        if (previousItem >= 0) {
            viewPager.setCurrentItem(previousItem);
        }
    }

    private void goToNextScreen() {
        int nextItem = viewPager.getCurrentItem() + 1;
        if (nextItem < viewPager.getAdapter().getCount()) {
            viewPager.setCurrentItem(nextItem);
        }
    }

    protected abstract void onSkip();

    protected abstract void onDone();

    private class OnboardingFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<OnboardingFragment> fragments;

        OnboardingFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(@NonNull OnboardingFragment fragment) {
            fragments.add(fragment);
            notifyDataSetChanged();
        }
    }

    private class OnboardingPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            if (position == viewPager.getAdapter().getCount() - 1) {
                buttonSkip.setVisibility(View.GONE);
                buttonNext.setVisibility(View.GONE);
                buttonDone.setVisibility(View.VISIBLE);
            } else {
                buttonSkip.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                buttonPrevious.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                buttonNext.setVisibility(View.VISIBLE);
                buttonDone.setVisibility(View.GONE);
            }
        }
    }
}

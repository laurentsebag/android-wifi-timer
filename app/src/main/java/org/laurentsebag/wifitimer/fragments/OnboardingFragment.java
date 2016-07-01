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

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.laurentsebag.wifitimer.R;

public class OnboardingFragment extends Fragment {

    private static final String BUNDLE_KEY_TITLE = "title";
    private static final String BUNDLE_KEY_SUBTITLE = "subtitle";
    private static final String BUNDLE_KEY_IMAGE = "image";

    public static OnboardingFragment newInstance(@StringRes int title, @StringRes int subtitle, @DrawableRes int image) {
        OnboardingFragment onboardingFragment = new OnboardingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_TITLE, title);
        bundle.putInt(BUNDLE_KEY_SUBTITLE, subtitle);
        bundle.putInt(BUNDLE_KEY_IMAGE, image);
        onboardingFragment.setArguments(bundle);
        return onboardingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        Bundle bundle = getArguments();
        int title = bundle.getInt(BUNDLE_KEY_TITLE);
        int subtitle = bundle.getInt(BUNDLE_KEY_SUBTITLE);
        int image = bundle.getInt(BUNDLE_KEY_IMAGE);

        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.subtitle)).setText(subtitle);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(image);

        return view;
    }
}

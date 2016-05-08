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
import android.os.Bundle;
import android.view.View;

import org.laurentsebag.wifitimer.fragments.AboutDialogFragment;
import org.laurentsebag.wifitimer.R;

public class MainActivity extends TrackedFragmentActivity implements View.OnClickListener {

    private static final String ABOUT_DIALOG = "about_dialog";
    private AboutDialogFragment aboutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        findViewById(R.id.done).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        aboutDialog = new AboutDialogFragment();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                finish();
                break;
            case R.id.about:
                aboutDialog.show(getSupportFragmentManager(), ABOUT_DIALOG);
                break;
            case R.id.settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }
    }
}

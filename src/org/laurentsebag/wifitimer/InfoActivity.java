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

package org.laurentsebag.wifitimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends Activity implements View.OnClickListener {
    
    private static final int ABOUT_DIALOG = 0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        findViewById(R.id.done).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                finish();
                break;
            case R.id.about:
            	showDialog(ABOUT_DIALOG);
            	break;
            case R.id.settings:
            	Intent i = new Intent(this, SettingsActivity.class);
            	startActivity(i);
            	break;
        }
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    	case ABOUT_DIALOG:
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle(R.string.about_dialog_title);
    		TextView content = new TextView(this);
    		content.setText(R.string.about_dialog_content);
    		content.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    		content.setMovementMethod(LinkMovementMethod.getInstance());
    		builder.setView(content);
    		return builder.create(); 
		default:
			return super.onCreateDialog(id);
    	}
    }
}

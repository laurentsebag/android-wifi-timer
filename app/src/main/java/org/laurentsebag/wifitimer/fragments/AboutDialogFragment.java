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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.widget.TextView;

import org.laurentsebag.wifitimer.R;

public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int padding = getResources().getDimensionPixelSize(R.dimen.about_dialog_padding);
        final Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.about_dialog_title);
        TextView content = new TextView(context);
        content.setText(R.string.about_dialog_content);
        content.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        content.setPadding(padding, padding, padding, padding);
        builder.setView(content);
        return builder.create();
    }
}

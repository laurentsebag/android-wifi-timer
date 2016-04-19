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

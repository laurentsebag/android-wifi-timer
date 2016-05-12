package org.laurentsebag.wifitimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public abstract class TrackedWifiStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    protected void trackTimerCancel() {

    }

    protected void showWifiDialog(Context context, SharedPreferences.Editor editor) {

    }

    protected void savePreference(SharedPreferences.Editor editor) {
        editor.commit();
    }
}

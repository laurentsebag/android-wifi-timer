package org.laurentsebag.wifitimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class TrackedNotifActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    protected void trackNotificationClick(String label) {

    }

    protected void trackTimerCancel() {

    }
}

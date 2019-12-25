package com.developer.anishakd4.duckhunt.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.developer.anishakd4.duckhunt.Listeners.OnHomePressedListener;

public class InnerReceiver extends BroadcastReceiver {

    final String SYSTEM_DIALOG_REASON_KEY = "reason";
    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private OnHomePressedListener listener;

    public InnerReceiver(OnHomePressedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (listener != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        listener.onHomePressed();
                    }
                }
            }
        }
    }
}

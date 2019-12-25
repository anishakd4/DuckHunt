package com.developer.anishakd4.duckhunt.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.developer.anishakd4.duckhunt.Listeners.OnHomePressedListener;

public class HomeWatcher {

    private Context context;
    private IntentFilter filter;
    private OnHomePressedListener listener;
    private InnerReceiver recevier;

    public HomeWatcher(Context context) {
        this.context = context;
        filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        this.listener = listener;
        recevier = new InnerReceiver(listener);
    }

    public void startWatch() {
        if (recevier != null) {
            context.registerReceiver(recevier, filter);
        }
    }

    public void stopWatching() {
        if (recevier != null) {
            context.registerReceiver(recevier, filter);
            context.unregisterReceiver(recevier);
        }
    }

}

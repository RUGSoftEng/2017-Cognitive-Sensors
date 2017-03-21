/*
Ashton Spina
07/03/2017

Receives broadcasts from notifications and starts the main menu
 */
package com.teamwan.wander;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by LaminatedLama on 07-Mar-17.
 */

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    //receives intent from broadcast and starts the mainmenu
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);
    }
}

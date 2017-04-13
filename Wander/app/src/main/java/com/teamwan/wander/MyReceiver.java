/**
 *  This class receives an intent from a broadcast and
 *  starts the mainActivity if a notification is clicked.
 *
 * @author  Ashton Spina
 * @version 1.0
 * @since   2017-03-07
 */

package com.teamwan.wander;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {}

    @Override
    /**
     * This class receives broadcasted intents in order to start the app
     */
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);
    }
}

/**
 *  This handles creating a new notification that is scheduled
 *  for a certain time.
 *
 * @author  Ashton Spina
 * @version 1.0
 * @since   2017-03-07
 */

package com.teamwan.wander;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
     /**
     * Creates a notification which can launch the main menu activity upon click
      * */
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle("Remember:");
        builder.setContentText("Its time to Wander!");
        builder.setSmallIcon(R.drawable.ic_launcher);

        builder.setPriority(Notification.PRIORITY_HIGH);
        Intent notifyIntent = new Intent(this, MainMenu.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}
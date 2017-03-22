package com.teamwan.wander.db;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

//TODO for class: Remove toasts
public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Toast.makeText(context, "Connected to " + activeNetwork.getTypeName(), Toast.LENGTH_LONG).show();
                new DBUpload().execute(new DBpars(context));
//                Intent uplIntent = new Intent(context, DBUpload.class);
//                uplIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(uplIntent);
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Toast.makeText(context, "No WiFi connection", Toast.LENGTH_LONG).show();
            }
        } else {
            // not connected to the internet
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }

    }
}

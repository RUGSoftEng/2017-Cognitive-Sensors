package com.teamwan.wander.db;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
* Class that checks whether there is wifi connection; if so, send the data to the server
*/

//TODO for class: Remove toasts
public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // Connected to WIFI
                // Toast.makeText(context, "Connected to " + activeNetwork.getTypeName()+", uploading data", Toast.LENGTH_LONG).show();
                Log.i("ConnectivityReceiver", "Connected to WIFI, transferring data.");
                new DBDownload().execute(new DBpars(context)); //download questions from server
                new DBUpload().execute(new DBpars(context)); //send the stored local data to server
            }
        }

    }
}
